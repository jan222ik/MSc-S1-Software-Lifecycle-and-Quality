package at.fhv.bpmn;

import org.apache.catalina.webresources.TomcatURLStreamHandlerFactory;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableProcessApplication
public class Application {

    public Application(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    public static void main(String[] args) {
        TomcatURLStreamHandlerFactory.disable();
        SpringApplication.run(Application.class, args);
    }

    protected final ProcessEngine processEngine;

    @PostConstruct
    public void deployInvoice() {
        ClassLoader classLoader = Application.class.getClassLoader();

        if (processEngine.getIdentityService().createUserQuery().list().isEmpty()) {
            processEngine.getRepositoryService()
                    .createDeployment()
                    .addInputStream("src/main/quarantine/libraryV2.bpmn", classLoader.getResourceAsStream("src/main/quarantine/libraryV2.bpmn"))
                    .deploy();
        }
    }

}