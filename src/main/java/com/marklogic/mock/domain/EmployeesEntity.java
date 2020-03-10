package com.marklogic.mock.domain;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "employees", schema = "employees", catalog = "")
public class EmployeesEntity {
    private int empNo;
    private Date birthDate;
    private String firstName;
    private String lastName;
    private String gender;
    private Date hireDate;



    private Set<DeptEmpEntity> depts;



    private Set<SalariesEntity> salaries;


    private Set<TitlesEntity> titles;
    @Id
    @Column(name = "emp_no")
    public int getEmpNo() {
        return empNo;
    }

    public void setEmpNo(int empNo) {
        this.empNo = empNo;
    }


    @Column(name = "birth_date")
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }


    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    @Column(name = "gender")
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    @Column(name = "hire_date")
    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }
    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name = "emp_no")
    public Set<DeptEmpEntity> getDepts() {
        return depts;
    }

    public void setDepts(Set<DeptEmpEntity> depts) {
        this.depts = depts;
    }

    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name = "emp_no")
    public Set<SalariesEntity> getSalaries() {
        return salaries;
    }

    public void setSalaries(Set<SalariesEntity> salaries) {
        this.salaries = salaries;
    }

    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name = "emp_no")
    public Set<TitlesEntity> getTitles() {
        return titles;
    }

    public void setTitles(Set<TitlesEntity> titles) {
        this.titles = titles;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeesEntity that = (EmployeesEntity) o;
        return empNo == that.empNo &&
                Objects.equals(birthDate, that.birthDate) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(gender, that.gender) &&
                Objects.equals(hireDate, that.hireDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empNo, birthDate, firstName, lastName, gender, hireDate);
    }
}
