package org.hbrs.se2.project.control;

import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.*;
import org.hbrs.se2.project.dtos.impl.CompanyDTOImpl;
import org.hbrs.se2.project.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.repository.*;
import org.hbrs.se2.project.util.HelperForTests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ProfileControlTest {
    Set<String> major = new HashSet<>();
    Set<String> topic = new HashSet<>();
    Set<String> skill = new HashSet<>();
    @Autowired
    ProfileControl profileControl;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MajorRepository majorRepository;
    @Autowired
    TopicRepository topicRepository;
    @Autowired
    SkillRepository skillRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    UserControl userControl;
    @Autowired
    HelperForTests h;
    @Autowired
    UserDTO userDTO;
    @Autowired
    StudentDTO studentDTO;
    @Autowired
    CompanyDTO companyDTO;
    static String changedUsername = "OtherTestUserCompany";
    static String changedEmail = "OthertestUser@JUnitTest.de";
    static String changedName = "OtherTestCompany";
    static String changedIndustry = "OtherTestIndustry";
    static boolean changedBann = true;
    @BeforeEach
    void setUp() {
        studentDTO = h.registerTestStudent();
        companyDTO = h.registerTestCompany();
        //userDTO = h.getUserDTOForStudent();
        major.add("Math");
        topic.add("Zahlen");
        skill.add("Kopfrechnen");
    }

    @AfterEach
    void tearDown(){
        h.deleteTestUsers();
    }

    @Test
    @DisplayName("Checking if saveStudentData-Method, getMajorOfStudent, getTopicOfStudent, getSkillOfStudent work as expected")
    void saveStudentTest(){
        int studentID = studentDTO.getUserid();
        userDTO = userRepository.findByUserid(studentID);
        try{
            profileControl.saveStudentData(userDTO, studentDTO, major, topic, skill);
        }
        catch (DatabaseUserException e){
            throw new Error("Something went wrong with saving student data");
        }

        Set<MajorDTO> majorDTOs= new HashSet<>();
        majorDTOs.add(majorRepository.findByMajor("Math"));
        Set<String> majors = majorDTOs.stream().map(MajorDTO::getMajor).collect(Collectors.toSet());
        assertEquals(majors, profileControl.getMajorOfStudent(studentID).stream().map(MajorDTO::getMajor).collect(Collectors.toSet()));

        Set<TopicDTO> topicDTOS= new HashSet<>();
        topicDTOS.add(topicRepository.findByTopic("Zahlen"));
        Set<String> topics = topicDTOS.stream().map(TopicDTO::getTopic).collect(Collectors.toSet());
        assertEquals(topics, profileControl.getTopicOfStudent(studentID).stream().map(TopicDTO::getTopic).collect(Collectors.toSet()));

        Set<SkillDTO> skillDTOS= new HashSet<>();
        skillDTOS.add(skillRepository.findBySkill("Kopfrechnen"));
        Set<String> skills = skillDTOS.stream().map(SkillDTO::getSkill).collect(Collectors.toSet());
        assertEquals(skills, profileControl.getSkillOfStudent(studentID).stream().map(SkillDTO::getSkill).collect(Collectors.toSet()));

    }
    @Test
    @DisplayName("Tries to save a companyDTO in the DB")
    void saveCompanyTest(){
        userDTO = userRepository.findByUserid(companyDTO.getUserid());
        try {
            profileControl.saveCompanyData(userDTO, companyDTO);
        }
        catch(DatabaseUserException e){
            throw new Error("Something went wrong with saving company data");
        }
        assertEquals(companyDTO.getName(),companyRepository.findByUserid(companyDTO.getUserid()).getName());
        assertEquals(companyDTO.getIndustry(),companyRepository.findByUserid(companyDTO.getUserid()).getIndustry());
        assertEquals(companyDTO.getBanned(),companyRepository.findByUserid(companyDTO.getUserid()).getBanned());
    }
    @Test
    @DisplayName("Tries to alter values name, industry and banned from testcompany")
    void alterCompanyProfileTest1(){
        CompanyDTO alteredCompanyDTO = new CompanyDTOImpl(companyDTO.getUserid(),changedName,changedIndustry,changedBann);
        alteredCompanyDTO.setCompanyid(companyDTO.getCompanyid());
        userDTO = userRepository.findByUserid(alteredCompanyDTO.getUserid());
        try {
            profileControl.saveCompanyData(userDTO, alteredCompanyDTO);
        }
        catch(DatabaseUserException e){
            throw new Error("Something went wrong with saving company data");
        }
        assertEquals(changedName,companyRepository.findByUserid(companyDTO.getUserid()).getName());
        assertEquals(changedIndustry,companyRepository.findByUserid(companyDTO.getUserid()).getIndustry());
        assertEquals(changedBann,companyRepository.findByUserid(companyDTO.getUserid()).getBanned());
    }
    @Test
    @DisplayName("tries to alter values username and email from testcompany")
    void alterCompanyProfileTest2(){
        userDTO = userRepository.findByUserid(companyDTO.getUserid());
        UserDTO alteredUserDTO = new UserDTOImpl(changedUsername,userDTO.getPassword(),changedEmail,userDTO.getRole());
        alteredUserDTO.setUserid(companyDTO.getUserid());
        try {
            profileControl.saveCompanyData(alteredUserDTO, companyDTO);
        }
        catch(DatabaseUserException e){
            throw new Error("Something went wrong with saving company data");
        }
        assertEquals(changedUsername,userRepository.findByUserid(companyDTO.getUserid()).getUsername());
        assertEquals(changedEmail,userRepository.findByUserid(companyDTO.getUserid()).getEmail());
    }
    @Test
    @DisplayName("Checking if the remove Methods work as expected")
    void removeTest(){
        int studentID = studentDTO.getUserid();
        userDTO = userRepository.findByUserid(studentID);
        try{
            profileControl.saveStudentData(userDTO, studentDTO, major, topic, skill);
        }
        catch (DatabaseUserException e){
            throw new Error("Something went wrong with saving student data");
        }
        MajorDTO majorDTO = majorRepository.findByMajor("Math");
        assertDoesNotThrow(() -> profileControl.removeMajor(studentID, majorDTO.getMajorid()));

        TopicDTO topicDTO = topicRepository.findByTopic("Zahlen");
        assertDoesNotThrow(() -> profileControl.removeTopic(studentID, topicDTO.getTopicid()));

        SkillDTO skillDTO = skillRepository.findBySkill("Kopfrechnen");
        assertDoesNotThrow(() -> profileControl.removeSkill(studentID, skillDTO.getSkillid()));

    }

    @Test
    @DisplayName("Checking if the methods testing for unique work as expected")
    void uniqueTest(){
        userDTO = userRepository.findByUserid(studentDTO.getUserid());
        assertFalse(profileControl.checkUsernameUnique(userDTO.getUsername()));
        assertFalse(profileControl.checkEmailUnique(userDTO.getEmail()));
        assertFalse(profileControl.checkMatrikelnumberUnique(studentDTO.getMatrikelnumber()));
    }

    @Test
    @DisplayName("Checking if methods concerning company work")
    void companyTest(){
        assertDoesNotThrow(() -> profileControl.saveCompanyData(userRepository.findByUserid(companyDTO.getUserid()), companyDTO));
        assertEquals(companyDTO.getCompanyid(), userControl.getCompanyProfile(companyDTO.getUserid()).getCompanyid());
        assertEquals(companyDTO.getUserid(), userControl.getCompanyProfile(companyDTO.getUserid()).getUserid());
        assertEquals(companyDTO.getName(), userControl.getCompanyProfile(companyDTO.getUserid()).getName());
        assertEquals(companyDTO.getIndustry(), userControl.getCompanyProfile(companyDTO.getUserid()).getIndustry());
        assertEquals(companyDTO.getBanned(), userControl.getCompanyProfile(companyDTO.getUserid()).getBanned());
    }

    @Test
    @DisplayName("Checking if getStudentProfile() works as expected")
    void getStudentProfileTest(){
        assertEquals(studentDTO.getUserid(), userControl.getStudentProfile(studentDTO.getUserid()).getUserid());
        assertEquals(studentDTO.getStudentid(), userControl.getStudentProfile(studentDTO.getUserid()).getStudentid());
        assertEquals(studentDTO.getFirstname(), userControl.getStudentProfile(studentDTO.getUserid()).getFirstname());
        assertEquals(studentDTO.getLastname(), userControl.getStudentProfile(studentDTO.getUserid()).getLastname());
        assertEquals(studentDTO.getMatrikelnumber(), userControl.getStudentProfile(studentDTO.getUserid()).getMatrikelnumber());
        assertEquals(studentDTO.getUniversity(), userControl.getStudentProfile(studentDTO.getUserid()).getUniversity());

    }

    @Test
    @DisplayName("Checking if getStudentsMatchingKeyword() works as expected")
    void getStudentMatchingKeywordTest(){
        //These assertions kind of check for the same thing. Just wanted to make sure that the returned has a stored value.
        assertDoesNotThrow(() -> profileControl.getStudentsMatchingKeyword("hbrs").iterator().next());
        assertTrue(profileControl.getStudentsMatchingKeyword("hbrs").iterator().hasNext());
    }

    @Test
    @DisplayName("Checking if getUserByUserid() works as expected")
    void getUserByUseridTest(){
        userDTO = userRepository.findByUserid(studentDTO.getUserid());
        UserDTO userDTO2 = userControl.getUserByUserid(userDTO.getUserid());
        assertEquals(userDTO.getUserid(), userDTO2.getUserid());
        assertEquals(userDTO.getUsername(), userDTO2.getUsername());
        assertEquals(userDTO.getEmail(), userDTO2.getEmail());
        assertEquals(userDTO.getRole(), userDTO2.getRole());
    }
}