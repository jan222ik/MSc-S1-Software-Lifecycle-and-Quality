package at.fhv.bpmn;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class ConfirmationMailWorker implements JavaDelegate {
    private final static Logger LOGGER = Logger.getLogger(ConfirmationMailWorker.class.getName());


    @Override
    public void execute(final DelegateExecution delegateExecution) {
        System.out.println("Executing confirmation mail worker...");
        final String note = (String) delegateExecution.getVariable("del_note");
        LOGGER.info("Hello.\nYour order has been processed.\nSee the appended delivery note.\n" + note + "\nPlease remember that all books need to be returned within a month!\nThank you for your patronage.\nBest regards,\nYour library");
    }

}
