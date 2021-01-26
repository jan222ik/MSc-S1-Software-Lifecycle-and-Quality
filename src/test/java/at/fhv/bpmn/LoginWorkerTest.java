package at.fhv.bpmn;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class LoginWorkerTest {

    private final LoginWorker worker = new LoginWorker();

    @Test
    void checkFalsePassword() {
        assertFalse(worker.checkPassword("fail", "fail"));
    }

    @Test
    void testValidPassword() {
        assertTrue(worker.checkPassword("test", "abc"));
    }
}
