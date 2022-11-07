package org.hbrs.se2.project.dtos.impl;

import org.hbrs.se2.project.dtos.StudentDTO;

public class StudentDTOImpl implements StudentDTO {
    private int studentid;
    private int userid;
    private String firstname;
    private String lastname;
    private String matrikelnumber;
    private String university;

    public StudentDTOImpl(){}
    public StudentDTOImpl(int userid, String firstname, String lastname, String matrikelnumber, String university){
        this.userid=userid;
        this.firstname=firstname;
        this.lastname=lastname;
        this.matrikelnumber=matrikelnumber;
        this.university=university;
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

    public void setMatrikelnumber(String matrikelnumber) {
        this.matrikelnumber = matrikelnumber;
    }


    public void setUniversity(String university) {
        this.university = university;
    }

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
    public String getMatrikelnumber() {
        return matrikelnumber;
    }

    @Override
    public String getUniversity() {
        return university;
    }
}
