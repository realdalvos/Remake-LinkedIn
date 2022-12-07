package org.hbrs.se2.project.control;

import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.*;
import org.hbrs.se2.project.repository.*;
import org.hbrs.se2.project.util.HelperForTests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ProfileControlTest {
    List<String> major = new ArrayList<>();
    List<String> topic = new ArrayList<>();
    List<String> skill = new ArrayList<>();
    @Autowired
    ProfileControl profileControl;

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MajorRepository majorRepository;
    @Autowired
    TopicRepository topicRepository;
    @Autowired
    SkillRepository skillRepository;
    @Autowired
    HelperForTests h;
    @Autowired
    UserDTO userDTO;
    @Autowired
    StudentDTO studentDTO;
    @Autowired
    CompanyDTO companyDTO;

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
        userDTO = userRepository.findUserByUserid(studentID);
        try{
            profileControl.saveStudentData(userDTO, studentDTO, major, topic, skill);
        }
        catch (DatabaseUserException e){
            throw new Error("Something went wrong with saving student data");
        }

        List<MajorDTO> majorDTOs= new ArrayList<>();
        majorDTOs.add(majorRepository.findByMajor("Math"));
        List<String> majors = majorDTOs.stream().map(MajorDTO::getMajor).collect(Collectors.toList());
        assertEquals(majors, profileControl.getMajorOfStudent(studentID).stream().map(MajorDTO::getMajor).collect(Collectors.toList()));

        List<TopicDTO> topicDTOS= new ArrayList<>();
        topicDTOS.add(topicRepository.findByTopic("Zahlen"));
        List<String> topics = topicDTOS.stream().map(TopicDTO::getTopic).collect(Collectors.toList());
        assertEquals(topics, profileControl.getTopicOfStudent(studentID).stream().map(TopicDTO::getTopic).collect(Collectors.toList()));

        List<SkillDTO> skillDTOS= new ArrayList<>();
        skillDTOS.add(skillRepository.findBySkill("Kopfrechnen"));
        List<String> skills = skillDTOS.stream().map(SkillDTO::getSkill).collect(Collectors.toList());
        assertEquals(skills, profileControl.getSkillOfStudent(studentID).stream().map(SkillDTO::getSkill).collect(Collectors.toList()));

    }

    @Test
    @DisplayName("Checking if the remove Methods work as expected")
    void removeTest(){
        int studentID = studentDTO.getUserid();
        userDTO = userRepository.findUserByUserid(studentID);
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
        userDTO = userRepository.findUserByUserid(studentDTO.getUserid());
        assertFalse(profileControl.checkUsernameUnique(userDTO.getUsername()));
        assertFalse(profileControl.checkEmailUnique(userDTO.getEmail()));
        assertFalse(profileControl.checkMatrikelnumberUnique(studentDTO.getMatrikelnumber()));
    }

    @Test
    @DisplayName("Checking if methods concerning company work")
    void companyTest(){
        assertDoesNotThrow(() -> profileControl.saveCompanyData(userRepository.findUserByUserid(companyDTO.getUserid()), companyDTO));
        assertEquals(companyDTO.getCompanyid(), profileControl.getCompanyProfile(companyDTO.getUserid()).getCompanyid());
        assertEquals(companyDTO.getUserid(), profileControl.getCompanyProfile(companyDTO.getUserid()).getUserid());
        assertEquals(companyDTO.getName(), profileControl.getCompanyProfile(companyDTO.getUserid()).getName());
        assertEquals(companyDTO.getIndustry(), profileControl.getCompanyProfile(companyDTO.getUserid()).getIndustry());
        assertEquals(companyDTO.getBanned(), profileControl.getCompanyProfile(companyDTO.getUserid()).getBanned());
    }

    @Test
    @DisplayName("Checking if getStudentProfile() works as expected")
    void getStudentProfileTest(){
        assertEquals(studentDTO.getUserid(), profileControl.getStudentProfile(studentDTO.getUserid()).getUserid());
        assertEquals(studentDTO.getStudentid(), profileControl.getStudentProfile(studentDTO.getUserid()).getStudentid());
        assertEquals(studentDTO.getFirstname(), profileControl.getStudentProfile(studentDTO.getUserid()).getFirstname());
        assertEquals(studentDTO.getLastname(), profileControl.getStudentProfile(studentDTO.getUserid()).getLastname());
        assertEquals(studentDTO.getMatrikelnumber(), profileControl.getStudentProfile(studentDTO.getUserid()).getMatrikelnumber());
        assertEquals(studentDTO.getUniversity(), profileControl.getStudentProfile(studentDTO.getUserid()).getUniversity());

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
        userDTO = userRepository.findUserByUserid(studentDTO.getUserid());
        UserDTO userDTO2 = profileControl.getUserByUserid(userDTO.getUserid());
        assertEquals(userDTO.getUserid(), userDTO2.getUserid());
        assertEquals(userDTO.getUsername(), userDTO2.getUsername());
        assertEquals(userDTO.getEmail(), userDTO2.getEmail());
        assertEquals(userDTO.getRole(), userDTO2.getRole());
    }
}