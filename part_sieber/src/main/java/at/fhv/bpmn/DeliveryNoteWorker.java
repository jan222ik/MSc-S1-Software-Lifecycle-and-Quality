package at.fhv.bpmn;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.HashMap;
import java.util.logging.Logger;

public class DeliveryNoteWorker implements JavaDelegate {
    private final static Logger LOGGER = Logger.getLogger(DeliveryNoteWorker.class.getName());
    private final static HashMap<String, String> userNames = new HashMap<String, String>() {{
        put("admin", "Armin Admin");
        put("test", "Max Mustermann");
        put("mat", "Matthias Rupp");
        put("jan", "Janik Mayr");
    }};

    @Override
    public void execute(final DelegateExecution delegateExecution) {
        LOGGER.info("Executing delivery note worker...");
        final String username = (String) delegateExecution.getVariable("username");
        final String realName = userNames.get(username);
        final HashMap<String, Long> chosenBooks = (HashMap<String, Long>) delegateExecution.getVariable("user_books");
        final StringBuilder builder = new StringBuilder("\nYour order ").append(realName).append(": \n");
        chosenBooks.forEach((k, v) -> builder.append("\tBookname: ").append(k).append(", amount: ").append(v).append("\n"));
        final Double cost = (Double) delegateExecution.getVariable("delivery_cost");
        builder.append("Cost for delivery: ").append(cost);
        delegateExecution.setVariable("del_note", builder.toString());
        LOGGER.info(builder.toString());
    }

}
