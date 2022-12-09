package org.hbrs.se2.project.entities;

import javax.persistence.*;

@Entity
@Table(name="conversation", schema="mid9db")
public class Conversation {

    private int conversationid;
    private String title;
    private int jobid;
    private int studentid;
    private int companyid;

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
    public int getJobid() {
        return jobid;
    }

    public void setJobid(int jobid) {
        this.jobid = jobid;
    }

    @Basic
    @Column(name="studentid")
    public int getStudentid() {
        return studentid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    @Basic
    @Column(name="companyid")
    public int getCompanyid() {
        return companyid;
    }

    public void setCompanyid(int companyid) {
        this.companyid = companyid;
    }

}
