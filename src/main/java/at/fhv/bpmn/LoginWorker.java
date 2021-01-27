package at.fhv.bpmn;


import org.assertj.core.util.VisibleForTesting;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.HashMap;
import java.util.logging.Logger;

public class LoginWorker implements JavaDelegate {
    private final static Logger LOGGER = Logger.getLogger(LoginWorker.class.getName());
    private final static HashMap<String, String> userData = new HashMap<String, String>() {{
        put("admin", "admin");
        put("test", "abc");
        put("mat", "1234");
        put("jan", "22ik");
    }};
    private final static HashMap<String, Integer> dueBookTime = new HashMap<String, Integer>() {{
        put("admin", 3);
        put("test", 15);
        put("mat", 0);
        put("jan", 100);
    }};

    @Override
    public void execute(final DelegateExecution delegateExecution) {
        LOGGER.info("Executing login worker...");
        final String username = (String) delegateExecution.getVariable("username");
        final String enteredPassword = (String) delegateExecution.getVariable("password");
        final Integer overdueTime = (Integer) dueBookTime.get(username);
        LOGGER.info("Attempting login for user " + username + "....");
        final boolean correct = checkPassword(username, enteredPassword);
        LOGGER.info("Login info correct: " + correct);
        delegateExecution.setVariable("correct", correct);
        delegateExecution.setVariable("totalovertime", overdueTime);

    }

    @SuppressWarnings("WeakerAccess")
    @VisibleForTesting
    protected boolean checkPassword(final String username, final String passwordPhrase) {
        final String correctPassword = userData.get(username);
        return correctPassword != null && correctPassword.equals(passwordPhrase);
    }
}
