package org.hbrs.se2.project.services.impl;

import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.*;
import org.hbrs.se2.project.repository.*;
import org.hbrs.se2.project.services.ProfileServiceInterface;
import org.hbrs.se2.project.services.factory.EntityCreationService;
import org.hbrs.se2.project.services.ui.CommonUIElementProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class ProfileService implements ProfileServiceInterface {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final MajorRepository majorRepository;
    private final SkillRepository skillRepository;
    private final TopicRepository topicRepository;
    private final ConversationRepository conversationRepository;
    private final EntityCreationService entityCreationService;

    protected final CommonUIElementProvider ui;

    private final PasswordEncoder passwordEncoder;

    public ProfileService(UserRepository userRepository, StudentRepository studentRepository, CompanyRepository companyRepository, MajorRepository majorRepository, SkillRepository skillRepository, TopicRepository topicRepository, ConversationRepository conversationRepository, EntityCreationService entityCreationService, CommonUIElementProvider ui, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
        this.majorRepository = majorRepository;
        this.skillRepository = skillRepository;
        this.topicRepository = topicRepository;
        this.conversationRepository = conversationRepository;
        this.entityCreationService = entityCreationService;
        this.ui = ui;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveStudentData(UserDTO user, StudentDTO student, Set<String> majors, Set<String> topics, Set<String> skills) {
        userRepository.save(entityCreationService.userFactory().createEntity(user));
        studentRepository.save(entityCreationService
                .studentFactory(getMajors(majors), getTopics(topics), getSkills(skills))
                .createEntity(student));
    }

    @Override
    public void saveCompanyData(UserDTO user, CompanyDTO company) {
        userRepository.save(entityCreationService.userFactory().createEntity(user));
        companyRepository.save(entityCreationService.companyFactory().createEntity(company));
    }

    private Set<MajorDTO> getMajors(Set<String> newMajors) {
        Set<MajorDTO> majors = new HashSet<>();
        // get major data from database by major attribute
        newMajors.parallelStream().forEach(newMajor -> Optional.ofNullable(majorRepository.findByMajor(newMajor)).ifPresentOrElse(majors::add, () ->
                majors.add(majorRepository.findByMajor(majorRepository.save(entityCreationService.majorFactory().createEntity(newMajor)).getMajor()))));
        return majors;
    }

    private Set<TopicDTO> getTopics(Set<String> newTopics) {
        Set<TopicDTO> topics = new HashSet<>();
        // get topic data from database by major attribute
        newTopics.parallelStream().forEach(newTopic -> Optional.ofNullable(topicRepository.findByTopic(newTopic)).ifPresentOrElse(topics::add, () ->
                topics.add(topicRepository.findByTopic(topicRepository.save(entityCreationService.topicFactory().createEntity(newTopic)).getTopic()))));
        return topics;
    }

    private Set<SkillDTO> getSkills(Set<String> newSkills) {
        Set<SkillDTO> skills = new HashSet<>();
        // same process as in updateMajors and updateTopics
        newSkills.parallelStream().forEach(newSkill -> Optional.ofNullable(skillRepository.findBySkill(newSkill)).ifPresentOrElse(skills::add, () ->
                skills.add(skillRepository.findBySkill(skillRepository.save(entityCreationService.skillFactory().createEntity(newSkill)).getSkill()))));
        return skills;
    }

    @Override
    public Set<MajorDTO> getMajorOfStudent(int userid) {
        return majorRepository.findByStudentid(studentRepository.findByUserid(userid).getStudentid());
    }

    @Override
    public Set<TopicDTO> getTopicOfStudent(int userid) {
        return topicRepository.findByStudentid(studentRepository.findByUserid(userid).getStudentid());
    }

    @Override
    public Set<SkillDTO> getSkillOfStudent(int userid) {
        return skillRepository.findByStudentid(studentRepository.findByUserid(userid).getStudentid());
    }

    @Override
    @Transactional
    public void removeMajor(int userid, int majorid) {
        studentRepository.save(entityCreationService.studentRemoveMajorFactory(majorid).createEntity(studentRepository.findByUserid(userid)));
        if (!studentRepository.existsMajorRelation(majorid)) {
            majorRepository.deleteById(majorid);
        }
    }

    @Override
    @Transactional
    public void removeTopic(int userid, int topicid) {
        studentRepository.save(entityCreationService.studentRemoveTopicFactory(topicid).createEntity(studentRepository.findByUserid(userid)));
        if (!studentRepository.existsTopicRelation(topicid)) {
            topicRepository.deleteById(topicid);
        }
    }

    @Override
    @Transactional
    public void removeSkill(int userid, int skillid) {
        studentRepository.save(entityCreationService.studentRemoveSkillFactory(skillid).createEntity(studentRepository.findByUserid(userid)));
        if (!studentRepository.existsSkillRelation(skillid)) {
            skillRepository.deleteById(skillid);
        }
    }

    /**
     * @return set of StudentDTOs, where either skills, topics, majors or university match the given keyword.*/
    public Set<StudentDTO> getStudentsMatchingKeyword(String keyword) {
        return studentRepository.findByKeyword(keyword);
    }

    public void deleteUser(UserDTO user) throws DatabaseUserException {
        if (userRepository.deleteByUserid(user.getUserid()) != 1) {
            throw new DatabaseUserException("Wrong amount of datasets deleted");
        }
        conversationRepository.garbageCollection();
    }

    public void changeUserPassword(UserDTO user) throws Exception {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (userRepository.updateUserSetPasswordForUserid(user.getPassword(), user.getUserid()) != 1)
            throw new DatabaseUserException("A Failure occurred while saving a user account in the database at createAccount");
    }
}
