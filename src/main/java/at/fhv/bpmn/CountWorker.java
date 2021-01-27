package at.fhv.bpmn;


import org.assertj.core.util.VisibleForTesting;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.HashMap;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class CountWorker implements JavaDelegate {
    private final static Logger LOGGER = Logger.getLogger(CountWorker.class.getName());


    @Override
    public void execute(final DelegateExecution delegateExecution) {
        LOGGER.info("Executing count worker...");
        delegateExecution.setVariable("reason_selection", "No error");
        HashMap<String, Long> chosenBooks = (HashMap<String, Long>) delegateExecution.getVariable("user_books");
        Integer bookSum = countBooks(chosenBooks);
        delegateExecution.setVariable("total_amount", bookSum);
    }

    @VisibleForTesting
    protected int countBooks(HashMap<String, Long> chosenBooks) {
        return chosenBooks.values().stream().flatMapToInt(e -> IntStream.of(e.intValue())).sum();
    }
}