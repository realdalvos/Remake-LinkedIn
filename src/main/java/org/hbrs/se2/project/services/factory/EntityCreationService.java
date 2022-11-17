package org.hbrs.se2.project.services.factory;

import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.JobDTO;
import org.hbrs.se2.project.dtos.StudentDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.entities.Company;
import org.hbrs.se2.project.entities.Job;
import org.hbrs.se2.project.entities.Student;
import org.hbrs.se2.project.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class EntityCreationService {

    ModelMapper mapper = new ModelMapper();

    public AbstractEntityFactory<User> userFactory(UserDTO user) {
        return () -> mapper.map(user, User.class);
    }

    public AbstractEntityFactory<Company> companyFactory(UserDTO user, CompanyDTO company) {
        return () -> {
            Company c = mapper.map(company, Company.class);
            c.setUserid(user.getUserid());
            return c;
        };
    }

    public AbstractEntityFactory<Student> studentFactory(UserDTO user, StudentDTO student) {
        return () -> {
            Student s = mapper.map(student, Student.class);
            s.setUserid(user.getUserid());
            return s;
        };
    }

    public AbstractEntityFactory<Job> jobFactory(JobDTO job) {
        return () -> mapper.map(job, Job.class);
    }

}
