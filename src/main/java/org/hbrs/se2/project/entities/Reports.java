package org.hbrs.se2.project.entities;

import javax.persistence.*;

@Entity
@Table(name ="reports", schema="mid9db")
public class Reports {
    private int reportid;
    private int companyid;
    private String text;
    private int studentid;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reportid")
    public int getReportid(){
        return  reportid;
    }

    public void setReportid(int reportid){
        this.reportid = reportid;
    }

    @Basic
    @Column(name = "companyid")
    public int getCompanyid(){
        return companyid;
    }

    public void setCompanyid(int companyid){
        this.companyid = companyid;
    }

    @Basic
    @Column(name = "text")
    public String getText(){
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Basic
    @Column(name = "studentid")
    public int getStudentid(){
        return studentid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }
}
