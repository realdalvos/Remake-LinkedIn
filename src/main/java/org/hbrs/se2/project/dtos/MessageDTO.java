package org.hbrs.se2.project.dtos;

import java.io.Serializable;
import java.time.Instant;

public interface MessageDTO extends Serializable {

    public int getMessageid();

    public void setMessageid(int messageid);

    public String getContent();

    public void setContent(String content);

    public Instant getTimestamp();

    public void setTimestamp(Instant timestamp);

    public int getConversationid();

    public void setConversationid(int conversationid);

    public boolean getRead();

    public void setRead(boolean read);

    public int getUserid();

    public void setUserid(int userid);

}
