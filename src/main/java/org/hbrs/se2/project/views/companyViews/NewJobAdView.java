package org.hbrs.se2.project.views.companyViews;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.control.JobControl;
import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.entities.Job;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.views.AppView;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Company - Create new Job Post / Job Ad
 */
@Route(value = Globals.Pages.NEW_ADD_VIEW, layout = AppView.class)
@PageTitle("New Job Ad")
public class NewJobAdView extends Div {

    @Autowired
    private JobControl jobControl;

    private H3 newAdText = new H3();

    public NewJobAdView() throws DatabaseUserException {
        setSizeFull();
        newAdText.setText("Create a new Job Ad");

        // Job title text area
        TextArea title = new TextArea("Job Title");
        int charLimitTitle = 100;
        title.setMaxLength(charLimitTitle);
        title.addValueChangeListener(e -> {
            e.getSource().setHelperText(e.getValue().length() + "/" + charLimitTitle);
        });

        // Job Description text area
        TextArea description = new TextArea("Job Description");
        int charLimitDescr = 1024;
        description.setMaxLength(charLimitDescr);
        description.addValueChangeListener(e -> {
            e.getSource().setHelperText(e.getValue().length() + "/" + charLimitDescr);
        });

        // Salary text field
        TextField salary = new TextField("Approximate salary");

        // post new job button
        Button postButton = new Button("Post new Job Ad");

        // new job ad form
        FormLayout formLayout = new FormLayout();
        formLayout.add(title, description, salary, postButton);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1)
        );

        postButton.addClickListener(event -> {
            // get companyid from current user
            UserDTO currentUser = this.getCurrentUser();
            CompanyDTO comp = jobControl.getCompanyByUserid(currentUser.getUserid());
            int companyid = comp.getCompanyid();

            // create new job entity
            Job job = new Job();
            job.setCompanyid(companyid);
            job.setTitle(title.getValue());
            job.setDescription(description.getValue());
            job.setSalary(salary.getValue());

            // check if all input fields were filled out
            if(!jobControl.checkFormJobInput(job.getTitle(), job.getDescription(), job.getSalary())) {
                // error dialog
                Dialog dialog = new Dialog();
                dialog.add(new Text("Please fill out all text fields."));
                // close button
                Button closeb = new Button("Close");
                closeb.addClickListener(e -> dialog.close());
                dialog.add(closeb);
                dialog.setWidth("400px");
                dialog.setHeight("150px");
                dialog.open();
                throw new Error("Not all input field were filled out.");
            }

            // call job control to save new job post entity
            jobControl.createNewJobPost(job);

            this.navigateToMyAdsView();
        });

        // add all components to View
        add(newAdText);
        add(formLayout);
        this.setWidth("30%");
    }

    private UserDTO getCurrentUser() {
        return (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
    }

    private void navigateToMyAdsView() {
        UI.getCurrent().navigate(Globals.Pages.MYADS_VIEW);
    }
}
