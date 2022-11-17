package org.hbrs.se2.project.entities;

import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Table(name = "topic", schema = "mid9db")
public class Topic {

    private int topicid;
    private String topic;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topicid")
    public int getTopicid() {
        return topicid;
    }

    public void setTopicid(int topicid) {
        this.topicid = topicid;
    }

    @Basic
    @Column(name = "topic")
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

}
