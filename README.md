# mock_28225_retry_feature

This is a mock of case 28225, the projects is done using spring boot, spring batch and the community library marklogic-spring-batch-core:1.7.0 
Tha application connect to the well known Mysql sample database Employees, reads employees table and serialize each tuple as an xml document into Marklogic database Documents.
The job implemented is single step with a chunk oriented tasklet with commit-interval set at 100. The objective here is to demonstrate the spring retry feature enabled (both at step level and xriter level)

To activate retry at step level;
* Uncomment //.faultTolerant().retry(Exception.class).retryLimit(3) in method BatchConfig.step()
* Comment @Retryable(include = Exception.class,maxAttempts = 3,backoff = @Backoff(multiplier = 2),listeners = "defaultListenerSupport") 
annotation in MarklogicItemWriter.write(...)

To activate retry at writer level;
* comment //.faultTolerant().retry(Exception.class).retryLimit(3) in method BatchConfig.step()
* Uncomment @Retryable(include = Exception.class,maxAttempts = 3,backoff = @Backoff(multiplier = 2),listeners = 
annotation in MarklogicItemWriter.write(...)


prequisites:
*Both Mysql and Marklogic 9/10 need to be running on a host (to be specified in application.properties spring boot file) The docker-compose file used to test the project is included into this repository

*Mysql employees sample database need to be imported ( guidelines on https://dev.mysql.com/doc/employee/en/employees-installation.html)

*Set up a Marklogic HTTP server on port 8011 or whatever(to be specified in application.properties spring boot file)

Descriptiopn:

The job consists of a step where the reader is a JdbcCursorItemReader that reads from Mysql database, a processor MarkLogicItemProcessor
that will convert Employee object to DocumentWriteOperation object, a writer MarkLogicItemWriter which uses RestBatchWriter to write chunks
of DocumentWriteOperation.

To simulate an Exception thrown from whithin Marklogic RestWriter, I tried to upload the documents with a user not having the authorization
for uploads.
The retries are logged the log file mock28255-logger.log where the retries are logged through the retry listener DefaultListenerSupport.


