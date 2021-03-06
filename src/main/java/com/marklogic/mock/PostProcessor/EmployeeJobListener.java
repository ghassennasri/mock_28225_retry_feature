package com.marklogic.mock.PostProcessor;

import com.marklogic.client.DatabaseClient;
import com.marklogic.mock.processor.MarkLogicItemWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import java.util.Date;
import java.util.List;

public class EmployeeJobListener implements JobExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeJobListener.class);

    protected DatabaseClient client;

    private MarkLogicItemWriter markLogicItemWriter;
   public EmployeeJobListener(MarkLogicItemWriter markLogicItemWriter){
       this.markLogicItemWriter=markLogicItemWriter;
   }

    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {


        if(jobExecution.getStatus() == BatchStatus.COMPLETED){
            LOGGER.info("Employee job terminated");



        }else if(jobExecution.getStatus() == BatchStatus.FAILED){
            LOGGER.info("Employee job failed");
            List<Throwable> exceptionList = jobExecution.getAllFailureExceptions();
            for(Throwable th : exceptionList){
                LOGGER.info("exception :" +th.getLocalizedMessage());
            }
        }
        if(markLogicItemWriter!=null){
            markLogicItemWriter.getBatchWriter().waitForCompletion();
        }
    }

    private long getTimeInMillis(Date start, Date stop){
        return stop.getTime()- start.getTime();
    }

}
