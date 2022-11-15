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

import java.util.List;
import java.util.stream.Stream;

/**
 * Student - Jobs list View
 */
@Route(value = Globals.Pages.JOBS_VIEW, layout = AppView.class)
@PageTitle("Jobs")
public class JobsView extends Div {

    private JobControl jobControl;

    // Grid components
    private TextField textField = new TextField("Jobsuche");
    private Button button = new Button("Suchen");

    // Create a Grid bound to the list
    private Grid<JobDTO> grid = new Grid<>();

    public JobsView(JobControl jobControl) {
        this.jobControl = jobControl;
        setSizeFull();

        // Header
        grid.addColumn(JobDTO::getTitle).setHeader("Titel");
        grid.addColumn(JobDTO::getSalary).setHeader("Bezahlung");
        // set items details renderer
        grid.setItemDetailsRenderer(createJobDetailsRenderer());

        button.addClickListener(event -> {
            String keyword = textField.getValue();
            List<JobDTO> jobs = jobControl.getJobsMatchingKeyword(keyword);

            // pass relevant job list with detail information to grid
            grid.setItems(jobs);
        });

        add(textField);
        add(button);
        add(grid);
    }

    private ComponentRenderer<JobDetailsFormLayout, JobDTO> createJobDetailsRenderer() {
        return new ComponentRenderer<>(
                JobDetailsFormLayout::new, JobDetailsFormLayout::setJobDetails);
    }

    private class JobDetailsFormLayout extends FormLayout {

        // Grid Detail View Components
        private final TextField companyName = new TextField("Unternehmen");
        private final TextField jobLocation = new TextField("Arbeitsort");
        private final TextField companyContactDetails = new TextField("Kontaktdaten");
        private final TextArea jobDescription = new TextArea("Beschreibung");

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

        public void setJobDetails(JobDTO job) {
            // set all field with job details
            companyName.setValue(jobControl.getCompanyOfJob(job));
            jobLocation.setValue(job.getLocation());
            companyContactDetails.setValue(job.getContactdetails());
            jobDescription.setValue(job.getDescription());
        }
    }

}
