package org.hbrs.se2.project.views.studentViews;

import com.vaadin.flow.component.Key;
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

import java.util.stream.Stream;

/**
 * Student - Jobs list View
 */
@Route(value = Globals.Pages.JOBS_VIEW, layout = AppView.class)
@PageTitle("Jobs")
public class JobsView extends Div {
    // interactive search field
    private final TextField searchField = new TextField(getTranslation("view.job.text.search"));
    private final Button searchButton = new Button(getTranslation("view.job.button.search"));

    // Create a Grid bound to the list
    private final Grid<JobDTO> grid = new Grid<>();

    public JobsView(JobControl jobControl) {
        // Header
        grid.addColumn(JobDTO::getTitle).setHeader(getTranslation("view.job.text.title")).setSortable(true);
        grid.addColumn(JobDTO::getSalary).setHeader(getTranslation("view.job.text.salary")).setSortable(true);
        
        searchField.addKeyPressListener(Key.ENTER, event -> searchButton.clickInClient());
        searchField.addValueChangeListener(x ->this.searchButton.setEnabled(!this.searchField.getValue().isEmpty()));
        searchField.addKeyPressListener(pressEvent ->this.searchButton.setEnabled(!this.searchField.getValue().isEmpty()));
        // pass relevant job list to grid
        searchButton.addClickListener(event -> grid.setItems(jobControl.getJobsMatchingKeyword(searchField.getValue())));
        searchButton.setEnabled(false);
        // set items details renderer
        grid.setItemDetailsRenderer(new ComponentRenderer<>(job -> {
            FormLayout layout = new FormLayout();

            final TextField companyName = new TextField(getTranslation("view.job.text.company"));
            final TextField jobLocation = new TextField(getTranslation("view.job.text.location"));
            final TextField companyContactDetails = new TextField(getTranslation("view.job.text.contactDetails"));
            final TextArea jobDescription = new TextArea(getTranslation("view.job.text.description"));

            // set all fields with job details
            companyName.setValue(jobControl.getCompanyOfJob(job));
            jobLocation.setValue(job.getLocation());
            companyContactDetails.setValue(job.getContactdetails());
            jobDescription.setValue(job.getDescription());

            // add textFields to FormLayout
            Stream.of(companyName, jobLocation, companyContactDetails, jobDescription).forEach(
                    field -> {
                        field.setReadOnly(true);
                        layout.add(field);
                    }
            );
            layout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));
            layout.setColspan(companyContactDetails, 2);
            layout.setColspan(jobDescription, 2);

            return layout;
        }));

        add(searchField);
        add(searchButton);
        grid.setItems(jobControl.getAllJobs());
        add(grid);
        grid.setItems(jobControl.getAllJobs());
    }
}
