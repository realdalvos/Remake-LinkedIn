package org.hbrs.se2.project.repository;

import org.hbrs.se2.project.dtos.JobDTO;
import org.hbrs.se2.project.entities.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {

        //could return multiple datasets, careful
        JobDTO findJobByCompanyidAndTitle(int id, String title);

        List<JobDTO> findJobByCompanyid(int id);

        @Transactional
        @Query("SELECT j FROM Job j")
        List<JobDTO> getAll();
}
