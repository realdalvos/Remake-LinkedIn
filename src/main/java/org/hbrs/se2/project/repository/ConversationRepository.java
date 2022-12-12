package org.hbrs.se2.project.repository;

import org.hbrs.se2.project.dtos.ConversationDTO;
import org.hbrs.se2.project.entities.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Integer> {

    @Query("SELECT c FROM Conversation c JOIN c.messages m WHERE c.studentid=:studentid ORDER BY m.timestamp DESC")
    List<ConversationDTO> findByStudentid(int studentid);

    @Query("SELECT c FROM Conversation c JOIN c.messages m WHERE c.companyid=:companyid ORDER BY m.timestamp DESC")
    List<ConversationDTO> findByCompanyid(int companyid);

    @Transactional
    @Modifying
    @Query("DELETE FROM Conversation c WHERE c.studentid=NULL AND c.companyid=NULL")
    void garbageCollection();

}
