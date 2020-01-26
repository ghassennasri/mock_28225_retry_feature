package com.marklogic.mock.processor;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.document.DocumentWriteOperation;
import com.marklogic.client.ext.batch.RestBatchWriter;
import com.marklogic.client.ext.batch.WriteListener;
import com.marklogic.client.ext.helper.LoggingObject;
import com.marklogic.client.impl.DocumentWriteOperationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
public class MarkLogicItemWriter extends LoggingObject implements ItemWriter<DocumentWriteOperation>, ItemStream {

    private static final Logger logger = LoggerFactory.getLogger(MarkLogicItemWriter.class);

    public RestBatchWriter getBatchWriter() {
        return batchWriter;
    }

    public void setBatchWriter(RestBatchWriter batchWriter) {
        this.batchWriter = batchWriter;
    }

    private RestBatchWriter batchWriter;
    private WriteFailureListener writeListner;
    @Autowired
    private DefaultListenerSupport defaultListenerSupport;



    private void initializeWriter(List<DatabaseClient> databaseClients){
        RestBatchWriter rbw = new RestBatchWriter(databaseClients);
        rbw.setReleaseDatabaseClients(false);
        this.batchWriter = rbw;
        writeListner=new WriteFailureListener();
        batchWriter.setWriteListener(writeListner);
    }



   /*The method write will retry 3 times on Exception with initial delay of 1000ms and exponential factor of 2
   The listener defaultListenerSupport will be entered to log retries
   */
    @Override
    @Retryable(include = Exception.class,maxAttempts = 3,backoff = @Backoff(multiplier = 2),listeners = "defaultListenerSupport") //initial delay for retry is 1000ms
    public void write(List<? extends DocumentWriteOperation> operations) throws Exception {

        if(operations == null || operations.isEmpty())
            return;

            List<DocumentWriteOperation> newItems = new ArrayList<>();
            for (DocumentWriteOperation op : operations) {
                newItems.add(
                        new DocumentWriteOperationImpl(
                                DocumentWriteOperation.OperationType.DOCUMENT_WRITE,
                                op.getUri(),
                                op.getMetadata(),
                                op.getContent()));
            }

        batchWriter.write(newItems);
        batchWriter.waitForCompletion();
        Throwable caughtError = writeListner.getErrorCaught();
        if(caughtError!=null)
        {
            throw new Exception(caughtError);
        }


    }
    @Recover
    public void recoverException(Exception e){
        logger.info("Message from recovery method");
    }
    public void setDatabaseClient(DatabaseClient databaseClient){
        initializeWriter(Arrays.asList(databaseClient));
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        batchWriter.initialize();
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
    }

    @Override
    public void close() throws ItemStreamException {
    }
}
