package org.hbrs.se2.project.dtos.impl;

import org.hbrs.se2.project.dtos.TopicDTO;

public class TopicDTOImpl implements TopicDTO {
    private int topicid;
    private String topic;

    public int getTopicid() {
        return topicid;
    }

    public void setTopicid(int topicid) {
        this.topicid = topicid;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

}
