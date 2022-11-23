package org.hbrs.se2.project.views.companyViews;

import com.vaadin.flow.component.UI;
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
import org.hbrs.se2.project.control.LoginControl;
import org.hbrs.se2.project.dtos.JobDTO;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.views.AppView;

/**
 * Company - My Ads List View
 */
@Route(value = Globals.Pages.MYADS_VIEW, layout = AppView.class)
@PageTitle("Meine Stellen")
public class MyAdsView extends Div {

    public MyAdsView(JobControl jobcontrol, LoginControl logincontrol){

        Grid<JobDTO> grid = new Grid<>();

        grid.setItems(jobcontrol.getAllCompanyJobs(jobcontrol.getCompanyByUserid(logincontrol.getCurrentUser().getUserid()).getCompanyid()));
        grid.setSelectionMode(Grid.SelectionMode.NONE);

        grid.addColumn(JobDTO::getTitle).setHeader(getTranslation("view.job.text.title")).setSortable(true);
        grid.addColumn(JobDTO::getDescription).setHeader(getTranslation("view.job.text.description"));
        grid.addColumn(JobDTO::getSalary).setHeader(getTranslation("view.job.text.salary")).setSortable(true);
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

            // Description Field (TextArea for more rows)
            final TextArea descriptionField = new TextArea(getTranslation("view.job.text.description"));
            descriptionField.setValue(jobDTO.getDescription());
            descriptionField.setReadOnly(true);

            // Salary Field
            final TextField salaryField = new TextField(getTranslation("view.job.text.salary"));
            salaryField.setValue(jobDTO.getSalary().toString());
            salaryField.setReadOnly(true);

            // Location Field
            final TextField locationField = new TextField(getTranslation("view.job.text.location"));
            locationField.setValue(jobDTO.getLocation());
            locationField.setReadOnly(true);

            final TextField contactdetailsField = new TextField(getTranslation("view.job.text.contactDetails"));
            contactdetailsField.setValue(jobDTO.getContactdetails());
            contactdetailsField.setReadOnly(true);

            //Add textFields to FormLayout
            layout.add(titleField);
            layout.add(descriptionField);
            layout.add(salaryField);
            layout.add(locationField);
            layout.add(contactdetailsField);

            return layout;
        }));

        add(grid);
    }
}
