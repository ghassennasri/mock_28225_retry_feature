package com.marklogic.mock.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;
@Embeddable
public class SalariesEntityPK implements Serializable {

    private Date fromDate;

    @ManyToOne
    @JoinColumn(name = "emp_no", nullable = false)

    private EmployeesEntity employee;

    public EmployeesEntity getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeesEntity employee) {
        this.employee = employee;
    }

    @Column(name = "from_date")

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SalariesEntityPK that = (SalariesEntityPK) o;
        return Objects.equals(that.employee,employee) &&
                Objects.equals(fromDate, that.fromDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employee, fromDate);
    }
}
