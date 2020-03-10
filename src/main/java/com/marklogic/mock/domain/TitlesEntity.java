package com.marklogic.mock.domain;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "titles", schema = "employees")

public class TitlesEntity {



    private Date toDate;

    public TitlesEntityPK getTitlesEntityPK() {
        return titlesEntityPK;
    }

    public void setTitlesEntityPK(TitlesEntityPK titlesEntityPK) {
        this.titlesEntityPK = titlesEntityPK;
    }

    @EmbeddedId
    TitlesEntityPK titlesEntityPK;




    @Column(name = "to_date")
    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }


}
