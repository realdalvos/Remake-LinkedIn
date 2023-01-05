package org.hbrs.se2.project.services.impl;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.hbrs.se2.project.dtos.RatingDTO;
import org.hbrs.se2.project.repository.RatingRepository;
import org.hbrs.se2.project.services.factory.EntityCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingService {

    @Autowired
    RatingRepository ratingRepository;
    @Autowired
    EntityCreationService entityCreationService;

    public Span getRating(int companyid){
        Span rating = new Span("Unternehmensreputation: ");
        Float avg = ratingRepository.getAvgRating(companyid);
        if (avg == null) {
            avg = 0.0F;
        }
        for (int i = 0; i < 5; i++) {
            if (avg >= 1) {
                rating.add(new Icon(VaadinIcon.STAR));
            } else if (avg > 0.25 && avg < 0.75) {
                rating.add(new Icon(VaadinIcon.STAR_HALF_LEFT_O));
            } else {
                rating.add(new Icon(VaadinIcon.STAR_O));
            }
            avg = avg - 1;
        }
        return rating;
    }

    public void createRating(RatingDTO rating){
        ratingRepository.save(entityCreationService.ratingFactory().createEntity(rating));
    }

    public boolean studentHasRatedCompany(int companyid, int studentid){
        return ratingRepository.studentHasRatedCompany(companyid, studentid);
    }

}
