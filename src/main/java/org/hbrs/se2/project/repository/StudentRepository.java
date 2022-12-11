package org.hbrs.se2.project.repository;

import org.hbrs.se2.project.dtos.StudentDTO;
import org.hbrs.se2.project.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Set;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    StudentDTO findByMatrikelnumber(String matrikelnumber);

    StudentDTO findByUserid(int id);

    @Query("SELECT DISTINCT student FROM Student student " +
            "JOIN student.skills s " +
            "JOIN student.topics t " +
            "JOIN student.majors m " +
            "WHERE LOWER(student.university) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(s.skill) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(t.topic) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(m.major) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Set<StudentDTO> findByKeyword(String keyword);

    @Query("SELECT COUNT(c) > 0 FROM Student s Join s.majors c WHERE c.majorid=:majorid")
    boolean existsMajorRelation(int majorid);

    @Query("SELECT COUNT(c) > 0 FROM Student s JOIN s.topics c WHERE c.topicid=:topicid")
    boolean existsTopicRelation(int topicid);

    @Query("SELECT COUNT(c) > 0 FROM Student s JOIN s.skills c WHERE c.skillid=:skillid")
    boolean existsSkillRelation(int skillid);

}
