package at.fhv.bpmn;


import org.assertj.core.util.VisibleForTesting;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.HashMap;
import java.util.logging.Logger;

public class BookAvailabilityWorker implements JavaDelegate {
    private final static Logger LOGGER = Logger.getLogger(BookAvailabilityWorker.class.getName());
    private final static HashMap<String, Long> bookList = new HashMap<String, Long>() {{
        put("Harry Potter", 100L);
        put("Haskell for Dummies", 10L);
        put("Java for Dummies", 30L);
        put("English for runaways", 15L);
        put("UML modeling", 23L);
        put("SysML modeling", 12L);
        put("Eclipse documentation", 1L);
        put("A", 4L);
        put("B", 0L);
        put("C", 40L);
    }};

    @Override
    public void execute(final DelegateExecution delegateExecution) {
        LOGGER.info("Executing book availability worker...");
        delegateExecution.setVariable("reason_selection", "No error");
        HashMap<String, Long> userBooks = new HashMap<>();
        HashMap<String, Long> chosenBooks = (HashMap<String, Long>) delegateExecution.getVariable("user_books");
        final String bookName = (String) delegateExecution.getVariable("book_name");

        final Long amount = (Long) delegateExecution.getVariable("book_amount");
        final boolean enough = enoughBooks(bookName, amount);
        if (chosenBooks != null) {
            userBooks = chosenBooks;
        }
        if (doesBookExist(bookName)) {
            if (enough) {
                userBooks.put(bookName, amount);
            } else {
                delegateExecution.setVariable("reason_selection", "Not enough books available");
            }
        } else {
            delegateExecution.setVariable("reason_selection", "This book does not exist in the library");
        }

        delegateExecution.setVariable("enough_books", enough);
        delegateExecution.setVariable("user_books", userBooks);
    }

    @VisibleForTesting
    protected boolean doesBookExist(String bookname) {
        return bookList.containsKey(bookname);
    }


    @VisibleForTesting
    protected boolean enoughBooks(String bookname, Long amount) {
        if (amount <= 0) return false;
        final Long available = bookList.getOrDefault(bookname, 0L);
        return available >= amount;
    }

}
