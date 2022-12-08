package org.hbrs.se2.project.repository;

import org.hbrs.se2.project.dtos.TopicDTO;
import org.hbrs.se2.project.entities.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {

    TopicDTO findByTopic(String topic);

    TopicDTO findByTopicid(int topicid);

    @Query("SELECT t FROM Topic t WHERE LOWER(t.topic) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<TopicDTO> findByKeyword(String keyword);

}
