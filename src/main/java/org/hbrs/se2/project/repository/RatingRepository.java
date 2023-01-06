package org.hbrs.se2.project.repository;

import org.hbrs.se2.project.dtos.RatingDTO;
import org.hbrs.se2.project.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Float> {

    RatingDTO findByStudentid(int studentid);

    @Query("SELECT AVG(rating) FROM Rating r WHERE r.companyid=:companyid")
    Float getAvgRating(int companyid);

    @Query("SELECT COUNT(r) > 0 FROM Rating r WHERE r.companyid=:companyid AND r.studentid=:studentid")
    boolean studentHasRatedCompany(int companyid, int studentid);

}
