package org.hbrs.se2.project.entities;

import javax.persistence.*;
/**
 * This entity reflects the exact same database table "student"
 * so JPA Repository 'understands' how to save or read data from/to this table
 *
 * This is why you for instance pass a student entity 'student' to the method
 * repository.save(student)
 */
@Entity
@Table(name="student", schema="mid9db")
public class Student {
    private int studentid;
    private int userid;
    private String firstname;
    private String lastname;
    private String matrikelnumber;
    private String studyMajor;
    private String university;

    public Student() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="studentid")
    public int getStudentid() {return studentid;}
    public void setStudentid(int id) {this.studentid = id;}
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
    @Column(name="studymajor")
    public String getStudyMajor() {return studyMajor;}
    public void setStudyMajor(String major) {this.studyMajor = major;}

    @Basic
    @Column(name="university")
    public String getUniversity() {return university;}
    public void setUniversity(String uni) {this.university = uni;}
}
