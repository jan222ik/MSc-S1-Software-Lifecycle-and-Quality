package at.fhv.bpmn.acceptance;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;


@SuppressWarnings("WeakerAccess")
@SpringBootTest(
        properties = {
                "camunda.bpm.generate-unique-process-engine-name=true",
                "camunda.bpm.generate-unique-process-application-name=true",
                "spring.datasource.generate-unique-name=true",
        }
)

public class BrowseAcceptanceTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    ProcessEngine processEngine;

    @Autowired
    RuntimeService runtimeService;

    @BeforeEach
    public void setUp() {
        init(this.processEngine);
    }


    /**
     * Run a process including the subprocess to make sure the intregration works
     */
    @Test
    @Deployment
    public void libProcessUntilBrowsing() {
        ProcessInstance process = this.runtimeService.startProcessInstanceByKey("process_lib");
        assertThat(process)
                .isNotNull()
                .isStarted();
        // Login
        complete(task(process),
                withVariables(
                        "username", "admin",
                        "password", "admin"
                )
        );
        assertThat(process)
                .hasPassed("Activity_Check_Login_Data")
                .hasPassed("Gateway_Correct_Password")
                .hasPassed("Activity_Check_For_Due_Books")
                .hasVariables("overdueresult")
                .hasPassed("Activity_Due_Info")
                .hasPassed("Gateway_May_Lend_More_Books")
                .hasNotPassed("Activity_Subprocess_Browse_Books");

    }


    @Test
    @Deployment
    public void fullRun() {
        ProcessInstance process = this.runtimeService.startProcessInstanceByKey("process_lib");
        assertThat(process)
                .isNotNull()
                .isStarted();
        // Login
        complete(task(process),
                withVariables(
                        "username", "admin",
                        "password", "admin"
                )
        );
        assertThat(process)
                .hasPassed("Activity_Check_Login_Data")
                .hasPassed("Gateway_Correct_Password")
                .hasPassed("Activity_Check_For_Due_Books")
                .hasVariables("overdueresult")
                .hasPassed("Activity_Due_Info")
                .hasPassed("Gateway_May_Lend_More_Books")
                .hasNotPassed("Activity_Subprocess_Browse_Books");

        // Run SubProcess
        ProcessInstance subProcess = processEngine
                .getRuntimeService()
                .createProcessInstanceQuery()
                .superProcessInstanceId(process.getId())
                .singleResult();

        Map<String, Object> variables = withVariables(
                "book_name", "Harry Potter",
                "book_amount", 13L
        );
        complete(task(subProcess), variables);

        assertThat(subProcess)
                .hasPassed("Activity_Check_Status")
                .hasPassed("Gateway_hasEnough");

        complete(task(subProcess), withVariables("selection_done", true));

        assertThat(subProcess)
                .hasPassed("Event_Browsing_End")
                .isEnded();

        // Check End
        assertThat(process)
                .hasVariables("user_books")
                .hasPassed("Activity_Amount_Calculation")
                .isEnded();
    }

    @Test
    public void browseProcess() {
        ProcessInstance process = this.runtimeService.startProcessInstanceByKey("browse_process");
        Map<String, Object> variables = withVariables(
                "book_name", "Harry Potter",
                "book_amount", 13L
        );
        complete(task(process), variables);

        assertThat(process)
                .hasPassed("Activity_Check_Status")
                .hasPassed("Gateway_hasEnough");

        complete(task(process), withVariables("selection_done", true));

        assertThat(process)
                .hasPassed("Event_Browsing_End")
                .isEnded();

    }

}
