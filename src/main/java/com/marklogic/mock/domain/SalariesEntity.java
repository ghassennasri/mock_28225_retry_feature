package com.marklogic.mock.domain;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "salaries", schema = "employees", catalog = "")

public class SalariesEntity {

    private int salary;

    private Date toDate;

    public SalariesEntityPK getSalariesEntityPK() {
        return salariesEntityPK;
    }

    public void setSalariesEntityPK(SalariesEntityPK salariesEntityPK) {
        this.salariesEntityPK = salariesEntityPK;
    }

    @EmbeddedId
    SalariesEntityPK salariesEntityPK;


    @Column(name = "salary")
    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }


    @Column(name = "to_date")
    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }



}
