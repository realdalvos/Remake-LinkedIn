package org.hbrs.se2.project.services.impl;

import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import org.hbrs.se2.project.dtos.ConversationDTO;
import org.hbrs.se2.project.dtos.MessageDTO;
import org.hbrs.se2.project.dtos.StudentDTO;
import org.hbrs.se2.project.dtos.impl.ConversationDTOImpl;
import org.hbrs.se2.project.repository.*;
import org.hbrs.se2.project.services.factory.EntityCreationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class InboxService {

    @Autowired
    ConversationRepository conversationRepository;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    EntityCreationService entityCreationService;
    @Autowired
    JobRepository jobRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    CompanyRepository companyRepository;

    private final ModelMapper mapper = new ModelMapper();

    public List<ConversationDTO> getConversationsOfStudent(int studentid) {
        return conversationRepository.findByStudentid(studentid);
    }

    public List<ConversationDTO> getConversationsOfCompany(int companyid) {
        return conversationRepository.findByCompanyid(companyid);
    }

    public MessageList getMessagesOfStudent(ConversationDTO conversation) {
        messageRepository.setRead(conversation.getConversationid(), companyRepository.findByCompanyid(conversation.getCompanyid()).getUserid());
        return getMessagesFromConversation(conversation.getConversationid());
    }

    public MessageList getMessagesOfCompany(ConversationDTO conversation) {
        messageRepository.setRead(conversation.getConversationid(), studentRepository.findByStudentid(conversation.getStudentid()).getUserid());
        return getMessagesFromConversation(conversation.getConversationid());
    }

    private MessageList getMessagesFromConversation(int conversationid) {
        MessageList list = new MessageList();
        List<MessageListItem> items = new ArrayList<>();
        messageRepository.findByConversationid(conversationid).forEach(message ->
                items.add(new MessageListItem(message.getContent(), message.getTimestamp(), userRepository.findByUserid(message.getUserid()).getUsername())));
        list.setItems(items);
        return list;
    }

    public int getNumberOfUnreadMessages(int conversationid, int userid) {
        return messageRepository.getUnreadMessagesFromConversation(conversationid, userid);
    }

    public Instant getLatestMessageTime(int conversationid) {
        return messageRepository.getLatestMessageFromConversation(conversationid);
    }

    public void saveMessage(MessageDTO message) {
        messageRepository.save(entityCreationService.messageFactory().createEntity(message));
    }

    public ConversationDTO newConversation(ConversationDTO conversation) {
        return mapper.map(conversationRepository.save(entityCreationService.conversationFactory().createEntity(conversation)), ConversationDTOImpl.class);
    }

    public String getJob(int jobid) {
        return jobRepository.findByJobid(jobid).getTitle();
    }

    public String getNameOfStudentFromConversation(ConversationDTO conversation) {
        StudentDTO student = studentRepository.findByStudentid(conversation.getStudentid());
        return student.getFirstname() + " " + student.getLastname();
    }

    public String getNameOfCompanyFromConversation(ConversationDTO conversation) {
        return companyRepository.findByCompanyid(conversation.getCompanyid()).getName();
    }

}
