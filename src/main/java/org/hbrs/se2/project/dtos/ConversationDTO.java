package org.hbrs.se2.project.dtos;

import java.io.Serializable;

public interface ConversationDTO extends Serializable {

    public int getConversationid();

    public void setConversationid(int conversationid);

    public String getTitle();

    public void setTitle(String title);

    public Integer getJobid();

    public void setJobid(int jobid);

    public Integer getStudentid();

    public void setStudentid(Integer studentid);

    public Integer getCompanyid();

    public void setCompanyid(Integer companyid);

}
