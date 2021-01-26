package at.fhv.bpmn.bdd;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;


@RunWith(SpringRunner.class)
@SpringBootTest(
        properties = {
                "camunda.bpm.generate-unique-process-engine-name=true",
                "camunda.bpm.generate-unique-process-application-name=true",
                "spring.datasource.generate-unique-name=true",
        }
)
@CucumberContextConfiguration
@ContextConfiguration(classes = TestConfiguration.class)
public class LoginProcessStepDefs {

    private static final String activityID = "process_lib";

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    ProcessEngine processEngine;

    @Autowired
    RuntimeService runtimeService;
    ProcessInstance instance;

    private String password;
    private String username;


    @Before
    public void setUp() {
        init(this.processEngine);
    }

    @After
    public void tearDown() {
        System.out.println("Stopping a Login scenario");
    }

    @Given("A Task for Login Exists")
    public void startLoginTask() {
        this.instance = this.runtimeService.startProcessInstanceByKey(activityID);
        assertThat(this.instance).isNotNull();
        assertThat(this.instance).isStarted();
    }

    @When("I enter the username {string}")
    public void setUsername(String username) {
        this.username = username;
    }

    @And("I enter the password {string}")
    public void iEnterThePassword(String password) {
        this.password = password;
    }

    @Then("I have finished the task")
    public void iHaveFinishedTheTask() {
        completeTask(true);
    }

    @Then("I have to do task again")
    public void iHaveToDoTaskAgain() {
        completeTask(false);
    }

    private void completeTask(boolean expected) {
        complete(task(this.instance),
                withVariables(
                        "username", username,
                        "password", password
                )
        );
        if (expected) {
            assertThat(this.instance).hasPassed("Event_07t6c2k");
        } else {
            assertThat(this.instance).hasNotPassed("Event_07t6c2k");
        }
    }
}
