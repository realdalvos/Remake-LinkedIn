package org.hbrs.se2.project.views.companyViews;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.control.JobControl;
import org.hbrs.se2.project.dtos.JobDTO;
import org.hbrs.se2.project.entities.Job;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.util.Utils;
import org.hbrs.se2.project.views.AppView;

/**
 * Company - My Ads List View
 */
@Route(value = Globals.Pages.MYADS_VIEW, layout = AppView.class)
@PageTitle("Meine Stellen")
public class MyAdsView extends Div {

    public MyAdsView(JobControl jobcontrol){

        Grid<JobDTO> grid = new Grid<>();

        grid.setItems(jobcontrol.getAllCompanyJobs(jobcontrol.getCompanyByUserid(Utils.getCurrentUser().getUserid()).getCompanyid()));
        grid.setSelectionMode(Grid.SelectionMode.NONE);

        grid.addColumn(JobDTO::getTitle).setHeader(getTranslation("view.job.text.title")).setSortable(true).setWidth("20%");
        grid.addColumn(JobDTO::getDescription).setHeader(getTranslation("view.job.text.description")).setWidth("30%");
        grid.addColumn(JobDTO::getSalary).setHeader(getTranslation("view.job.text.salary")).setSortable(true).setWidth("15%");
        grid.addColumn(JobDTO::getLocation).setHeader(getTranslation("view.job.text.location")).setWidth("15%");
        grid.addComponentColumn(JobDTO -> {
            Button deleteButton = new Button(getTranslation("view.job.button.delete"));
            deleteButton.addClickListener(e -> {jobcontrol.deleteJob(JobDTO.getJobid());
                UI.getCurrent().getPage().reload();});
            return deleteButton;
        });


        // Detail renderer for more information
        grid.setItemDetailsRenderer(new ComponentRenderer<>(jobDTO -> {
            FormLayout layout = new FormLayout();

            // Title field
            final TextField titleField = new TextField(getTranslation("view.job.text.title"));
            titleField.setValue(jobDTO.getTitle());
            titleField.setReadOnly(true);
            titleField.setMaxWidth("33%");
            titleField.setWidthFull();

            // Description Field (TextArea for more rows)
            final TextArea descriptionField = new TextArea(getTranslation("view.job.text.description"));
            descriptionField.setValue(jobDTO.getDescription());
            descriptionField.setReadOnly(true);
            descriptionField.setMaxWidth("66%");
            descriptionField.setWidthFull();

            // Salary Field
            final TextField salaryField = new TextField(getTranslation("view.job.text.salary"));
            salaryField.setValue(jobDTO.getSalary().toString());
            salaryField.setReadOnly(true);
            salaryField.setMaxWidth("33%");
            salaryField.setWidthFull();

            // Location Field
            final TextField locationField = new TextField(getTranslation("view.job.text.location"));
            locationField.setValue(jobDTO.getLocation());
            locationField.setReadOnly(true);
            locationField.setMaxWidth("33%");
            locationField.setWidthFull();

            // Contactdetails Field
            final TextField contactdetailsField = new TextField(getTranslation("view.job.text.contactDetails"));
            contactdetailsField.setValue(jobDTO.getContactdetails());
            contactdetailsField.setReadOnly(true);
            contactdetailsField.setMaxWidth("33%");
            contactdetailsField.setWidthFull();

            //Add textFields to FormLayout
            layout.add(titleField);
            layout.add(salaryField);
            layout.add(locationField);
            layout.add(descriptionField);
            layout.add(contactdetailsField);
            layout.setSizeFull();
            layout.setHeightFull();
            layout.setWidthFull();
            layout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 3));
            layout.setColspan(descriptionField,2);
            return layout;
        }));
        grid.setHeight("100%");
        H3 title = new H3(" Übersicht über ihre aktuellen Stellenausschreibungen");
        title.getElement().getStyle().set("color", "#f2a6b4");
        title.getElement().getStyle().set("text-align", "center");
        add(title);
        add(grid);
        setHeight("100%");
    }
}
