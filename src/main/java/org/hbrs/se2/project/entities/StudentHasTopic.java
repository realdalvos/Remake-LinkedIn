package org.hbrs.se2.project.entities;

import org.hbrs.se2.project.wrapper.StudentHasTopicKey;
import javax.persistence.*;

@Entity @IdClass(StudentHasTopicKey.class)
@Table(name = "student_has_topic", schema = "mid9db")
public class StudentHasTopic {
    private int studentid;
    private int topicid;

    @Id
    @Column(name = "studentid")
    public int getStudentid() {
        return studentid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    @Id
    @Column(name = "topicid")
    public int getTopicid() {
        return topicid;
    }

    public void setTopicid(int topicid) {
        this.topicid = topicid;
    }

}
