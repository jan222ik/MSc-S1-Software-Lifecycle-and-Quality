package at.fhv.bpmn;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class CountWorkerTest {
    private final CountWorker cw = new CountWorker();
    private final static HashMap<String, Long> chosenBooks = new HashMap<String, Long>() {{
        put("Harry Potter", 30L);
        put("Haskell for Dummies", 5L);
        put("A", 2L);
    }};

    @Test
    void countBooks() {
        assertEquals(37L, cw.countBooks(chosenBooks));
    }

    @Test
    void countEmptyBooksList() {
        assertEquals(0L, cw.countBooks(new HashMap<>()));
    }
}