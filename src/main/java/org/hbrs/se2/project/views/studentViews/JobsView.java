package org.hbrs.se2.project.views.studentViews;


import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
    Label emptyLabel = new Label();
    // interactive search field
    private final TextField searchField = new TextField(getTranslation("view.job.text.search"));
    private final Button searchButton = new Button(getTranslation("view.job.button.search"));
    private final Button buttonAllJobs = new Button("Alle Jobs");
    // Create a Grid bound to the list
    private final Grid<JobDTO> grid = new Grid<>();

    public JobsView(JobControl jobControl) {
        HorizontalLayout layout = new HorizontalLayout();

        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        searchField.setClearButtonVisible(true); // possibility to delete filter words

        // changing width of textField, buttonFilter and buttonAllJobs to improve on usability
        searchField.setWidth("25");
        searchButton.setWidth("25%");
        buttonAllJobs.setWidth("25%");

        HorizontalLayout topLayout = new HorizontalLayout();

        searchField.getStyle().set("margin-center", "auto");
        searchButton.getStyle().set("margin-center", "auto");
        buttonAllJobs.getStyle().set("margin-center", "auto");

        // Center Alignment
        topLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        topLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        // adding TextField and 2 buttons
        topLayout.add(searchField);
        topLayout.add(searchButton);
        topLayout.add(buttonAllJobs);

        layout.add(topLayout);
        layout.add(emptyLabel);
        layout.setWidth("100%");

        // Header
        grid.addColumn(JobDTO::getTitle).setHeader(getTranslation("view.job.text.title")).setSortable(true).setTextAlign(ColumnTextAlign.CENTER);
        grid.addColumn(JobDTO::getSalary).setHeader(getTranslation("view.job.text.salary")).setSortable(true);
        grid.setHeightByRows(true);
        searchField.addKeyPressListener(Key.ENTER, event -> searchButton.clickInClient());
        // pass relevant job list to grid
        searchButton.addClickListener(event -> grid.setItems(jobControl.getJobsMatchingKeyword(searchField.getValue())));
        // set items details renderer
        grid.setItemDetailsRenderer(new ComponentRenderer<>(job -> {
            FormLayout formLayout = new FormLayout();

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
                        formLayout.add(field);
                    }
            );
            formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));
            formLayout.setColspan(companyContactDetails, 2);
            formLayout.setColspan(jobDescription, 2);

            return formLayout;
        }));

        grid.setItems(jobControl.getAllJobs());

        // buttonAllJobs will show all Job Ads without any filter

        buttonAllJobs.addClickListener(event -> {
            List<JobDTO> jobDetails = jobControl.getAllJobs();
            grid.setItems(jobDetails);
        });

        // Adding column Borders and Row Stripes for better visibility
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        /*
        Adding a text with the number of available Jobs underneath the grid
        To achieve that I used a workaround instead of working with the size of the grid
        */

        List<JobDTO> jobDetails = jobControl.getAllJobs();

        int jobCount = jobDetails.size();
        String s = "Es sind momentan " + jobCount + " Jobs für Sie verfügbar! ";
        H2 jobCountText = new H2(s);

        jobCountText.getElement().getStyle().set("font-size","15px");
        jobCountText.getElement().getStyle().set("text-align","center");

        // adding text, topLayout, grid and the text for showing the total number of jobs available

        add(introductionText());
        add(topLayout);
        add(grid);
        add(jobCountText);
    }

    /*
    with introductionText() below the career page will show information and help the user
    guide through the option of either filtering the job ads or showing all available job ads
    */
    private VerticalLayout introductionText() {
        VerticalLayout vLayout = new VerticalLayout();
        H2 startText = new H2("Willkommen auf Ihrer Karriereseite!" );

        H3 descriptionText = new H3("Sie können nach Jobs filtern oder sich direkt alle anzeigen lassen! ");

        vLayout.setSizeFull();
        vLayout.setPadding(false);
        vLayout.setSpacing(true);
        vLayout.getThemeList().set("spacing-s", true);
        vLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        startText.getElement().getStyle().set("font-size","45px"); // font size
        startText.getElement().getStyle().set("color", "#f2a6b4"); // hex value of color in custom Theme for continuity
        startText.getElement().getStyle().set("text-align","center"); // text is now in center

        descriptionText.getElement().getStyle().set("font-size","20px");
        descriptionText.getElement().getStyle().set("text-align","center");

        vLayout.add(startText);
        vLayout.add(descriptionText);
        vLayout.add(emptyLabel);

        return vLayout;
    }
}
