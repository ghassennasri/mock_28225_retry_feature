package com.marklogic.mock.domain;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "dept_manager", schema = "employees", catalog = "")

public class DeptManagerEntity {

    private Date fromDate;
    private Date toDate;
    @EmbeddedId
    private DeptManagerEntityPK deptManagerEntityPK;

    @Basic
    @Column(name = "from_date",insertable = false, updatable = false)
    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    @Basic
    @Column(name = "to_date",insertable = false, updatable = false)
    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }


}
