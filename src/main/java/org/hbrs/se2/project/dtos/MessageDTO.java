package org.hbrs.se2.project.dtos;

import java.util.Date;

public interface MessageDTO {

    public int getMessageid();
    public void setMessageid(int messageid);

    public String getContent();

    public void setContent(String content);

    public Date getTimestamp();

    public void setTimestamp(Date timestamp);

    public int getConversationid();

    public void setConversationid(int conversationid);

}
