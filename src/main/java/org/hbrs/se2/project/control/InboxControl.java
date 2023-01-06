package org.hbrs.se2.project.control;

import com.vaadin.flow.component.messages.MessageList;
import org.hbrs.se2.project.dtos.ConversationDTO;
import org.hbrs.se2.project.dtos.MessageDTO;
import org.hbrs.se2.project.services.impl.InboxService;
import org.springframework.stereotype.Controller;
import java.time.Instant;
import java.util.Set;

@Controller
public class InboxControl {

    private final InboxService inboxService;

    public InboxControl(InboxService inboxService) {
        this.inboxService = inboxService;
    }

    /**
     * Get all conversations of a student
     * @param studentid Student ID
     * @return Set of all conversations of a student
     */
    public Set<ConversationDTO> getConversationsOfStudent(int studentid) {
        return inboxService.getConversationsOfStudent(studentid);
    }

    /**
     * Get all conversations of a company
     * @param companyid Company ID
     * @return Set of all conversations of a company
     */
    public Set<ConversationDTO> getConversationsOfCompany(int companyid) {
        return inboxService.getConversationsOfCompany(companyid);
    }

    /**
     * Get all messages of a given conversation from a student
     * @param conversation ConversationDTO representing the conversation
     * @return MessageList of a conversations from a student
     */
    public MessageList getMessagesOfStudent(ConversationDTO conversation) {
        return inboxService.getMessagesOfStudent(conversation);
    }

    /**
     * Get all messages of a given conversation from a company
     * @param conversation ConversationDTO representing the conversation
     * @return MessageList of a conversations from a company
     */
    public MessageList getMessagesOfCompany(ConversationDTO conversation) {
        return inboxService.getMessagesOfCompany(conversation);
    }

    /**
     * Get the number of unread messages of a conversation for a generic user
     * @param conversationid Conversation ID of the conversation
     * @param userid User ID of the user
     * @return Number of unread messages of the user for the conversation
     */
    public int getNumberOfUnreadMessagesFromConversation(int conversationid, int userid) {
        return inboxService.getNumberOfUnreadMessagesFromConversation(conversationid, userid);
    }

    /**
     * Get the number of unread messages of all conversations for a student
     * @param userid User ID of the student
     * @return Number of unread messages of the student
     */
    public int getNumberOfUnreadMessagesFromStudent(int userid) {
        return inboxService.getNumberOfUnreadMessagesFromStudent(userid);
    }

    /**
     * Get the number of unread messages of all conversations for a company
     * @param userid User ID of the company
     * @return Number of unread messages of the company
     */
    public int getNumberOfUnreadMessagesFromCompany(int userid) {
        return inboxService.getNumberOfUnreadMessagesFromCompany(userid);
    }

    /**
     * Get timestamp of the latest message of a given conversation
     * @param conversationid Conversation ID
     * @return An Instant representing the latest timestamp
     */
    public Instant getLatestMessageTime(int conversationid) {
        return inboxService.getLatestMessageTime(conversationid);
    }

    /**
     * Save a new message in the Database
     * @param message MessageDTO to be persisted into the DB
     */
    public void saveMessage(MessageDTO message) {
        inboxService.saveMessage(message);
    }

    /**
     * Save a new Conversation in the Database
     * @param conversation ConversationDTO representing the conversation
     * @return DTO reference to the newly created conversation
     */
    public ConversationDTO newConversation(ConversationDTO conversation) {
        return inboxService.newConversation(conversation);
    }

    /**
     * Get the title of a job for a given ID
     * @param jobid Job ID of the job
     * @return String of the job title
     */
    public String getJob(int jobid) {
        return inboxService.getJob(jobid);
    }

    /**
     * Get the name of the student participating in a conversation
     * @param conversation ConversationDTO representing the conversation
     * @return String of the student's name
     */
    public String getNameOfStudentFromConversation(ConversationDTO conversation) {
        return inboxService.getNameOfStudentFromConversation(conversation);
    }

    /**
     * Get the name of the company participating in a conversation
     * @param conversation ConversationDTO representing the conversation
     * @return String of the company's name
     */
    public String getNameOfCompanyFromConversation(ConversationDTO conversation) {
        return  inboxService.getNameOfCompanyFromConversation(conversation);
    }

    /**
     * Remove the student from a given conversation
     * @param conversation ConversationDTO representing the conversation
     */
    public void endConversationStudent(ConversationDTO conversation) {
        inboxService.endConversationStudent(conversation);
    }

    /**
     * Remove the company from a given conversation
     * @param conversation ConversationDTO representing the conversation
     */
    public void endConversationCompany(ConversationDTO conversation) {
        inboxService.endConversationCompany(conversation);
    }

}
