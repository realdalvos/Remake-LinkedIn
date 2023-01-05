package org.hbrs.se2.project.services.impl;

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

    public Float getRating(int companyid){
        return ratingRepository.getAvgRating(companyid);
    }

    public void createRating(RatingDTO rating){
        ratingRepository.save(entityCreationService.ratingFactory().createEntity(rating));
    }

    public boolean studentHasRatedCompany(int companyid, int studentid){
        return ratingRepository.studentHasRatedCompany(companyid, studentid);
    }

}
