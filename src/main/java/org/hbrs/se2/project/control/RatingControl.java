package org.hbrs.se2.project.control;

import com.vaadin.flow.component.html.Span;
import org.hbrs.se2.project.dtos.RatingDTO;
import org.hbrs.se2.project.services.impl.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class RatingControl {

    @Autowired
    RatingService ratingService;

    public Span getRating(int companyid){
        return ratingService.getRating(companyid);
    }

    public void createRating(RatingDTO rating){
        ratingService.createRating(rating);
    }

    public boolean studentHasRatedCompany(int companyid, int studentid){
        return ratingService.studentHasRatedCompany(companyid, studentid);
    }

}
