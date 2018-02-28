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
}