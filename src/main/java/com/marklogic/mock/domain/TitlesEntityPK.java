package com.marklogic.mock.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;
@Embeddable
public class TitlesEntityPK implements Serializable {

    private String title;
    private Date fromDate;



    @ManyToOne
    @JoinColumn(name = "emp_no", nullable = false)

    private EmployeesEntity employee;

    @Column(name = "title")

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        TitlesEntityPK that = (TitlesEntityPK) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(fromDate, that.fromDate) &&
                Objects.equals(employee, that.employee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, fromDate, employee);
    }
}
