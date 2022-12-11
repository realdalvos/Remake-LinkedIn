package org.hbrs.se2.project.repository;

import org.hbrs.se2.project.dtos.JobDTO;
import org.hbrs.se2.project.entities.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {

        //could return multiple datasets, careful
        JobDTO findByCompanyidAndTitle(int id, String title);

        List<JobDTO> findByCompanyid(int id);

        JobDTO findByJobid(int id);

        @Query("SELECT j FROM Job j")
        List<JobDTO> getAll();

}
