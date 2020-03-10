package com.marklogic.mock.domain;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import java.sql.Date;
import java.util.Set;

@Entity
@Subselect(
        "select dept.dept_name,dept.dept_no,emp_no,from_date,to_date " +
                "from departments as dept join dept_emp  " +
                "on dept_emp.dept_no=dept.dept_no")
@Synchronize({"dept_emp"})
@Immutable
public class DeptEmpEntity {


    @EmbeddedId
    private DeptEmpEntityPK deptEmpEntityPK;


    private Date fromDate;
    private Date toDate;


    @Basic
    @Column(name = "from_date")
    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    @Basic
    @Column(name = "to_date")
    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }


    public DeptEmpEntityPK getDeptEmpEntityPK() {
        return deptEmpEntityPK;
    }

    public void setDeptEmpEntityPK(DeptEmpEntityPK deptEmpEntityPK) {
        this.deptEmpEntityPK = deptEmpEntityPK;
    }

}

