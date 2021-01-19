package at.fhv.bpmn;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.HashMap;
import java.util.logging.Logger;

public class LoginWorker implements JavaDelegate {
    private final static Logger LOGGER =Logger.getLogger(LoginWorker.class.getName());
    private final static HashMap<String, String> userData = new HashMap<String, String>() {{
        put("admin", "admin");
        put("test", "abc");
        put("mat", "1234");
        put("jan", "22ik");
    }};

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("Executing...");
        ExternalTaskClient client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080")
                .asyncResponseTimeout(1000)
                .build();

        client.subscribe("check-login")
                .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {
                    boolean correct = false;
                    // put business logic

                    String username = (String) externalTask.getVariable("username");
                    String enteredPassword = (String) externalTask.getVariable("password");

                    System.out.println("Attempting login for user " + username + "....");

                    String correctPassword = userData.get(username);
                    if(correctPassword != null) {
                        if(correctPassword.equals(enteredPassword)) correct = true;
                    }
                    System.out.println("Login info correct: " + correct);
                });
    }

    public static void main(String[] args) throws Exception {
        new LoginWorker().execute(null);
    }
}
