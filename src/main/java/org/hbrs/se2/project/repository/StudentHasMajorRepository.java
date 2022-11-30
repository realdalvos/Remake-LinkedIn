package org.hbrs.se2.project.repository;

import org.hbrs.se2.project.dtos.StudentHasMajorDTO;
import org.hbrs.se2.project.entities.StudentHasMajor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StudentHasMajorRepository extends JpaRepository<StudentHasMajor, Integer> {

    List<StudentHasMajorDTO> findByStudentid(int studentid);

    @Transactional
    @Modifying
    @Query("DELETE FROM StudentHasMajor s WHERE s.studentid=:studentid AND s.majorid=:majorid")
    void deleteByStudentidAndMajorid(int studentid, int majorid);

    @Transactional
    @Query("SELECT COUNT(c) > 0 FROM StudentHasMajor c WHERE c.majorid=:majorid")
    boolean existsRelation(int majorid);

}
