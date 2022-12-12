package org.hbrs.se2.project.dtos.impl;

import org.hbrs.se2.project.dtos.ConversationDTO;

public class ConversationDTOImpl implements ConversationDTO {

    private int conversationid;
    private String title;
    private Integer jobid;
    private Integer studentid;
    private Integer companyid;

    public int getConversationid() {
        return conversationid;
    }

    public void setConversationid(int conversationid) {
        this.conversationid = conversationid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getJobid() {
        return jobid;
    }

    public void setJobid(int jobid) {
        this.jobid = jobid;
    }

    public Integer getStudentid() {
        return studentid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    public Integer getCompanyid() {
        return companyid;
    }

    public void setCompanyid(int companyid) {
        this.companyid = companyid;
    }

}
