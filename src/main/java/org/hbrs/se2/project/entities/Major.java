package org.hbrs.se2.project.entities;

import javax.persistence.*;

@Entity
@Table(name= "major", schema = "mid9db")
public class Major {

    private int majorid;

    private String major;

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
}
