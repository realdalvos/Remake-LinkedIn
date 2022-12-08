package org.hbrs.se2.project.repository;

import org.hbrs.se2.project.dtos.TopicDTO;
import org.hbrs.se2.project.entities.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Set;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {

    TopicDTO findByTopic(String topic);

    @Query("SELECT t FROM Topic t JOIN t.students s WHERE s.studentid=:studentid")
    Set<TopicDTO> findByStudentid(int studentid);

}
