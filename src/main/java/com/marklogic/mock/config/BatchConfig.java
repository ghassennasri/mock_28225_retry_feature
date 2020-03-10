package com.marklogic.mock.config;


import com.marklogic.client.DatabaseClient;
import com.marklogic.client.document.DocumentWriteOperation;
import com.marklogic.client.ext.helper.DatabaseClientProvider;
import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.StringHandle;
import com.marklogic.client.io.marker.AbstractWriteHandle;
import com.marklogic.client.io.marker.DocumentMetadataWriteHandle;
import com.marklogic.mock.PostProcessor.EmployeeJobListener;
import com.marklogic.mock.domain.EmployeesEntity;
import com.marklogic.mock.model.Department;
import com.marklogic.mock.model.Employee;
import com.marklogic.mock.model.Salary;
import com.marklogic.mock.model.Title;
import com.marklogic.mock.processor.MarkLogicItemWriter;
import com.marklogic.spring.batch.item.processor.MarkLogicItemProcessor;
import org.hibernate.SessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.HibernateCursorItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.HibernateCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@PropertySource(value = "file:src/main/resources/application.properties")
@Configuration
@EnableBatchProcessing
@Import(value = {
        com.marklogic.spring.batch.config.MarkLogicConfiguration.class
})
public class BatchConfig {
    private final String JOB_NAME = "GNA_JOB";

    private static final String PROPERTY_XML_EXPORT_FILE_PATH = "database.to.xml.job.export.file.path";

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    public JobBuilderFactory getJobBuilderFactory() {
        return jobBuilderFactory;
    }

    public StepBuilderFactory getStepBuilderFactory() {
        return stepBuilderFactory;
    }



    public DatabaseClientProvider getDatabaseClientProvider() {
        return databaseClientProvider;
    }

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;
    @Autowired
    private MarkLogicItemWriter markLogicItemWriter;

    @Autowired
    public DatabaseClientProvider databaseClientProvider;
    @Autowired
    EntityManagerFactory entityManagerFactory;



	public HibernateCursorItemReader<EmployeesEntity> customerItemReader(
			EntityManagerFactory entityManagerFactory) {
		return new HibernateCursorItemReaderBuilder<EmployeesEntity>()
				.name("employeeItemReader")
				.sessionFactory(entityManagerFactory.unwrap(SessionFactory.class))
				.queryString("from EmployeesEntity")
				.build();
	}
    public JdbcCursorItemReader<Employee> getReader() {
        JdbcCursorItemReader<Employee> cursorItemReader = new JdbcCursorItemReader<>();
        cursorItemReader.setDataSource(dataSource);
        cursorItemReader.setSql("SELECT emp_no,first_name,last_name,gender,birth_date,hire_date FROM employees limit 2000");
        cursorItemReader.setRowMapper(new EmployeeRowMapper());
        return cursorItemReader;
    }
    @Bean
    EmployeeJobListener employeeJobListener(MarkLogicItemWriter markLogicItemWriter){
        return new EmployeeJobListener(markLogicItemWriter);
    }

    @Bean
    @JobScope
    public Step step(
            StepBuilderFactory stepBuilderFactory,
            DatabaseClientProvider databaseClientProvider,
            @Value("#{jobParameters['output_collections'] ?: 'GNACOLLECTION6'}") String[] collections,
            @Value("#{jobParameters['chunk_size'] ?: 1000}") int chunkSize) {

        DatabaseClient databaseClient = databaseClientProvider.getDatabaseClient();

        //JdbcCursorItemReader<Employee> reader = getReader();
        HibernateCursorItemReader<EmployeesEntity> reader=customerItemReader(entityManagerFactory);


        MarkLogicItemProcessor<EmployeesEntity> processor = item -> {
            DocumentWriteOperation dwo = new DocumentWriteOperation() {

                @Override
                public OperationType getOperationType() {
                    return OperationType.DOCUMENT_WRITE;
                }

                @Override
                public String getUri() {
                    return UUID.randomUUID().toString() + ".xml";
                }

                @Override
                public DocumentMetadataWriteHandle getMetadata() {
                    DocumentMetadataHandle metadata = new DocumentMetadataHandle();
                    metadata.withCollections(collections);
                    return metadata;
                }

                @Override
                public AbstractWriteHandle getContent() {
                    JAXBContext jaxbContext = null;
                    String xmlContent = "";
                    try {
                        jaxbContext = JAXBContext.newInstance(Employee.class);
                        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
                        Employee e=mapEmployee(item);
                        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                        StringWriter sw = new StringWriter();
                        jaxbMarshaller.marshal(e, sw);
                        xmlContent = sw.toString();
                    } catch (JAXBException e) {
                        e.printStackTrace();
                    }

                    return new StringHandle(xmlContent);
                }

                @Override
                public String getTemporalDocumentURI() {
                    return null;
                }
            };
            return dwo;
        };

        markLogicItemWriter.setDatabaseClient(databaseClient);


        return stepBuilderFactory.get("step1")
                .<EmployeesEntity, DocumentWriteOperation>chunk(chunkSize)
                .reader(reader)
                .processor(processor)
                .writer(markLogicItemWriter)

                //.faultTolerant().retry(Exception.class).retryLimit(3)
                .build();
    }


    @Bean(name = JOB_NAME)
    public Job job(JobBuilderFactory jobBuilderFactory, Step step) {

        return jobBuilderFactory.get(JOB_NAME)
                .start(step)
                .incrementer(new RunIdIncrementer())
                .listener(employeeJobListener(markLogicItemWriter))
                .build();
    }
    private Employee mapEmployee(EmployeesEntity employeesEntity){
        Employee e=new Employee();
        e.setEmpNo(employeesEntity.getEmpNo());
        e.setBirthDate(employeesEntity.getBirthDate());
        e.setFirstName(employeesEntity.getFirstName());
        e.setLastName(employeesEntity.getLastName());
        e.setGender(employeesEntity.getGender());

        List<Department> departmentList=employeesEntity.getDepts().stream().map(x->{
            Department department=new Department();
            department.setDepartmentName(x.getDeptEmpEntityPK().getDepartment().getDeptName());
            department.setDepartmentNumber(x.getDeptEmpEntityPK().getDepartment().getDeptNo());
            department.setStartDate(x.getFromDate());
            department.setEndDate(x.getToDate());
            return department;
        }).collect(Collectors.toList());
        List<Title> titleList=employeesEntity.getTitles().stream().map(x->{
            Title title=new Title();
            title.setTitle(x.getTitlesEntityPK().getTitle());
            title.setStartDate(x.getTitlesEntityPK().getFromDate());
            title.setEndDate(x.getToDate());
            return title;
        }).collect(Collectors.toList());
        List<Salary> salaryList=employeesEntity.getSalaries().stream().map(x->{
                Salary salary=new Salary();
                salary.setSalary(x.getSalary());
                salary.setStartDate(x.getSalariesEntityPK().getFromDate());
                salary.setEndDate(x.getToDate());
                return salary;
        }).collect(Collectors.toList());
        e.setDepartments(departmentList);
        e.setSalaries(salaryList);
        e.setTitles(titleList);
        return e;
    }
}

