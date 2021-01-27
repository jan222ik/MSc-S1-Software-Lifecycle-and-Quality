package at.fhv.bpmn;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class DueBookInfoWorkerTest {
    private final DueBookInfoWorker dbiw = new DueBookInfoWorker();

    @Test
    void checkOverdueTimeOne() {
        assertTrue(dbiw.checkOverdueTime(1, (msg) -> {
        }));
    }

    @Test
    void checkOverdueTimeTwo() {
        assertTrue(dbiw.checkOverdueTime(2, (msg) -> {
        }));
    }

    @Test
    void checkOverdueTimeThree() {
        assertFalse(dbiw.checkOverdueTime(3, (msg) -> {
        }));
    }

    @Test
    void checkOverdueTimeOneLogging() {
        AtomicBoolean hasLogged = new AtomicBoolean(false);
        assertTrue(dbiw.checkOverdueTime(1, (msg) -> {
            assertEquals("Enjoy your visit!", msg);
            hasLogged.set(true);
        }));
        assertTrue(hasLogged.get());
    }

    @Test
    void checkOverdueTimeTwoLogging() {
        AtomicBoolean hasLogged = new AtomicBoolean(false);
        assertTrue(dbiw.checkOverdueTime(2, (msg) -> {
            assertEquals("You have between 15 and 30 days of overdue books. Please return them.", msg);
            hasLogged.set(true);
        }));
        assertTrue(hasLogged.get());
    }

    @Test
    void checkOverdueTimeThreeLogging() {
        AtomicBoolean hasLogged = new AtomicBoolean(false);
        assertFalse(dbiw.checkOverdueTime(3, (msg) -> {
            assertEquals("You have more than 30 days of overdue books. Before you return your overdue books, you cannot use the library.", msg);
            hasLogged.set(true);
        }));
        assertTrue(hasLogged.get());
    }

    @Test
    void unknownProcessNumber() {
        assertThrows(RuntimeException.class, () -> dbiw.checkOverdueTime(4, (msg) -> {
        }));
    }

}