package org.hbrs.se2.project.dtos.impl;

import org.hbrs.se2.project.dtos.RatingDTO;

public class RatingDTOImpl implements RatingDTO {

    private int ratingid;
    private int studentid;
    private int companyid;
    private int rating;

    public RatingDTOImpl(int studentid, int companyid, int rating){
        this.studentid = studentid;
        this.companyid = companyid;
        this.rating = rating;
    }

    public int getRatingid() {
        return ratingid;
    }

    public void setRatingid(int ratingid) {
        this.ratingid = ratingid;
    }

    public int getStudentid() {
        return studentid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    public int getCompanyid() {
        return companyid;
    }

    public void setCompanyid(int companyid) {
        this.companyid = companyid;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
