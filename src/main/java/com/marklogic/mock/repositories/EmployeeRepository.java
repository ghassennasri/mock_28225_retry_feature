package com.marklogic.mock.repositories;

import com.marklogic.mock.domain.DeptManagerEntity;
import com.marklogic.mock.domain.EmployeesEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<EmployeesEntity, Integer> {

}
