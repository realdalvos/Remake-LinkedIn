package org.hbrs.se2.project.repository;

import org.hbrs.se2.project.dtos.MessageDTO;
import org.hbrs.se2.project.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Query("SELECT m FROM Message m WHERE m.conversationid=:conversationid ORDER BY m.timestamp ASC")
    List<MessageDTO> findByConversationid(int conversationid);

    @Query("SELECT COUNT(c) FROM Message c WHERE c.conversationid=:conversationid AND NOT c.userid=:userid AND c.read=FALSE")
    int getUnreadMessagesFromConversation(int conversationid, int userid);

    @Query("SELECT COUNT(m) FROM Message m JOIN m.conversation c WHERE c.companyid=:companyid AND m.read=FALSE AND NOT m.userid=:userid")
    int getUnreadMessagesFromCompany(int companyid, int userid);

    @Query("SELECT COUNT(m) FROM Message m JOIN m.conversation c WHERE c.studentid=:studentid AND m.read=FALSE AND NOT m.userid=:userid")
    int getUnreadMessagesFromStudent(int studentid, int userid);

    @Query("SELECT MAX(timestamp) FROM Message m WHERE m.conversationid=:conversationid")
    Instant getLatestMessageFromConversation(int conversationid);

    @Transactional
    @Modifying
    @Query("UPDATE Message m SET m.read=TRUE WHERE m.conversationid=:conversationid AND m.userid=:userid")
    void setRead(int conversationid, int userid);

}
