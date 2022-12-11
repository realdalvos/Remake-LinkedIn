package org.hbrs.se2.project.control;

import com.vaadin.flow.component.messages.MessageList;
import org.hbrs.se2.project.dtos.ConversationDTO;
import org.hbrs.se2.project.dtos.MessageDTO;
import org.hbrs.se2.project.services.impl.InboxService;
import org.springframework.stereotype.Controller;
import java.time.Instant;
import java.util.List;

@Controller
public class InboxControl {

    final InboxService inboxService;

    public InboxControl(InboxService inboxService) {
        this.inboxService = inboxService;
    }

    public List<ConversationDTO> getConversationsOfStudent(int studentid) {
        return inboxService.getConversationsOfStudent(studentid);
    }

    public List<ConversationDTO> getConversationsOfCompany(int companyid) {
        return inboxService.getConversationsOfCompany(companyid);
    }

    public MessageList getMessagesOfStudent(ConversationDTO conversation) {
        return inboxService.getMessagesOfStudent(conversation);
    }

    public MessageList getMessagesOfCompany(ConversationDTO conversation) {
        return inboxService.getMessagesOfCompany(conversation);
    }

    public int getNumberOfUnreadMessages(int conversationid, int userid) {
        return inboxService.getNumberOfUnreadMessages(conversationid, userid);
    }

    public Instant getLatestMessageTime(int conversationid) {
        return inboxService.getLatestMessageTime(conversationid);
    }

    public void saveMessage(MessageDTO message) {
        inboxService.saveMessage(message);
    }

    public ConversationDTO newConversation(ConversationDTO conversation) {
        return inboxService.newConversation(conversation);
    }

    public String getJob(int jobid) {
        return inboxService.getJob(jobid);
    }

    public String getNameOfStudentFromConversation(ConversationDTO conversation) {
        return inboxService.getNameOfStudentFromConversation(conversation);
    }

    public String getNameOfCompanyFromConversation(ConversationDTO conversation) {
        return  inboxService.getNameOfCompanyFromConversation(conversation);
    }

}
