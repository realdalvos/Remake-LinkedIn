package org.hbrs.se2.project.repository;

import org.hbrs.se2.project.dtos.MajorDTO;
import org.hbrs.se2.project.entities.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public interface MajorRepository extends JpaRepository<Major, Integer> {

    MajorDTO findByMajor(String major);

    MajorDTO findByMajorid(int majorid);

    @Transactional
    @Modifying
    @Query("DELETE FROM Major m WHERE m.majorid=:majorid")
    void deleteByMajorid(int majorid);

}
