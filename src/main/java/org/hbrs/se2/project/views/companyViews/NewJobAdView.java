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
import com.vaadin.flow.data.value.ValueChangeMode;
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
@PageTitle("Joberstellung ")
public class NewJobAdView extends Div {

    // Job title text area
    private TextArea title = createTitleArea();
    // Job Description text area
    private TextArea description = createDescriptionArea();
    // Contact details
    private TextField contactdetails = new TextField("E-Mail");
    // Salary text field
    private IntegerField salary = createSalaryArea();
    // Location text field
    private TextField location = createWorkLocation();
    // post new job button
    private Button postButton = new Button("Anzeige erstellen");

    private Binder<JobDTOImpl> binder = new BeanValidationBinder<>(JobDTOImpl.class);

    public NewJobAdView(JobControl jobControl) {
        setSizeFull();
        H3 newAdText = new H3();
        newAdText.setText("Neue Jobanzeige erstellen");

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
                Utils.makeDialog("Fülle bitte alle Felder aus");
                throw new Error("Nicht alle Felder wurden ausgefüllt");
            }
            navigateHandler.navigateToMyAdsView();
        });

        // add all components to View
        add(newAdText);
        add(formLayout);
        this.setWidth("60%");
    }

    // Adding a character counter

    private TextArea createTitleArea() {
        TextArea title = new TextArea("Titel");
        int charLimitTitle = 25;    // smaller character Limit for title
        title.setWidthFull();
        title.setMaxLength(charLimitTitle);
        title.setValueChangeMode(ValueChangeMode.EAGER); // changing character counter while typing
        title.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + charLimitTitle));
        return title;
    }

    private TextArea createDescriptionArea() {
        TextArea description = new TextArea("Beschreibung");
        int charLimitDescr = 1024;
        description.setWidthFull();
        description.setMaxLength(charLimitDescr);
        description.setValueChangeMode(ValueChangeMode.EAGER);
        description.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + charLimitDescr));
        return description;
    }

    private IntegerField  createSalaryArea() {
        IntegerField euroField = new IntegerField ();
        euroField.setLabel("Jahresgehalt");
        Div euroSuffix = new Div();
        euroSuffix.setText("€");
        euroField.setSuffixComponent(euroSuffix);
        return euroField;
    }

    private TextField createWorkLocation() {
        TextField location = new TextField("Arbeitsort");
        int charLimitDescr = 50;
        location.setWidthFull();
        location.setMaxLength(charLimitDescr);
        location.setValueChangeMode(ValueChangeMode.EAGER);
        location.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + charLimitDescr));
        return location;
    }


}
