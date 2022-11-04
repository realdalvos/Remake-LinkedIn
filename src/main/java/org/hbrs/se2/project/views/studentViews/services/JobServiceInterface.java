package org.hbrs.se2.project.views.studentViews.services;

import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.impl.JobDTOImpl;
import org.hbrs.se2.project.views.studentViews.JobsView;

import java.util.List;

public interface JobServiceInterface {
    public CompanyDTO getCompanyByUserid(int id);
    public void createNewJobPost(JobDTOImpl job);
    public List<JobDTOImpl> getJobsMatchingKeyword(String keyword);
    public List<JobsView.JobDetail> getAllJobsData(List<JobDTOImpl> jobs);
}
