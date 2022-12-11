package org.hbrs.se2.project.repository;

import org.hbrs.se2.project.dtos.MajorDTO;
import org.hbrs.se2.project.entities.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Set;

@Repository
public interface MajorRepository extends JpaRepository<Major, Integer> {

    MajorDTO findByMajor(String major);

    @Query("SELECT m FROM Major m JOIN m.students s WHERE s.studentid=:studentid")
    Set<MajorDTO> findByStudentid(int studentid);

}
