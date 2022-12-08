package org.hbrs.se2.project.repository;

import org.hbrs.se2.project.dtos.SkillDTO;
import org.hbrs.se2.project.entities.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Set;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer> {

    SkillDTO findBySkill(String skill);

    @Query("SELECT sk FROM Skill sk JOIN sk.students s WHERE s.studentid=:studentid")
    Set<SkillDTO> findByStudentid(int studentid);

}
