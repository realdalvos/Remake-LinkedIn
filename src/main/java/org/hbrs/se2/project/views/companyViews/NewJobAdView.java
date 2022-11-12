package org.hbrs.se2.project.views.companyViews;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.control.JobControl;
import org.hbrs.se2.project.dtos.impl.JobDTOImpl;
import org.hbrs.se2.project.helper.navigateHandler;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.util.Utils;
import org.hbrs.se2.project.views.AppView;

/**
 * Company - Create new Job Post / Job Ad
 */
@Route(value = Globals.Pages.NEW_ADD_VIEW, layout = AppView.class)
@PageTitle("New Job Ad")
public class NewJobAdView extends Div {

    // Job title text area
    private TextArea title = createTitleArea();
    // Job Description text area
    private TextArea description = createDescriptionArea();
    // Contact details
    private TextField contactdetails = new TextField("E-Mail");
    // Salary text field
    private TextField salary = new TextField("Approximate salary");
    // Location text field
    private TextField location = new TextField("Location");
    // post new job button
    private Button postButton = new Button("Post new Job Ad");

    private Binder<JobDTOImpl> binder = new BeanValidationBinder<>(JobDTOImpl.class);

    public NewJobAdView(JobControl jobControl) {
        setSizeFull();
        H3 newAdText = new H3();
        newAdText.setText("Create a new Job Ad");

        // new job ad form
        FormLayout formLayout = new FormLayout();
        formLayout.add(title, description, contactdetails, salary, location, postButton);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1)
        );

        binder.setBean(new JobDTOImpl(jobControl.getCompanyByUserid(Utils.getCurrentUser().getUserid()).getCompanyid()));
        // map input field values to DTO variables based on chosen names
        binder.bindInstanceFields(this);

        contactdetails.setValue(Utils.getCurrentUser().getEmail());

        postButton.addClickListener(event -> {

            if (binder.isValid()) {
                // call job control to save new job post entity
                jobControl.createNewJobPost(binder.getBean());
            } else {
                Utils.makeDialog("Please fill out all text fields.");
                throw new Error("Not all input field were filled out.");
            }
            navigateHandler.navigateToMyAdsView();
        });

        // add all components to View
        add(newAdText);
        add(formLayout);
        this.setWidth("30%");
    }

    private TextArea createTitleArea() {
        TextArea title = new TextArea("Job Title");
        int charLimitTitle = 100;
        title.setMaxLength(charLimitTitle);
        title.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + charLimitTitle));
        return title;
    }

    private TextArea createDescriptionArea() {
        TextArea description = new TextArea("Job Description");
        int charLimitDescr = 1024;
        description.setMaxLength(charLimitDescr);
        description.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + charLimitDescr));
        return description;
    }
}
