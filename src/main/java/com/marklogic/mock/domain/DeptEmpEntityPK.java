package com.marklogic.mock.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DeptEmpEntityPK implements Serializable {
    public DepartmentsEntity getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentsEntity departmentName) {
        this.department = departmentName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeptEmpEntityPK that = (DeptEmpEntityPK) o;
        return Objects.equals(department, that.department) &&
                Objects.equals(employee, that.employee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(department, employee);
    }

    @ManyToOne
    @JoinColumn(name = "dept_no", insertable = false, updatable = false)
    private DepartmentsEntity department;

    @ManyToOne
    @JoinColumn(name = "emp_no", nullable = false)

    private EmployeesEntity employee;



}
