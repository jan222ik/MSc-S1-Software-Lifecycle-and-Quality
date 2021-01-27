package at.fhv.bpmn;


import org.assertj.core.util.VisibleForTesting;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.function.Consumer;
import java.util.logging.Logger;

public class DueBookInfoWorker implements JavaDelegate {
    private final static Logger LOGGER = Logger.getLogger(DueBookInfoWorker.class.getName());

    @Override
    public void execute(final DelegateExecution delegateExecution) {
        LOGGER.info("Executing due book info worker...");
        final Integer dueprocess = (Integer) delegateExecution.getVariable("overdueresult");
        try {
            boolean allowed = checkOverdueTime(dueprocess, LOGGER::info);
            delegateExecution.setVariable("allowed", allowed);
            LOGGER.info("User can proceed: " + allowed);
        } catch (RuntimeException ex) {
            LOGGER.warning(ex.getMessage());
        }
    }

    @VisibleForTesting
    protected boolean checkOverdueTime(final Integer overdueTime, Consumer<String> logStatement) {
        boolean allowed = true;
        switch (overdueTime) {
            case 1:
                logStatement.accept("Enjoy your visit!");
                break;
            case 2:
                logStatement.accept("You have between 15 and 30 days of overdue books. Please return them.");
                break;
            case 3:
                logStatement.accept("You have more than 30 days of overdue books. Before you return your overdue books, you cannot use the library.");
                allowed = false;
                break;
            default:
                throw new RuntimeException("This shouldn't have happened, check your decision table.");
        }
        return allowed;
    }

}
