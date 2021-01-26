package at.fhv.bpmn;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class DeliveryCostWorker implements JavaDelegate {
    private final static Logger LOGGER = Logger.getLogger(DeliveryCostWorker.class.getName());


    @Override
    public void execute(final DelegateExecution delegateExecution) {
        LOGGER.info("Executing delivery cost worker...");
        delegateExecution.setVariable("reason_selection", "No error");
        Integer amount = (Integer) delegateExecution.getVariable("total_amount");
        double cost = 0.0;
        if(amount >= 5 && amount < 10 ) cost = 5.5;
        else if (amount >= 10 && amount < 20) cost = 9.9;
        else if(amount >= 20 && amount < 30) cost = 20.0;
        else if(amount >= 30) cost = 35.0;
        delegateExecution.setVariable("delivery_cost", cost);
    }
}
