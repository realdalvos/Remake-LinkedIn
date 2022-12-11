package org.hbrs.se2.project.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name= "major", schema = "mid9db")
public class Major {
    private int majorid;
    private String major;
    private Set<Student> students = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "majorid")
    public int getMajorid() {
        return majorid;
    }

    public void setMajorid(int majorid) {
        this.majorid = majorid;
    }

    @Basic
    @Column(name = "major")
    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    @ManyToMany(mappedBy = "majors")
    public Set<Student> getStudents() {return students;}

    public void setStudents(Set<Student> students) {this.students = students;}

}
