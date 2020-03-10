package com.marklogic.mock;

import com.marklogic.mock.domain.EmployeesEntity;
import com.marklogic.mock.repositories.EmployeeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@EnableAutoConfiguration
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes ={ com.marklogic.mock.config.BatchConfig.class,com.marklogic.mock.Mock28225Application.class})

@RunWith(SpringRunner.class)

@DataJpaTest
public class EmployeeLoadTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Test
    public void whenFindAll_thenReturnProductList() {
        // when
        List<EmployeesEntity> employeesEntityList = (List<EmployeesEntity>) employeeRepository.findAll();

        // then
        assertThat(employeesEntityList).hasSize(1);
    }
}
