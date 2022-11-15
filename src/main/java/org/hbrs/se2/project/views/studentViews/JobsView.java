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

import java.util.List;
import java.util.stream.Stream;

/**
 * Student - Jobs list View
 */
@Route(value = Globals.Pages.JOBS_VIEW, layout = AppView.class)
@PageTitle("Jobs")
public class JobsView extends Div {

    // interactive search field
    private TextField searchField = new TextField("Jobsuche");
    private Button searchButton = new Button("Suchen");

    // Create a Grid bound to the list
    private Grid<JobDTO> grid = new Grid<>();

    public JobsView(JobControl jobControl) {

        // Header
        grid.addColumn(JobDTO::getTitle).setHeader("Titel");
        grid.addColumn(JobDTO::getSalary).setHeader("Bezahlung");

        searchField.addKeyPressListener(Key.ENTER, event -> {
            searchButton.click();
        });
        
        searchButton.addClickListener(event -> {
            String keyword = searchField.getValue();
            List<JobDTO> jobs = jobControl.getJobsMatchingKeyword(keyword);

            // pass relevant job list to grid
            grid.setItems(jobs);
        });

        // set items details renderer
        grid.setItemDetailsRenderer(new ComponentRenderer<>(job -> {
            FormLayout layout = new FormLayout();

            final TextField companyName = new TextField("Unternehmen");
            final TextField jobLocation = new TextField("Arbeitsort");
            final TextField companyContactDetails = new TextField("Kontaktdaten");
            final TextArea jobDescription = new TextArea("Beschreibung");

            // set all field with job details
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
        add(grid);
    }

}
