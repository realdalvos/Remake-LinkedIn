package org.hbrs.se2.project.dtos.impl;

import org.hbrs.se2.project.dtos.StudentDTO;
import org.springframework.stereotype.Component;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;

@Component
public class StudentDTOImpl implements StudentDTO {
    private int studentid;
    private int userid;
    @NotEmpty(message = "Darf nicht leer sein")
    private String firstname;
    @NotEmpty(message = "Darf nicht leer sein")
    private String lastname;
    @Digits(integer = 7, fraction = 7, message = "Geben Sie bitte eine gültige Matrikelnummer ein")
    @NotEmpty
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

    public StudentDTOImpl(int userid, int studentid, String firstname, String lastname, String matrikelnumber, String university){
        this.userid=userid;
        this.studentid = studentid;
        this.firstname=firstname;
        this.lastname=lastname;
        this.matrikelnumber=matrikelnumber;
        this.university=university;
    }

    public void setStudentId(int id) {this.studentid = id;}

    public void setUserid(int userid) { this.userid = userid; }

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

    public int getStudentid() {return studentid;}

    public int getUserid() { return userid; }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getMatrikelnumber() {
        return matrikelnumber;
    }

    public String getUniversity() {
        return university;
    }
}
