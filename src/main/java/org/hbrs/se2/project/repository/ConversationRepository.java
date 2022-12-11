package org.hbrs.se2.project.repository;

import org.hbrs.se2.project.dtos.ConversationDTO;
import org.hbrs.se2.project.entities.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Integer> {

    List<ConversationDTO> findByStudentid(int studentid);

    List<ConversationDTO> findByCompanyid(int companyid);

}
