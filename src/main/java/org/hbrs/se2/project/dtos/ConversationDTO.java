package org.hbrs.se2.project.dtos;

import java.io.Serializable;

public interface ConversationDTO extends Serializable {

    public int getConversationid();

    public void setConversationid(int conversationid);

    public String getTitle();

    public void setTitle(String title);

    public int getJobid();

    public void setJobid(int jobid);

    public int getStudentid();

    public void setStudentid(int studentid);

    public int getCompanyid();

    public void setCompanyid(int companyid);

}
