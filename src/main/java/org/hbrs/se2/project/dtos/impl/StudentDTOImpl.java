package org.hbrs.se2.project.dtos.impl;

import org.hbrs.se2.project.dtos.StudentDTO;

public class StudentDTOImpl implements StudentDTO {
    private int studentid;
    private int userid;
    private String firstname;
    private String lastname;
    private int matrikelnumber;
    private String university;
    private String major;
    private String topic;
    private String skill;

    @Override
    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public void setStudentId(int id) {this.studentid = id;}

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setMatrikelnumber(int matrikelnumber) {
        this.matrikelnumber = matrikelnumber;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public void setMajor(String major) {
        this.major = major;
    }
    
    public void setTopic(String topic){ this.topic = topic;}

    @Override
    public int getStudentid() {return studentid;}
    @Override
    public int getUserid() {
        return userid;
    }

    @Override
    public String getFirstname() {
        return firstname;
    }

    @Override
    public String getLastname() {
        return lastname;
    }

    @Override
    public int getMatrikelnumber() {
        return matrikelnumber;
    }

    @Override
    public String getUniversity() {
        return university;
    }

    @Override
    public String getMajor() {
        return major;
    }

    @Override
    public String getTopic(){ return topic;}
}
