package at.fhv.bpmn;


import org.assertj.core.util.VisibleForTesting;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class DueBookInfoWorker implements JavaDelegate {
    private final static Logger LOGGER = Logger.getLogger(DueBookInfoWorker.class.getName());

    @Override
    public void execute(final DelegateExecution delegateExecution) {
        System.out.println("Executing...");
        final Integer dueprocess = (Integer) delegateExecution.getVariable("dueprocess");
        LOGGER.info("PROCNUM " + dueprocess);
        boolean allowed = checkOverdueTime(dueprocess);
        delegateExecution.setVariable("allowed", allowed);
        LOGGER.info("User can proceed: " + allowed);
    }

    @VisibleForTesting
    protected boolean checkOverdueTime(final Integer overdueTime) {
        boolean allowed = true;
        switch (overdueTime) {
            case 1:
                LOGGER.info("Enjoy your visit!");
                break;
            case 2:
                LOGGER.info("You have between 15 and 30 days of overdue books. Please return them.");
                break;
            case 3:
                LOGGER.info("You have more than 30 days of overdue books. Before you return your overdue books, you cannot use the library.");
                allowed = false;
                break;
            default:
                LOGGER.warning("This shouldn't have happened, check your decision table.");

        }
        return allowed;
    }

}
