package org.hbrs.se2.project.services.impl;

import org.hbrs.se2.project.dtos.*;
import org.hbrs.se2.project.repository.*;
import org.hbrs.se2.project.services.ProfileServiceInterface;
import org.hbrs.se2.project.services.factory.EntityCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ProfileService implements ProfileServiceInterface {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private MajorRepository majorRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private EntityCreationService entityCreationService;

    @Override
    public void saveStudentData(UserDTO user, StudentDTO student, Set<String> major, Set<String> topic, Set<String> skill) {
        userRepository.save(entityCreationService.userFactory().createEntity(user));
        studentRepository.save(entityCreationService
                .studentFactory(getMajors(major, student.getStudentid()), getTopics(topic, student.getStudentid()), getSkills(skill, student.getStudentid()))
                .createEntity(student));
    }

    @Override
    public void saveCompanyData(UserDTO user, CompanyDTO company) {
        userRepository.save(entityCreationService.userFactory().createEntity(user));
        companyRepository.save(entityCreationService.companyFactory().createEntity(company));
    }

    private Set<MajorDTO> getMajors(Set<String> major, int studentid) {
        Set<MajorDTO> majors = new HashSet<>();
        // get major data from database by major attribute
        major.parallelStream().forEach(m -> {
            MajorDTO majorDTO = majorRepository.findByMajor(m);
            // check if found major is null
            if (majorDTO == null) {
                // if major does not exist, create new major from input
                majorRepository.save(entityCreationService.majorFactory().createEntity(m));
                // get saved major data
                majors.add(majorRepository.findByMajor(m));
            } else {
                majors.add(majorDTO);
            }
        });
        return majors;
    }

    private Set<TopicDTO> getTopics(Set<String> topic, int studentid) {
        Set<TopicDTO> topics = new HashSet<>();
        // get topic data from database by major attribute
        topic.parallelStream().forEach(t -> {
            TopicDTO topicDTO = topicRepository.findByTopic(t);
            // check if found topic is null
            if (topicDTO == null) {
                // if topic does not exist, create new topic from input
                topicRepository.save(entityCreationService.topicFactory().createEntity(t));
                // get topic major data
                topics.add(topicRepository.findByTopic(t));
            } else {
                topics.add(topicDTO);
            }
        });
        return topics;
    }

    private Set<SkillDTO> getSkills(Set<String> skill, int studentid) {
        Set<SkillDTO> skills = new HashSet<>();
        // same process as in updateMajors and updateTopics
        skill.parallelStream().forEach(s -> {
            SkillDTO skillDTO = skillRepository.findBySkill(s);
            if (skillDTO == null) {
                skillRepository.save(entityCreationService.skillFactory().createEntity(s));
                skills.add(skillRepository.findBySkill(s));
            } else {
                skills.add(skillDTO);
            }
        });
        return skills;
    }

    @Override
    public StudentDTO getStudentProfile(int userid) {
        return studentRepository.findByUserid(userid);
    }

    @Override
    public CompanyDTO getCompanyProfile(int userid) {
        return companyRepository.findByUserid(userid);
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
    public void removeMajor(int userid, MajorDTO major) {
        studentRepository.save(entityCreationService.studentRemoveMajor(studentRepository.findByUserid(userid), major));
        if (!studentRepository.existsMajorRelation(major.getMajorid())) {
            majorRepository.deleteById(major.getMajorid());
        }
    }

    @Override
    public void removeTopic(int userid, TopicDTO topic) {
        studentRepository.save(entityCreationService.studentRemoveTopic(studentRepository.findByUserid(userid), topic));
        if (!studentRepository.existsTopicRelation(topic.getTopicid())) {
            topicRepository.deleteById(topic.getTopicid());
        }
    }

    @Override
    public void removeSkill(int userid, SkillDTO skill) {
        studentRepository.save(entityCreationService.studentRemoveSkill(studentRepository.findByUserid(userid), skill));
        if (!studentRepository.existsSkillRelation(skill.getSkillid())) {
            skillRepository.deleteById(skill.getSkillid());
        }
    }

    public Set<StudentDTO> getStudentsMatchingKeyword(String keyword) {
        return studentRepository.findByKeyword(keyword);
    }

    public UserDTO getUserByUserid(int userid) {
        return userRepository.findByUserid(userid);
    }

}
