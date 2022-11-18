package org.hbrs.se2.project.services.factory;

import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.JobDTO;
import org.hbrs.se2.project.dtos.StudentDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.entities.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class EntityCreationService {

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

    public AbstractEntityFactory<StudentHasMajor, int[]> shmFactory() {
        return id -> {
            StudentHasMajor shm = new StudentHasMajor();
            shm.setStudentid(id[0]);
            shm.setMajorid(id[1]);
            return  shm;
        };
    }

    public AbstractEntityFactory<StudentHasTopic, int[]> shtFactory() {
        return id -> {
            StudentHasTopic sht = new StudentHasTopic();
            sht.setStudentid(id[0]);
            sht.setTopicid(id[1]);
            return  sht;
        };
    }

    public AbstractEntityFactory<StudentHasSkill, int[]> shsFactory() {
        return id -> {
            StudentHasSkill shs = new StudentHasSkill();
            shs.setStudentid(id[0]);
            shs.setSkillid(id[1]);
            return  shs;
        };
    }

}
