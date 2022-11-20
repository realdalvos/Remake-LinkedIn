package org.hbrs.se2.project.repository;

import org.hbrs.se2.project.dtos.StudentHasSkillDTO;
import org.hbrs.se2.project.entities.StudentHasSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public interface StudentHasSkillRepository extends JpaRepository<StudentHasSkill, Integer> {

    List<StudentHasSkillDTO> findByStudentid(int skillid);

    @Transactional
    @Modifying
    @Query("DELETE FROM StudentHasSkill s WHERE s.studentid=:studentid AND s.skillid=:skillid")
    void deleteByStudentidAndSkillid(int studentid, int skillid);

}
