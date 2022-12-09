package org.hbrs.se2.project.dtos.impl;

import org.hbrs.se2.project.dtos.MessageDTO;

import java.util.Date;

public class MessageDTOImpl implements MessageDTO {

    private int messageid;
    private String content;
    private Date timestamp;
    private int conversationid;

    public int getMessageid() {
        return messageid;
    }

    public void setMessageid(int messageid) {
        this.messageid = messageid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getConversationid() {
        return conversationid;
    }

    public void setConversationid(int conversationid) {
        this.conversationid = conversationid;
    }

}
