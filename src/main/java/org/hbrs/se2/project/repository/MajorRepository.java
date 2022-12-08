package org.hbrs.se2.project.repository;

import org.hbrs.se2.project.dtos.MajorDTO;
import org.hbrs.se2.project.entities.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MajorRepository extends JpaRepository<Major, Integer> {

    MajorDTO findByMajor(String major);

    MajorDTO findByMajorid(int majorid);

    @Query("SELECT m FROM Major m WHERE LOWER(m.major) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<MajorDTO> findByKeyword(String keyword);

}
