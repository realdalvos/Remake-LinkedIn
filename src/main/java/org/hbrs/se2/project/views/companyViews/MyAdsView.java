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
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Company - My Ads List View
 */
@Route(value = Globals.Pages.MYADS_VIEW, layout = AppView.class)
@PageTitle("Meine Stellen")
public class MyAdsView extends Div {
    @Autowired
    public MyAdsView(JobControl jobcontrol, LoginControl logincontrol){

        Grid<JobDTO> grid = new Grid<>();

        grid.setItems(jobcontrol.getAllCompanyJobs(jobcontrol.getCompanyByUserid(logincontrol.getCurrentUser().getUserid()).getCompanyid()));
        grid.setSelectionMode(Grid.SelectionMode.NONE);

        grid.addColumn(JobDTO::getTitle).setHeader("Titel").setSortable(true);
        grid.addColumn(JobDTO::getDescription).setHeader("Beschreibung");
        grid.addColumn(JobDTO::getSalary).setHeader("Gehalt").setSortable(true);
        grid.addComponentColumn(JobDTO -> {
            Button deleteButton = new Button("Entfernen");
            deleteButton.addClickListener(e -> {jobcontrol.deleteJob(JobDTO.getJobid());
                UI.getCurrent().getPage().reload();});
            return deleteButton;
        });


        // Detail renderer for more information
        grid.setItemDetailsRenderer(new ComponentRenderer<>(jobDTO -> {
            FormLayout layout = new FormLayout();

            // Title field
            final TextField titleField = new TextField("Titel");
            titleField.setValue(jobDTO.getTitle());
            titleField.setReadOnly(true);

            // Description Field (TextArea for more rows)
            final TextArea descriptionField = new TextArea("Beschreibung");
            descriptionField.setValue(jobDTO.getDescription());
            descriptionField.setReadOnly(true);

            // Salary Field
            final TextField salaryField = new TextField("Gehalt");
            salaryField.setValue(jobDTO.getSalary().toString());
            salaryField.setReadOnly(true);

            // Location Field
            final TextField locationField = new TextField("Ort");
            locationField.setValue(jobDTO.getLocation());
            locationField.setReadOnly(true);

            //Add textFields to FormLayout
            layout.add(titleField);
            layout.add(descriptionField);
            layout.add(salaryField);
            layout.add(locationField);

            return layout;
        }));

        add(grid);
    }
}
