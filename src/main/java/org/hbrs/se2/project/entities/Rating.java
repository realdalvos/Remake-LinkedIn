package org.hbrs.se2.project.entities;

import javax.persistence.*;

@Entity
@Table(name ="rating", schema="mid9db")
public class Rating {

    private int ratingid;
    private int studentid;
    private int companyid;
    private int value;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ratingid")
    public int getRatingid(){
        return  ratingid;
    }

    public void setRatingid(int ratingid){
        this.ratingid = ratingid;
    }

    @Basic
    @Column(name = "studentid")
    public int getStudentid(){
        return studentid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    @Basic
    @Column(name = "companyid")
    public int getCompanyid(){
        return companyid;
    }

    public void setCompanyid(int companyid){
        this.companyid = companyid;
    }

    @Basic
    @Column(name = "rating")
    public int getRating(){
        return value;
    }

    public void setRating(int rating) {
        this.value = rating;
    }

}
