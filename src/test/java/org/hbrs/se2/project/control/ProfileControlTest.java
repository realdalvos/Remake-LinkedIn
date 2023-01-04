package org.hbrs.se2.project.control;

import org.bouncycastle.util.Strings;
import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.*;
import org.hbrs.se2.project.dtos.impl.CompanyDTOImpl;
import org.hbrs.se2.project.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.repository.*;
import org.hbrs.se2.project.repository.MajorRepository;
import org.hbrs.se2.project.repository.SkillRepository;
import org.hbrs.se2.project.repository.TopicRepository;
import org.hbrs.se2.project.repository.UserRepository;
import org.hbrs.se2.project.util.HelperForTests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
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
    @DisplayName("Tests if the method getStudentsMatchingKeyword returns the test student if the topic, major or skill matches. " +
            "There is only 1 major, 1 topic and 1 skill.")
    void test_getStudentsMatchingKeyword_with_single_student_with_single_entry_in_major_topic_skill() throws DatabaseUserException {
        List<StudentDTO> students = h.registerTestStudents(1);
        StudentDTO student0 = students.get(0);

        String[] keywordsMajorTopicSkill = new String[]{"major0", "topic0", "skill0"};
        ArrayList<Set<String>> setsOfKeywords = new ArrayList<>();
        for(String keyword: keywordsMajorTopicSkill){
            setsOfKeywords.add(new HashSet<String>(List.of(keyword)));
        }

        profileControl.saveStudentData(userRepository.findByUserid(student0.getUserid()), student0, setsOfKeywords.get(0), setsOfKeywords.get(1), setsOfKeywords.get(2));

        //Method getStudentsMatchingKeyword should return the test student when either major, topic or skill matches
        for(String keyword: keywordsMajorTopicSkill){
            Set<StudentDTO> results = profileControl.getStudentsMatchingKeyword(keyword);
            assertEquals(1, results.stream().
                            map(student -> student.getStudentid()).
                            filter(studid -> studid == student0.getStudentid()).
                            count(),
                    "Result set should contain student0 with keyword " +keyword+ ".");
        }
    }

    @Test
    @DisplayName("Tests if the method getStudentsMatchingKeyword returns the test student if the topic, major or skill matches. " +
            "There are multiple majors, topics and skills.")
    void test_getStudentsMatchingKeyword_with_single_student_with_multiple_entry_in_major_topic_skill() throws DatabaseUserException {
        List<StudentDTO> students = h.registerTestStudents(1);
        StudentDTO student0 = students.get(0);

        String[][] keywordsMajorTopicSkill = new String[][]{new String[]{"major0", "secondKeyword0"}, new String[]{"topic0", "secondKeyword1"}, new String[]{"skill0",  "secondKeyword2"}};
        ArrayList<Set<String>> setsOfKeywords = new ArrayList<>();
        for(String[] keywords: keywordsMajorTopicSkill){
            setsOfKeywords.add(new HashSet<String>(List.of(keywords)));
        }

        profileControl.saveStudentData(userRepository.findByUserid(student0.getUserid()), student0, setsOfKeywords.get(0), setsOfKeywords.get(1), setsOfKeywords.get(2));

        //Method getStudentsMatchingKeyword should return the test student when either major, topic or skill matches
        for(String[] keywords : keywordsMajorTopicSkill) {
            for (String keyword : keywords) {
                System.out.println(keyword);
                Set<StudentDTO> results = profileControl.getStudentsMatchingKeyword(keyword);
                assertEquals(1, results.stream().
                                map(student -> student.getStudentid()).
                                filter(studid -> studid == student0.getStudentid()).
                                count(),
                        "Result set should contain student0 with keyword " +keyword+ ".");
            }
        }
    }

    @Test
    @DisplayName("Tests if the method getStudentsMatchingKeyword returns the test students if the topic, major or skill matches. " +
            "There is only 1 major, 1 topic and 1 skill.")
    void test_getStudentsMatchingKeyword_with_multiple_students_with_same_major_topic_skill() throws DatabaseUserException {
        List<StudentDTO> students = h.registerTestStudents(2);
        StudentDTO student0 = students.get(0);
        StudentDTO student1 = students.get(1);

        String[] keywordsMajorTopicSkill = new String[]{"major0", "topic0", "skill0"};
        ArrayList<Set<String>> setsOfKeywords = new ArrayList<>();
        for(String keyword: keywordsMajorTopicSkill){
            setsOfKeywords.add(new HashSet<String>(List.of(keyword)));
        }

        profileControl.saveStudentData(userRepository.findByUserid(student0.getUserid()), student0, setsOfKeywords.get(0), setsOfKeywords.get(1), setsOfKeywords.get(2));
        profileControl.saveStudentData(userRepository.findByUserid(student1.getUserid()), student1, setsOfKeywords.get(0), setsOfKeywords.get(1), setsOfKeywords.get(2));

        //Method getStudentsMatchingKeyword should return both test students when either major, topic or skill matches
        for(String keyword: keywordsMajorTopicSkill){
            Set<StudentDTO> results = profileControl.getStudentsMatchingKeyword(keyword);
            assertEquals(2, results.stream().
                            map(student -> student.getStudentid()).
                            filter(studid -> studid == student0.getStudentid() || studid == student1.getStudentid()).
                            count(),
                    "Result set should contain both student0 and student1 with keyword " +keyword+ ".");
        }
    }

    @Test
    @DisplayName("Tests if the method getStudentsMatchingKeyword returns the test students if the topic, major or skill matches. " +
            "There is only 1 major, 1 topic and 1 skill.")
    void test_getStudentsMatchingKeyword_with_multiple_students_with_different_major_topic_skill() throws DatabaseUserException {
        List<StudentDTO> students = h.registerTestStudents(2);
        StudentDTO student0 = students.get(0);
        StudentDTO student1 = students.get(1);

        String[] keywordsMajorTopic0 = new String[]{"majorStud0", "topicStud0"};
        ArrayList<Set<String>> setsOfKeywords0 = new ArrayList<>();
        for(String keyword: keywordsMajorTopic0){
            setsOfKeywords0.add(new HashSet<String>(List.of(keyword)));
        }

        String[] keywordsMajorTopic1 = new String[]{"majorStud1", "topicStud1"};
        ArrayList<Set<String>> setsOfKeywords1 = new ArrayList<>();
        for(String keyword: keywordsMajorTopic1){
            setsOfKeywords1.add(new HashSet<String>(List.of(keyword)));
        }

        String[] sharedSkill = new String[]{"SharedSkill"};
        ArrayList<Set<String>> setOfSharedSkill = new ArrayList<>();
        for(String keyword: sharedSkill){
            setOfSharedSkill.add(new HashSet<String>(List.of(keyword)));
        }

        profileControl.saveStudentData(userRepository.findByUserid(student0.getUserid()), student0, setsOfKeywords0.get(0), setsOfKeywords0.get(1), setOfSharedSkill.get(0));
        profileControl.saveStudentData(userRepository.findByUserid(student1.getUserid()), student1, setsOfKeywords1.get(0), setsOfKeywords1.get(1), setOfSharedSkill.get(0));

        //Method getStudentsMatchingKeyword should return the student0, not student1
        for(String keyword : keywordsMajorTopic0){
            Set<StudentDTO> results = profileControl.getStudentsMatchingKeyword(keyword);
            assertEquals(1, results.stream().
                            map(student -> student.getStudentid()).
                            filter(studid -> studid == student0.getStudentid()).
                            count(),
                    "Result set should contain student0 with keyword " +keyword+ ".");

            assertEquals(0, results.stream().
                            map(student -> student.getStudentid()).
                            filter(studid -> studid == student1.getStudentid()).
                            count(),
                    "Result set should NOT contain student1 with keyword " +keyword+ ".");
        }

        //Method getStudentsMatchingKeyword should return the student1, not student0
        for(String keyword : keywordsMajorTopic1){
            Set<StudentDTO> results = profileControl.getStudentsMatchingKeyword(keyword);
            assertEquals(1, results.stream().
                            map(student -> student.getStudentid()).
                            filter(studid -> studid == student1.getStudentid()).
                            count(),
                    "Result set should contain student1 with keyword " +keyword+ ".");

            assertEquals(0, results.stream().
                            map(student -> student.getStudentid()).
                            filter(studid -> studid == student0.getStudentid()).
                            count(),
                    "Result set should NOT contain student0 with keyword " +keyword+ ".");
        }

        //Method getStudentsMatchingKeyword should return student0 and student1
        for(String keyword : sharedSkill){
            Set<StudentDTO> results = profileControl.getStudentsMatchingKeyword(keyword);
            assertEquals(2, results.stream().
                            map(student -> student.getStudentid()).
                            filter(studid -> studid == student0.getStudentid() || studid == student1.getStudentid()).
                            count(),
                    "Result set should contain student0 and student1.");
        }
    }

    @Test
    @DisplayName("Tests if the method getStudentsMatchingKeyword returns the test students if the topic, major or skill partly matches. " +
            "There is only 1 major, 1 topic and 1 skill.")
    void test_getStudentsMatchingKeyword_with_multiple_students_with_fragmented_keyword() throws DatabaseUserException {
        List<StudentDTO> students = h.registerTestStudents(2);
        StudentDTO student0 = students.get(0);
        StudentDTO student1 = students.get(1);

        String[] keywordsMajorTopicSkill0 = new String[]{"majorStud0", "topicStud1", "skillStud2"};
        ArrayList<Set<String>> setsOfKeywords0 = new ArrayList<>();
        for(String keyword: keywordsMajorTopicSkill0){
            setsOfKeywords0.add(new HashSet<String>(List.of(keyword)));
        }

        String[] keywordsMajorTopicSkill1 = new String[]{"majorStud3", "topicStud4", "skillStud5"};
        ArrayList<Set<String>> setsOfKeywords1 = new ArrayList<>();
        for(String keyword: keywordsMajorTopicSkill1){
            setsOfKeywords1.add(new HashSet<String>(List.of(keyword)));
        }

        profileControl.saveStudentData(userRepository.findByUserid(student0.getUserid()), student0, setsOfKeywords0.get(0), setsOfKeywords0.get(1), setsOfKeywords0.get(2));
        profileControl.saveStudentData(userRepository.findByUserid(student1.getUserid()), student1, setsOfKeywords1.get(0), setsOfKeywords1.get(1), setsOfKeywords1.get(2));

        //Method getStudentsMatchingKeyword should return the student0, not student1
        for(String keyword : new String[]{"Stud0", "Stud1", "Stud2"}){
            Set<StudentDTO> results = profileControl.getStudentsMatchingKeyword(keyword);
            assertEquals(1, results.stream().
                            map(StudentDTO::getStudentid).
                            filter(studid -> studid == student0.getStudentid()).
                            count(),
                    "Result set should contain student0 with keyword " +keyword+ ".");

            assertEquals(0, results.stream().
                            map(StudentDTO::getStudentid).
                            filter(studid -> studid == student1.getStudentid()).
                            count(),
                    "Result set should NOT contain student1 with keyword " +keyword+ ".");
        }

        //Method getStudentsMatchingKeyword should return the student1, not student0
        for(String keyword : new String[]{"Stud3", "Stud4", "Stud5"}){
            Set<StudentDTO> results = profileControl.getStudentsMatchingKeyword(keyword);
            assertEquals(1, results.stream().
                            map(student -> student.getStudentid()).
                            filter(studid -> studid == student1.getStudentid()).
                            count(),
                    "Result set should contain student1 with keyword " +keyword+ ".");

            assertEquals(0, results.stream().
                            map(student -> student.getStudentid()).
                            filter(studid -> studid == student0.getStudentid()).
                            count(),
                    "Result set should NOT contain student0 with keyword " +keyword+ ".");
        }

        //Method getStudentsMatchingKeyword should return student0 and student1
        for(String keyword : new String[]{"major", "topic", "skill"}){
            Set<StudentDTO> results = profileControl.getStudentsMatchingKeyword(keyword);
            assertEquals(2, results.stream().
                            map(student -> student.getStudentid()).
                            filter(studid -> studid == student0.getStudentid() || studid == student1.getStudentid()).
                            count(),
                    "Result set should contain student0 and student1 with keyword " +keyword+ ".");
        }
    }



    @Test
    @DisplayName("Tests if the method getStudentsMatchingKeyword works, regardless if keyword is upper- or lowercase.")
    void test_getStudentsMatchingKeyword_case_sensitive() throws DatabaseUserException {
        List<StudentDTO> students = h.registerTestStudents(1);
        StudentDTO student0 = students.get(0);

        String[] keywordsMajorTopicSkillUniversity = new String[]{"major0", "topic0", "skill0", "hbrs"};
        ArrayList<Set<String>> setsOfKeywords = new ArrayList<>();
        for(String keyword: keywordsMajorTopicSkillUniversity){
            setsOfKeywords.add(new HashSet<String>(List.of(keyword)));
        }

        profileControl.saveStudentData(userRepository.findByUserid(student0.getUserid()), student0, setsOfKeywords.get(0), setsOfKeywords.get(1), setsOfKeywords.get(2));

        //Method getStudentsMatchingKeyword should return the test student when either major, topic or skill matches
        //lower case
        for(String keyword: keywordsMajorTopicSkillUniversity){
            Set<StudentDTO> results = profileControl.getStudentsMatchingKeyword(keyword);
            assertEquals(1, results.stream().
                            map(student -> student.getStudentid()).
                            filter(studid -> studid == student0.getStudentid()).
                            count(),
                    "Result set should contain student0 with keyword " +keyword+ ".");
        }

        for(int i = 0; i < keywordsMajorTopicSkillUniversity.length; i++){
            keywordsMajorTopicSkillUniversity[i] = keywordsMajorTopicSkillUniversity[i].toUpperCase();
        }
        //Method getStudentsMatchingKeyword should return the test student when either major, topic or skill matches
        //upper case
        for(String keyword : keywordsMajorTopicSkillUniversity){
            System.out.println(keyword);
            Set<StudentDTO> results = profileControl.getStudentsMatchingKeyword(keyword);
            assertEquals(1, results.stream().
                            map(student -> student.getStudentid()).
                            filter(studid -> studid == student0.getStudentid()).
                            count(),
                    "Result set should contain student0 with keyword " +keyword+ ".");
        }
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