package voteservice;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static voteservice.VoteController.PAGE_VALUES;

public class VoteControllerTest {

    private VoteController controller;
    private ResponseEntity<Integer> lastResponse;
    private ResponseEntity<Page<Integer>> pageResponseEntity;

    @Test
    public void fetchLast() throws Exception {
        givenIHaveAController();
        whenICallFetchLast();
        thenIAmReturnedAValue();
    }

    private void givenIHaveAController() {
        controller = new VoteController();
    }


    private void whenICallFetchLast() {
        lastResponse = controller.fetchLast();
    }

    private void thenIAmReturnedAValue() {
        assertEquals("Status not equal to 200", HttpStatus.OK, lastResponse.getStatusCode());
        assertEquals("Value not as expected", PAGE_VALUES.get(0), lastResponse.getBody());

    }

    @Test
    public void fetchPaginated() throws Exception {


        givenIHaveAController();
        andIcallFetchLastPaginatedWithinPageSizeLimit();
        thenIAmReturnedAResultOf(HttpStatus.OK, 1, 3, 5, 1);
        thenIAmReturnedTheCorrectNumberOfTotalElements();

        andIcallFetchLastPaginatedWithValueOverPageSizeLimit();
        thenIAmReturnedAResultOf(HttpStatus.PARTIAL_CONTENT, 1, 3, 5, 1, 7);
        thenIAmReturnedTheCorrectNumberOfTotalElements();

        andIcallFetchLastPaginatedWithValueOverTotalPageSize();
        thenIAmReturnedAnEmptyResult();

        andIcallFetchLastPaginatedWithNoValues();
        thenIAmReturnedAnEmptyResult();
        thenIAmReturnedTheCorrectNumberOfTotalElements();

        andICallFetchPaginatedWithALargePageSizeButValidNumber();
        thenIAmReturnedAResultOf(HttpStatus.OK, 1, 5, 5, 1);
        // As the page size is larger, the total number of elements is wrong. See README.md


        andIcallFetchLastPaginatedWithValueAtPageLimits();
        thenIAmReturnedAResultOf(HttpStatus.PARTIAL_CONTENT, 1, 5, 5, 1);
        // As we are retrieving a partial page, the total number of elements is wrong. See README.md

        andICallLast3FromPage4();
        thenIAmReturnedAResultOf(HttpStatus.OK, 30, 1, 5);
        // As we are retrieving a partial page, the total number of elements is wrong. See README.md

    }

    private void thenIAmReturnedTheCorrectNumberOfTotalElements() {
        assertEquals("not correct number of total elements", 14, pageResponseEntity.getBody().getTotalElements());
    }

    private void thenIAmReturnedTheCorrectNumberOfPages(int numberOfPages) {
        assertEquals("not correct number of pages", numberOfPages, pageResponseEntity.getBody().getTotalPages());
    }

    private void andICallLast3FromPage4() {
        pageResponseEntity = controller.fetchPaginated(3, 4);
    }

    private void andICallFetchPaginatedWithALargePageSizeButValidNumber() {
        pageResponseEntity = controller.fetchPaginated(5, 3);
    }

    private void andIcallFetchLastPaginatedWithValueOverTotalPageSize() {
        pageResponseEntity = controller.fetchPaginated(3, 1000);
    }

    private void andIcallFetchLastPaginatedWithValueAtPageLimits() {
        pageResponseEntity = controller.fetchPaginated(10, 3);
    }

    private void thenIAmReturnedAnEmptyResult() {
        assertEquals("Status not equal to 200", HttpStatus.OK, pageResponseEntity.getStatusCode());
        assertEquals("Body should be empty", false, pageResponseEntity.getBody().hasContent());
    }

    private void andIcallFetchLastPaginatedWithNoValues() {
        pageResponseEntity = controller.fetchPaginated(0, 1);
    }

    private void andIcallFetchLastPaginatedWithValueOverPageSizeLimit() {
        pageResponseEntity = controller.fetchPaginated(8, 1);
    }

    private void thenIAmReturnedAResultOf(HttpStatus statusCode, Integer... values) {
        assertEquals("Status not equal to status code", statusCode, pageResponseEntity.getStatusCode());

        assertEquals("Number of results not as expected ", values.length, pageResponseEntity.getBody().getContent().size());

        int count = 0;
        for (Integer actualValue : pageResponseEntity.getBody().getContent()) {
            assertEquals("Value not as expected", values[count], actualValue);
            count++;
        }


    }

    private void andIcallFetchLastPaginatedWithinPageSizeLimit() {
        pageResponseEntity = controller.fetchPaginated(4, 1);
    }

}