package org.hbrs.se2.project.services.factory;

import org.hbrs.se2.project.dtos.*;
import org.hbrs.se2.project.entities.*;
import org.hbrs.se2.project.repository.MajorRepository;
import org.hbrs.se2.project.repository.SkillRepository;
import org.hbrs.se2.project.repository.StudentRepository;
import org.hbrs.se2.project.repository.TopicRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class EntityCreationService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private MajorRepository majorRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private TopicRepository topicRepository;

    private ModelMapper mapper = new ModelMapper();

    public AbstractEntityFactory<User, UserDTO> userFactory() {
        return user -> mapper.map(user, User.class);
    }

    public AbstractEntityFactory<Company, CompanyDTO> companyFactory() {
        return company -> mapper.map(company, Company.class);
    }

    public AbstractEntityFactory<Student, StudentDTO> studentFactory() {
        return student -> mapper.map(student, Student.class);
    }

    public AbstractEntityFactory<Student, StudentDTO> studentFactory(Set<MajorDTO> majors, Set<TopicDTO> topics, Set<SkillDTO> skills) {
        return student -> {
            Student s = studentRepository.findById(student.getStudentid()).get();
            mapper.map(student, s);
            majors.forEach(m -> s.addMajor(mapper.map(m, Major.class)));
            topics.forEach(t -> s.addTopic(mapper.map(t, Topic.class)));
            skills.forEach(sk -> s.addSkill(mapper.map(sk, Skill.class)));
            return s;
        };
    }

    public AbstractEntityFactory<Student, StudentDTO> studentRemoveMajorFactory(int majorid) {
        return student -> {
            Student s = studentRepository.findById(student.getStudentid()).get();
            s.removeMajor(majorRepository.getReferenceById(majorid));
            return s;
        };
    }

    public AbstractEntityFactory<Student, StudentDTO> studentRemoveTopicFactory(int topicid) {
        return student -> {
            Student s = studentRepository.findById(student.getStudentid()).get();
            s.removeTopic(topicRepository.getReferenceById(topicid));
            return s;
        };
    }

    public AbstractEntityFactory<Student, StudentDTO> studentRemoveSkillFactory(int skillid) {
        return student -> {
            Student s = studentRepository.findById(student.getStudentid()).get();
            s.removeSkill(skillRepository.getReferenceById(skillid));
            return s;
        };
    }

    public AbstractEntityFactory<Job, JobDTO> jobFactory() {
        return job -> mapper.map(job, Job.class);
    }

    public AbstractEntityFactory<Major, String> majorFactory() {
        return major -> {
            Major m = new Major();
            m.setMajor(major);
            return  m;
        };
    }

    public AbstractEntityFactory<Topic, String> topicFactory() {
        return topic -> {
            Topic t = new Topic();
            t.setTopic(topic);
            return  t;
        };
    }

    public AbstractEntityFactory<Skill, String> skillFactory() {
        return skill -> {
            Skill s = new Skill();
            s.setSkill(skill);
            return  s;
        };
    }

}
