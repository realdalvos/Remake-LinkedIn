package org.hbrs.se2.project.repository;

import org.hbrs.se2.project.dtos.StudentHasSkillDTO;
import org.hbrs.se2.project.entities.StudentHasSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StudentHasSkillRepository extends JpaRepository<StudentHasSkill, Integer> {

    List<StudentHasSkillDTO> findByStudentid(int skillid);

    @Transactional
    @Modifying
    @Query("DELETE FROM StudentHasSkill s WHERE s.studentid=:studentid AND s.skillid=:skillid")
    void deleteByStudentidAndSkillid(int studentid, int skillid);

    @Transactional
    @Query("SELECT COUNT(c) > 0 FROM StudentHasSkill c WHERE c.skillid=:skillid")
    boolean existsRelation(int skillid);

}
