package org.hbrs.se2.project.repository;

import org.hbrs.se2.project.dtos.StudentHasTopicDTO;
import org.hbrs.se2.project.entities.StudentHasTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public interface StudentHasTopicRepository extends JpaRepository<StudentHasTopic, Integer> {

    List<StudentHasTopicDTO> findByStudentid(int studentid);

    @Transactional
    @Modifying
    @Query("DELETE FROM StudentHasTopic s WHERE s.studentid=:studentid AND s.topicid=:topicid")
    void deleteByStudentidAndTopicid(int studentid, int topicid);

    @Transactional
    @Query("SELECT COUNT(c) > 0 FROM StudentHasTopic c WHERE c.topicid=:topicid")
    boolean existsRelation(int topicid);

}
