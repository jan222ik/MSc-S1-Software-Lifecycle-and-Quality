package at.fhv.bpmn.acceptance;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;

import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.variable.value.FileValue;
import org.camunda.bpm.engine.variable.Variables;

import java.util.List;


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
    public void libProcessUntilBrowsing(){
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

        ActivityInstance activityInstance = runtimeService.getActivityInstance(process.getProcessInstanceId());
        for (ActivityInstance childActivityInstance : activityInstance.getChildActivityInstances()) {
            System.out.println("childActivityInstance = " + childActivityInstance);
        }

        ProcessInstance pi = process;
        TaskService taskService = processEngine.getTaskService();
        List<Task> subProcessTasks = taskService.createTaskQuery().processInstanceId(pi.getId()).orderByTaskName().asc().list();
        subProcessTasks.forEach((task) -> {
            System.out.println("task = " + task);
        });
    }

    @Test
    public void  browseProcess() {
        ProcessInstance process = this.runtimeService.startProcessInstanceByKey("browse_process");
        assertThat(process)
                .isNotNull()
                .isStarted()
                .hasPassed("Activity_Book_Selection");

    }

    private void addBook(String name, String a) {

    }

}
