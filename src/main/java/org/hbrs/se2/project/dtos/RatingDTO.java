package org.hbrs.se2.project.dtos;

import java.io.Serializable;

public interface RatingDTO extends Serializable {

    int getRatingid();
    void setRatingid(int ratingid);
    int getStudentid();
    void setStudentid(int studentid);
    int getCompanyid();
    void setCompanyid(int companyid);
    int getRating();
    void setRating(int rating);

}
