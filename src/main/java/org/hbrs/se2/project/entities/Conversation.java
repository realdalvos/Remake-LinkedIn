package org.hbrs.se2.project.entities;

import javax.persistence.*;

@Entity
@Table(name="conversation", schema="mid9db")
public class Conversation {

    private int conversationid;
    private String title;
    private Integer jobid;
    private Integer studentid;
    private Integer companyid;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="conversationid")
    public int getConversationid() {
        return conversationid;
    }

    public void setConversationid(int conversationid) {
        this.conversationid = conversationid;
    }

    @Basic
    @Column(name="title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name="jobid")
    public Integer getJobid() {
        return jobid;
    }

    public void setJobid(Integer jobid) {
        this.jobid = jobid;
    }

    @Basic
    @Column(name="studentid")
    public Integer getStudentid() {
        return studentid;
    }

    public void setStudentid(Integer studentid) {
        this.studentid = studentid;
    }

    @Basic
    @Column(name="companyid")
    public Integer getCompanyid() {
        return companyid;
    }

    public void setCompanyid(Integer companyid) {
        this.companyid = companyid;
    }

}
