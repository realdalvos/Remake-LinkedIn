package org.hbrs.se2.project.views.companyViews;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.control.JobControl;
import org.hbrs.se2.project.control.LoginControl;
import org.hbrs.se2.project.dtos.impl.JobDTOImpl;
import org.hbrs.se2.project.helper.navigateHandler;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.util.Utils;
import org.hbrs.se2.project.views.AppView;
import org.slf4j.Logger;

/**
 * Company - Create new Job Post / Job Ad
 */
@Route(value = Globals.Pages.NEW_ADD_VIEW, layout = AppView.class)
@PageTitle("Anzeige erstellen")
public class NewJobAdView extends Div {
    private final Logger logger = Utils.getLogger(this.getClass().getName());

    // Job title text area
    private TextField title = createTitleField();
    // Job Description text area
    private TextArea description = createDescriptionArea();
    // Contact details
    private TextField contactdetails = createEmailField();
    // Salary text field
    private IntegerField salary = createSalaryField();
    // Location text field
    private TextField location = new TextField("Arbeitsort");
    // post new job button
    private Button postButton = new Button("Anzeige erstellen");

    private Binder<JobDTOImpl> binder = new BeanValidationBinder<>(JobDTOImpl.class);

    public NewJobAdView(JobControl jobControl, LoginControl loginControl) {
        setSizeFull();
        H3 newAdText = new H3();
        newAdText.setText("Neue Jobanzeige erstellen");

        // new job ad form
        FormLayout formLayout = new FormLayout();
        formLayout.add(title, description, contactdetails, salary, location, postButton);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1)
        );

        binder.setBean(new JobDTOImpl(jobControl.getCompanyByUserid(loginControl.getCurrentUser().getUserid()).getCompanyid()));
        // map input field values to DTO variables based on chosen names
        binder.bindInstanceFields(this);

        contactdetails.setValue(loginControl.getCurrentUser().getEmail());

        postButton.addClickListener(event -> {

            if (binder.isValid()) {
                // call job control to save new job post entity
                jobControl.createNewJobPost(binder.getBean());
                navigateHandler.navigateToMyAdsView();
            } else {
                Utils.makeDialog("Fülle bitte alle Felder aus");
                logger.info("Not all fields have been filled in");
            }
        });

        // add all components to View
        add(newAdText);
        add(formLayout);
        this.setWidth("30%");
    }

    private TextField createTitleField() {
        TextField title = new TextField("Titel");
        title.getElement().setAttribute("name", "title");
        int charLimitTitle = 100;
        title.setMaxLength(charLimitTitle);
        title.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + charLimitTitle));
        return title;
    }

    private TextArea createDescriptionArea() {
        TextArea description = new TextArea("Beschreibung");
        description.getElement().setAttribute("name", "description");
        int charLimitDescr = 1024;
        description.setMaxLength(charLimitDescr);
        description.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + charLimitDescr));
        return description;
    }

    private TextField  createEmailField() {
        TextField email = new TextField("Kontaktdaten");
        email.getElement().setAttribute("name", "email");
        email.setPlaceholder("username@example.com");
        return email;
    }

    private IntegerField  createSalaryField() {
        IntegerField salary = new IntegerField("Jahresgehalt");
        salary.getElement().setAttribute("name", "salary");
        Div euroSuffix = new Div();
        euroSuffix.setText("€");
        salary.setSuffixComponent(euroSuffix);
        return salary;
    }
}
