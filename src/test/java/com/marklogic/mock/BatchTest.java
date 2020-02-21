package com.marklogic.mock;

import com.marklogic.mock.config.BatchConfig;
import org.jboss.byteman.contrib.bmunit.BMRule;
import org.jboss.byteman.contrib.bmunit.BMRules;
import org.jboss.byteman.contrib.bmunit.BMUnitConfig;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

@EnableAutoConfiguration
@SpringBatchTest
@RunWith(SpringRunner.class)
@BMUnitConfig(debug = true,verbose = true)
@ContextConfiguration(classes ={ com.marklogic.mock.config.BatchConfig.class,com.marklogic.mock.Mock28225Application.class})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class BatchTest {
    @Rule
    public BytemanRule byteman = BytemanRule.create( BatchTest.class );
    @Autowired
    private JobLauncher jobLauncher;


    @Autowired
    private BatchConfig config;

    @Test
    @BMRules(
            rules = {
                    @BMRule(
                            name="Throw socket exception",
                            targetClass = "com.sun.org.apache.xml.internal.serialize.Printer",
                            targetMethod = "printText",
                            isOverriding = true,

                            targetLocation ="AT INVOKE java.io.write(char)",
                            action = "throw new java.net.SocketException()"
                    ),
                   /* @BMRule(
                            name="Throw socket exception 2",
                            targetClass = "com.marklogic.client.impl.OkHttpServices",
                            targetMethod = "postResource",
                            isOverriding = true,

                            targetLocation ="AT ENTRY",
                            action = "throw new RuntimeException()"
                    )*/
            }
    )
    public void startBatch() throws Exception {
        Step step=config.step(config.getStepBuilderFactory(),config.databaseClientProvider,new String[]{"GNA_COL"},100);
        Job job=config.job(config.getJobBuilderFactory(),step);
        JobParameters jobParameters =
                new JobParametersBuilder()
                        .addLong("time",System.currentTimeMillis()).toJobParameters();
        jobLauncher.run(job, jobParameters);
    }
}
