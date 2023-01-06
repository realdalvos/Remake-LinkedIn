package org.hbrs.se2.project.control;

import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import org.hbrs.se2.project.dtos.*;
import org.hbrs.se2.project.dtos.impl.ConversationDTOImpl;
import org.hbrs.se2.project.dtos.impl.JobDTOImpl;
import org.hbrs.se2.project.dtos.impl.MessageDTOImpl;
import org.hbrs.se2.project.repository.*;
import org.hbrs.se2.project.util.HelperForTests;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.Instant;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InboxControlTest {

    @Autowired
    InboxControl inboxControl;
    @Autowired
    ConversationRepository conversationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JobControl jobControl;
    @Autowired
    JobRepository jobRepository;
    @Autowired
    HelperForTests h;
    @Autowired
    StudentDTO studentDTO;
    @Autowired
    CompanyDTO companyDTO;
    @Autowired
    JobDTO jobDTO;

    @BeforeEach
    void setUp() {
        studentDTO = h.registerTestStudent();
        companyDTO = h.registerTestCompany();

        // create and save a new job
        jobDTO = new JobDTOImpl(
                companyDTO.getCompanyid(), "Test title", "Testbeschreibung. assembly programmer.", 20, "Test location", "Test contactdetails");
        jobControl.createNewJobPost(jobDTO);
        jobDTO = jobRepository.findByCompanyidAndTitle(jobDTO.getCompanyid(), jobDTO.getTitle());
    }

    @AfterEach
    @DisplayName("Deleting the user called \"JUnitTest\". By deleting both users, all conversations, including the messages, involving the users get deleted")
    void tearDown() {
        h.deleteTestUsers();
        conversationRepository.garbageCollection();
    }

    @Test
    @DisplayName("Tests if a new conversation about a specific job can be established between a student and a company.")
    void newConversationTest(){
        ConversationDTO conversationDTO = new ConversationDTOImpl();
        conversationDTO.setCompanyid(companyDTO.getCompanyid());
        conversationDTO.setStudentid(studentDTO.getStudentid());
        conversationDTO.setJobid(jobDTO.getJobid());
        conversationDTO.setTitle("Test conversation");

        ConversationDTO conversationFromRepo = inboxControl.newConversation(conversationDTO);
        Assertions.assertNotNull(conversationFromRepo);

        assertEquals("Test conversation", conversationFromRepo.getTitle());
        assertEquals(studentDTO.getStudentid(), conversationFromRepo.getStudentid());
        assertEquals(companyDTO.getCompanyid(), conversationFromRepo.getCompanyid());
        assertEquals(jobDTO.getJobid(), conversationFromRepo.getJobid());
    }

    @Test
    @DisplayName("Tests fetching of conversations per user.")
    void getConversationsTest(){
        ConversationDTO conversationDTO1 = createConversation("getConversationsTest1");
        ConversationDTO conversationDTO2 = createConversation("getConversationsTest2");
        ConversationDTO conversationDTO3 = createConversation("getConversationsTest3");
        createMessage(conversationDTO1, "Test message 1", Instant.now(), companyDTO.getUserid());
        createMessage(conversationDTO2, "Test message 2", Instant.now(), companyDTO.getUserid());
        createMessage(conversationDTO3, "Test message 3", Instant.now(), studentDTO.getUserid());

        Set<ConversationDTO> studentConversations = inboxControl.getConversationsOfStudent(studentDTO.getStudentid());
        assertEquals(3, studentConversations.size());
        assertTrue(studentConversations.stream().anyMatch(conversationDTO -> conversationDTO1.getConversationid() == conversationDTO.getConversationid()));
        assertTrue(studentConversations.stream().anyMatch(conversationDTO -> conversationDTO2.getTitle().equals(conversationDTO.getTitle())));
        assertTrue(studentConversations.stream().allMatch(conversationDTO -> studentDTO.getStudentid() == conversationDTO.getStudentid()));

        ConversationDTO conversationDTO4 = createConversation("getConversationsTest4");
        createMessage(conversationDTO4, "Test message 4", Instant.now(), studentDTO.getUserid());

        Set<ConversationDTO> companyConversations = inboxControl.getConversationsOfCompany(companyDTO.getCompanyid());
        assertEquals(4, companyConversations.size());
        assertTrue(companyConversations.stream().anyMatch(conversationDTO -> conversationDTO3.getConversationid() == conversationDTO.getConversationid()));
        assertTrue(companyConversations.stream().anyMatch(conversationDTO -> conversationDTO4.getTitle().equals(conversationDTO.getTitle())));
        assertTrue(companyConversations.stream().allMatch(conversationDTO -> companyDTO.getCompanyid() == conversationDTO.getCompanyid()));
    }

    @Test
    @DisplayName("Tests saving of a message as student.")
    void saveMessageStudentTest(){
        ConversationDTO conversationDTO = createConversation("saveMessageStudentTest");
        Instant testTime = Instant.now();
        MessageDTO message = createMessage(conversationDTO, "Test message student", testTime, studentDTO.getUserid());

        MessageList list = inboxControl.getMessagesOfStudent(conversationDTO);
        MessageListItem item = list.getItems().get(0);

        assertEquals(message.getContent(), item.getText());
        assertEquals(testTime, item.getTime());
        assertEquals(userRepository.findByUserid(studentDTO.getUserid()).getUsername(), item.getUserName());
    }

    @Test
    @DisplayName("Tests saving of a message as company.")
    void saveMessageCompanyTest(){
        ConversationDTO conversationDTO = createConversation("saveMessageCompanyTest");
        Instant testTime = Instant.now();
        MessageDTO message = createMessage(conversationDTO, "Test message company", testTime, companyDTO.getUserid());

        MessageList list = inboxControl.getMessagesOfCompany(conversationDTO);
        MessageListItem item = list.getItems().get(0);

        assertEquals(message.getContent(), item.getText());
        assertEquals(testTime, item.getTime());
        assertEquals(userRepository.findByUserid(companyDTO.getUserid()).getUsername(), item.getUserName());
    }

    @Test
    @DisplayName("Tests if multiple messages get saved correctly and get fetched in correct order.")
    void multipleMessagesTest(){
        ConversationDTO conversationDTO = createConversation("multipleMessagesTest");
        Instant testTime1 = Instant.now();
        Instant testTime2 = Instant.now();
        Instant testTime3 = Instant.now();
        MessageDTO message1 = createMessage(conversationDTO, "Test message 1", testTime1, studentDTO.getUserid());
        MessageDTO message2 = createMessage(conversationDTO, "Test message 2", testTime2, companyDTO.getUserid());
        MessageDTO message3 = createMessage(conversationDTO, "Test message 3", testTime3, studentDTO.getUserid());

        MessageList list = inboxControl.getMessagesOfCompany(conversationDTO);
        MessageListItem item1 = list.getItems().get(0);
        MessageListItem item2 = list.getItems().get(1);
        MessageListItem item3 = list.getItems().get(2);

        assertEquals(message1.getContent(), item1.getText());
        assertEquals(testTime1, item1.getTime());
        assertEquals(h.getUserDTOForStudent().getUsername(), item1.getUserName());
        assertEquals(message2.getContent(), item2.getText());
        assertEquals(testTime2, item2.getTime());
        assertEquals(h.getUserDTOForCompany().getUsername(), item2.getUserName());
        assertEquals(message3.getContent(), item3.getText());
        assertEquals(testTime3, item3.getTime());
        assertEquals(h.getUserDTOForStudent().getUsername(), item3.getUserName());
    }

    @Test
    @DisplayName("Tests if the correct participant names and job title are retrieved from a conversation.")
    void getNamesFromConversationTest(){
        ConversationDTO conversationDTO = createConversation("nameTest");

        assertEquals(studentDTO.getFirstname() + " " + studentDTO.getLastname(), inboxControl.getNameOfStudentFromConversation(conversationDTO));
        assertEquals(companyDTO.getName(), inboxControl.getNameOfCompanyFromConversation(conversationDTO));
        assertEquals(jobDTO.getTitle(), inboxControl.getJob(jobDTO.getJobid()));
    }

    @Test
    @DisplayName("Tests if the time of the latest message is retrieved from the repo.")
    void getLatestMessageTimeTest(){
        ConversationDTO conversationDTO = createConversation("timeTest");
        Instant testTime1 = Instant.now();
        Instant testTime2 = Instant.now();
        Instant testTime3 = Instant.now();
        createMessage(conversationDTO, "Test message 1", testTime1, studentDTO.getUserid());
        createMessage(conversationDTO, "Test message 2", testTime2, companyDTO.getUserid());
        createMessage(conversationDTO, "Test message 3", testTime3, studentDTO.getUserid());

        assertEquals(testTime3, inboxControl.getLatestMessageTime(conversationDTO.getConversationid()));
        assertNotEquals(testTime1, inboxControl.getLatestMessageTime(conversationDTO.getConversationid()));
    }

    @Test
    @DisplayName("Tests if the correct number of unread messages is retrieved per user.")
    void getNumberOfUnreadMessagesTest(){
        ConversationDTO conversationDTO = createConversation("unreadTest");
        createMultipleMessages(conversationDTO);

        assertEquals(1, inboxControl.getNumberOfUnreadMessagesFromConversation(conversationDTO.getConversationid(), studentDTO.getUserid()));
        assertEquals(1, inboxControl.getNumberOfUnreadMessagesFromStudent(studentDTO.getUserid()));
        assertEquals(inboxControl.getNumberOfUnreadMessagesFromConversation(conversationDTO.getConversationid(), studentDTO.getUserid()), inboxControl.getNumberOfUnreadMessagesFromStudent(studentDTO.getUserid()));
        assertEquals(2, inboxControl.getNumberOfUnreadMessagesFromConversation(conversationDTO.getConversationid(), companyDTO.getUserid()));
        assertEquals(2, inboxControl.getNumberOfUnreadMessagesFromCompany(companyDTO.getUserid()));
        assertEquals(inboxControl.getNumberOfUnreadMessagesFromConversation(conversationDTO.getConversationid(), companyDTO.getUserid()), inboxControl.getNumberOfUnreadMessagesFromCompany(companyDTO.getUserid()));
    }

    private ConversationDTO createConversation(String title){
        ConversationDTO conversationDTO = new ConversationDTOImpl();
        conversationDTO.setCompanyid(companyDTO.getCompanyid());
        conversationDTO.setStudentid(studentDTO.getStudentid());
        conversationDTO.setJobid(jobDTO.getJobid());
        conversationDTO.setTitle(title);
        return inboxControl.newConversation(conversationDTO);
    }

    private MessageDTO createMessage(ConversationDTO conversationDTO, String content, Instant testTime, int userid){
        MessageDTO message = new MessageDTOImpl();
        message.setConversationid(conversationDTO.getConversationid());
        message.setContent(content);
        message.setTimestamp(testTime);
        message.setUserid(userid);
        inboxControl.saveMessage(message);
        return message;
    }

    private void createMultipleMessages(ConversationDTO conversationDTO){
        createMessage(conversationDTO, "Test message 1", Instant.now(), studentDTO.getUserid());
        createMessage(conversationDTO, "Test message 2", Instant.now(), companyDTO.getUserid());
        createMessage(conversationDTO, "Test message 3", Instant.now(), studentDTO.getUserid());
    }

}
