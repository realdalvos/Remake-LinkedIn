package org.hbrs.se2.project.control;

import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.entities.Job;
import org.hbrs.se2.project.repository.CompanyRepository;
import org.hbrs.se2.project.repository.JobRepository;
import org.hbrs.se2.project.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobControl {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JobRepository jobRepository;

    public CompanyDTO getCompanyByUserid(int id) {
        return companyRepository.findCompanyByUserid(id);
    }

    public void createNewJobPost(Job job) {
        this.jobRepository.save(job);
    }

    // check if input data from job post form is empty or not
    public boolean checkFormJobInput(String title, String description, String salary) {
        String[] array = {title, description, salary};
        return Utils.checkIfInputEmpty(array);
    }
}

