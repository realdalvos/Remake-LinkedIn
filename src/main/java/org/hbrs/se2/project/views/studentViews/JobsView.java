package org.hbrs.se2.project.views.studentViews;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.control.JobControl;
import org.hbrs.se2.project.dtos.JobDTO;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.views.AppView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

/**
 * Student - Jobs list View
 */
@Route(value = Globals.Pages.JOBS_VIEW, layout = AppView.class)
@PageTitle("Jobs")
public class JobsView extends Div {

    @Autowired
    private JobControl jobControl;

    public JobsView() {
        setSizeFull();

        // Grid components
        TextField textField = new TextField("Search Jobs");
        Button button = new Button("Search");

        // Create a Grid bound to the list
        Grid<JobDetail> grid = new Grid<>();
        // Header
        grid.addColumn(JobDetail::getTitle).setHeader("Title");
        grid.addColumn(JobDetail::getSalary).setHeader("Salary");
        // set items details renderer
        grid.setItemDetailsRenderer(createJobDetailsRenderer());

        button.addClickListener(event -> {
            String keyword = textField.getValue();
            List<JobDTO> jobs = jobControl.getJobsMatchingKeyword(keyword);

            // gather all important data !!!!!!!
            // user email, company name, job description
            List<JobDetail> jobDetails = jobControl.getAllJobsData(jobs);
            // pass relevant job list with detail information to grid
            grid.setItems(jobDetails);
        });

        add(textField);
        add(button);
        add(grid);
    }

    private static ComponentRenderer<JobDetailsFormLayout, JobDetail> createJobDetailsRenderer() {
        return new ComponentRenderer<>(
                JobDetailsFormLayout::new, JobDetailsFormLayout::setJobDetails);
    }

    private static class JobDetailsFormLayout extends FormLayout {

        // Grid Detail View Components
        private final TextField companyName = new TextField("Company");

        private final TextField jobLocation = new TextField("Location");
        private final TextField companyContactDetails = new TextField("Contact Details");
        private final TextArea jobDescription = new TextArea("Job Description");

        public JobDetailsFormLayout() {
            Stream.of(companyName, jobLocation, companyContactDetails, jobDescription).forEach(
                    field -> {
                        field.setReadOnly(true);
                        add(field);
                    }
            );

            setResponsiveSteps(new ResponsiveStep("0", 2));
            setColspan(companyContactDetails, 2);
            setColspan(jobDescription, 2);
        }

        public void setJobDetails(JobDetail details) {
            // set all field with job details
            companyName.setValue(details.getName());
            jobLocation.setValue(details.getLocation());
            companyContactDetails.setValue(details.getContactdetails());
            jobDescription.setValue(details.getDescription());
        }
    }

    public static class JobDetail {
        private String title;
        private String salary;
        private String description;
        private String location;
        private String name;
        private String contactdetails;

        public JobDetail(String title, String salary, String description, String location, String name, String contactdetails) {
            this.title = title;
            this.salary = salary;
            this.description = description;
            this.location = location;
            this.name = name;
            this.contactdetails = contactdetails;
        }

        public String getTitle() {
            return title;
        }

        public String getSalary() {
            return salary;
        }

        public String getDescription() {
            return description;
        }
        public String getLocation() { return location; }

        public String getName() {
            return name;
        }

        public String getContactdetails() {
            return contactdetails;
        }
    }
}
