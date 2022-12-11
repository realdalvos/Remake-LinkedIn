package org.hbrs.se2.project.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="student", schema="mid9db")
public class Student {
    private int studentid;
    private int userid;
    private String firstname;
    private String lastname;
    private String matrikelnumber;
    private String university;
    private Set<Major> majors = new HashSet<>();
    private Set<Skill> skills = new HashSet<>();
    private Set<Topic> topics = new HashSet<>();


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="studentid")
    public int getStudentid() {return studentid;}
    public void setStudentid(int id) {this.studentid = id;}

    @Basic
    @Column(name="userid")
    public int getUserid() {return userid;}
    public void setUserid(int id) {this.userid = id;}

    @Basic
    @Column(name="firstname")
    public String getFirstname() {return firstname;}
    public void setFirstname(String name) {this.firstname = name;}

    @Basic
    @Column(name="lastname")
    public String getLastname() {return lastname;}
    public void setLastname(String name) {this.lastname = name;}

    @Basic
    @Column(name="matrikelnumber")
    public String getMatrikelnumber() {return matrikelnumber;}
    public void setMatrikelnumber(String number) {this.matrikelnumber = number;}

    @Basic
    @Column(name="university")
    public String getUniversity() {return university;}
    public void setUniversity(String uni) {this.university = uni;}

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name = "student_has_major",
            schema = "mid9db",
            joinColumns = @JoinColumn(name = "studentid"),
            inverseJoinColumns = @JoinColumn(name = "majorid"))
    public Set<Major> getMajors() {return majors;}

    public void setMajors(Set<Major> majors) {this.majors = majors;}

    public void addMajor(Major major){
        majors.add(major);
    }

    public void removeMajor(Major major){
        majors.remove(major);
    }

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name = "student_has_skill",
            schema = "mid9db",
            joinColumns = @JoinColumn(name = "studentid"),
            inverseJoinColumns = @JoinColumn(name = "skillid"))
    public Set<Skill> getSkills() {return skills;}

    public void setSkills(Set<Skill> skills) {this.skills = skills;}

    public void addSkill(Skill skill){
        skills.add(skill);
    }

    public void removeSkill(Skill skill){
        skills.remove(skill);
    }

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name = "student_has_topic",
            schema = "mid9db",
            joinColumns = @JoinColumn(name = "studentid"),
            inverseJoinColumns = @JoinColumn(name = "topicid"))
    public Set<Topic> getTopics() {return topics;}

    public void setTopics(Set<Topic> topics) {this.topics = topics;}

    public void addTopic(Topic topic){
        topics.add(topic);
    }

    public void removeTopic(Topic topic){
        topics.remove(topic);
    }

}

