package org.hbrs.se2.project.repository;

import org.hbrs.se2.project.dtos.TopicDTO;
import org.hbrs.se2.project.entities.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public interface TopicRepository extends JpaRepository<Topic, Integer> {

    TopicDTO findByTopic(String topic);

    TopicDTO findByTopicid(int topicid);

}
