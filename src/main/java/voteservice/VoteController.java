package voteservice;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by matt on 31/07/2016.
 */
@RestController
@RequestMapping("/vote")
public class VoteController {

    public static final List<Integer> PAGE_VALUES = Arrays.asList(1, 3, 5, 1, 7, 8, 10, 12, 15, 30, 1, 5, 5, 1);
    private static final Logger LOG = LoggerFactory.getLogger(VoteController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/last")
    public ResponseEntity<Integer> fetchLast() {
        LOG.info("Getting last vote");

        Integer voteValue = PAGE_VALUES.get(0);
        HttpStatus resultantStatus = voteValue == null ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return new ResponseEntity<Integer>(voteValue, resultantStatus);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/last/{numberOfResultsPerPage}/from/{pageNumber}")
    public ResponseEntity<Page<Integer>> fetchPaginated(
            @PathVariable int numberOfResultsPerPage, @PathVariable int pageNumber) {

        Page<Integer> p = null;
        HttpStatus httpStatusCode = HttpStatus.OK;
        if (numberOfResultsPerPage > 5) {
            numberOfResultsPerPage = 5;
            LOG.trace("Limited request to 5 results per page");
            httpStatusCode = HttpStatus.PARTIAL_CONTENT;
        }
        Pageable pageRequest;
        if (numberOfResultsPerPage == 0 || queryResultsInOverflow(pageNumber, numberOfResultsPerPage)) {
            return createEmptyPage();
        }

        pageRequest = new PageRequest(pageNumber, numberOfResultsPerPage);

        Page<Integer> page = getPage(pageRequest);
        return new ResponseEntity<Page<Integer>>(page, httpStatusCode);


    }

    private boolean queryResultsInOverflow(@RequestParam("page") int pageNumber, @RequestParam("numberOfResultsPerPage") int numberOfResultsPerPage) {
        return pageNumber * numberOfResultsPerPage - numberOfResultsPerPage > PAGE_VALUES.size();
    }

    private ResponseEntity<Page<Integer>> createEmptyPage() {
        Page page = new PageImpl<Integer>(new ArrayList<>(), new PageRequest(1, 1), PAGE_VALUES.size());
        return new ResponseEntity<Page<Integer>>(page, HttpStatus.OK);
    }

    private Page<Integer> getPage(Pageable pageRequest) {


        int startingIndex = pageRequest.getOffset() - pageRequest.getPageSize();

        List<Integer> results = new ArrayList<>();


        for (int position = startingIndex; position < PAGE_VALUES.size() && results.size() < pageRequest.getPageSize(); position++) {
            results.add(PAGE_VALUES.get(position));
        }


        Page<Integer> pageToReturn = new PageImpl<Integer>(results, pageRequest, PAGE_VALUES.size());
        return pageToReturn;
    }

    // offset is 1 based not 0 based (like our array)
    private boolean pageValuesContainsResults(Pageable pageRequest, int offset) {
        return offset <= PAGE_VALUES.size() - pageRequest.getPageSize();
    }
    
}
