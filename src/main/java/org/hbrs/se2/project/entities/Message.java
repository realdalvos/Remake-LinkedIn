package org.hbrs.se2.project.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="message", schema="mid9db")
public class Message {

    private int messageid;
    private String content;
    private Date timestamp;
    private int conversationid;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="messageid")
    public int getMessageid() {
        return messageid;
    }

    public void setMessageid(int messageid) {
        this.messageid = messageid;
    }

    @Basic
    @Column(name="content", columnDefinition="TEXT")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name="timestamp")
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Basic
    @Column(name="conversationid")
    public int getConversationid() {
        return conversationid;
    }

    public void setConversationid(int conversationid) {
        this.conversationid = conversationid;
    }

}
