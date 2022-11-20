package org.hbrs.se2.project.wrapper;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public class StudentHasTopicKey implements Serializable {
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
