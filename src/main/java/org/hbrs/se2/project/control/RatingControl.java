package org.hbrs.se2.project.control;

import org.hbrs.se2.project.dtos.RatingDTO;
import org.hbrs.se2.project.services.impl.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class RatingControl {

    @Autowired
    private RatingService ratingService;

    /**
     * Get the average rating of a company
     * @param companyid Company ID
     * @return Float value of the average rating of the company
     */
    public Float getRating(int companyid){
        return ratingService.getRating(companyid);
    }

    /**
     * Save a new rating to the Database
     * @param rating RatingDTO to be persisted
     */
    public void createRating(RatingDTO rating){
        ratingService.createRating(rating);
    }

    /**
     * Checks if a student has already rated a company
     * @param companyid Company ID
     * @param studentid Student ID
     * @return Boolean indicating if the student has already rated the company
     */
    public boolean studentHasRatedCompany(int companyid, int studentid){
        return ratingService.studentHasRatedCompany(companyid, studentid);
    }

}
