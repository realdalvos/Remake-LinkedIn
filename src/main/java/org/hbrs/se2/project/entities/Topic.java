package org.hbrs.se2.project.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "topic", schema = "mid9db")
public class Topic {
    private int topicid;
    private String value;
    private Set<Student> students = new HashSet<>();

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
        return value;
    }

    public void setTopic(String topic) {
        this.value = topic;
    }

    @ManyToMany(mappedBy = "topics")
    public Set<Student> getStudents() {return students;}

    public void setStudents(Set<Student> students) {this.students = students;}

}
