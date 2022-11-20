package org.hbrs.se2.project.dtos.impl;

import org.hbrs.se2.project.dtos.StudentHasTopicDTO;

public class StudentHasTopicDTOImpl implements StudentHasTopicDTO {
    private int studentid;
    private int topicid;

    public int getStudentid() {
        return studentid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    public int getTopicid() {
        return topicid;
    }

    public void setTopicid(int topicid) {
        this.topicid = topicid;
    }

}
