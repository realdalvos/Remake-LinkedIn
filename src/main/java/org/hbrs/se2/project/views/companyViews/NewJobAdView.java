package org.hbrs.se2.project.views.companyViews;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.control.JobControl;
import org.hbrs.se2.project.dtos.impl.JobDTOImpl;
import org.hbrs.se2.project.helper.navigateHandler;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.util.Utils;
import org.hbrs.se2.project.views.AppView;

import java.util.Locale;

/**
 * Company - Create new Job Post / Job Ad
 */

@Route(value = Globals.Pages.NEW_ADD_VIEW, layout = AppView.class)
@PageTitle("Joberstellung ")
public class NewJobAdView extends Div {

    /*
    entryDate is an option for now and will be implemented as a comment.
    In order to implement it uncomment all comments with "entryDate" on top.
    In that case formLayout.add(title, description, contactdetails, salary, location, postButton); should be
    removed too
     */

    // Job title text area
    private TextArea title = createTitleArea();
    // Job Description text area
    private TextArea description = createDescriptionArea();
    // Contact details

    // Changed TextField to EmailField to check email addresses
    private EmailField contactdetails = new EmailField("E-Mail");
    // Salary text field
    private IntegerField salary = createSalaryArea();
    // Location text field
    private TextField location = createWorkLocation();
    // post new job button

    /*
    entryDate

    private DatePicker entryDate = createEntryDate();

     */

    private Button postButton = new Button("Anzeige erstellen");

    private Binder<JobDTOImpl> binder = new BeanValidationBinder<>(JobDTOImpl.class);

    public NewJobAdView(JobControl jobControl) {
        setSizeFull();
        H3 newAdText = new H3();
        newAdText.setText("Neue Jobanzeige erstellen");


        // new job ad form
        FormLayout formLayout = new FormLayout();

        /*
        entryData

        formLayout.add(title, description, contactdetails, salary, location, entryDate, postButton);

        (remove formLayout.add(title, description, contactdetails, salary, location, postButton);)
         */

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

    // Adding a character counter to textAreas and textFields

    private TextArea createTitleArea() {
        TextArea title = new TextArea("Titel");
        int charLimitTitle = 75;    // smaller character Limit for title
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

    /*
    entryData

        private DatePicker createEntryDate() {
        DatePicker entryDate = new DatePicker("frühstmöglicher Einstiegsbeginn");
        Locale locale = new Locale("de", "DE");
        entryDate.setLocale(locale);
        return entryDate;
    }

     */

}
