package org.hbrs.se2.project.control;

import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.JobDTO;
import org.hbrs.se2.project.views.studentViews.JobsView;
import org.hbrs.se2.project.views.studentViews.services.impl.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class JobControl {

    @Autowired
    JobService jobService;

    public CompanyDTO getCompanyByUserid(int id) {
        return jobService.getCompanyByUserid(id);
    }

    public void createNewJobPost(JobDTO job) {
        jobService.createNewJobPost(job);
    }

    public List<JobDTO> getJobsMatchingKeyword(String keyword) {
        return jobService.getJobsMatchingKeyword(keyword);
    }



    public List<JobsView.JobDetail> getAllJobsData(List<JobDTO> jobs) {
        return jobService.getAllJobsData(jobs);
    }
}

