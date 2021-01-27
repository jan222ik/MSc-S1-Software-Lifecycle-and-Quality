package at.fhv.bpmn;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookAvailabilityWorkerTest {
    private final BookAvailabilityWorker baw = new BookAvailabilityWorker();

    @Test
    void doesBookExistTrue() {
        assertTrue(baw.doesBookExist("Harry Potter"));
    }

    @Test
    void doesBookExistFalse() {
        assertFalse(baw.doesBookExist("This book should not exist"));
    }

    @Test
    void enoughBooksTrue() {
        assertTrue(baw.enoughBooks("A", 3L));
    }

    @Test
    void enoughBooksFalse() {
        assertFalse(baw.enoughBooks("B", 100L));
    }
}