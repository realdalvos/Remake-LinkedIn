package org.hbrs.se2.project.views.companyViews;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
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

    /*
    entryDate is an option for now and will be implemented as a comment.
    In order to implement it uncomment all comments with "entryDate" on top.
    In that case formLayout.add(title, description, contactdetails, salary, location, postButton); should be
    removed too
     */

    // Job title text area
    private TextField title = createTitleField();
    // Job Description text area
    private TextArea description = createDescriptionArea();
    // Contact details

    // Changed TextField to EmailField to check email addresses
    private EmailField contactdetails = createEmailField();
    // Salary text field
    private IntegerField salary = createSalaryField();
    // Location text field
    private TextField location = createWorkLocation();
    // post new job button

    /*
    entryDate

    private DatePicker entryDate = createEntryDate();

     */

    private Button postButton = new Button(getTranslation("view.newJobAd.button"));

    private Binder<JobDTOImpl> binder = new BeanValidationBinder<>(JobDTOImpl.class);

    public NewJobAdView(JobControl jobControl, LoginControl loginControl) {

        setHeightFull();
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setAlignSelf(FlexComponent.Alignment.CENTER);
        verticalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        H3 newJob = new H3();
        newJob.setText(getTranslation("view.newJobAd.h3"));
        newJob.getElement().getStyle().set("font-size","30px");
        newJob.getElement().getStyle().set("text-align","center"); // content centered instead of being stuck on the side
        Icon createJobAdIcon = new Icon(VaadinIcon.EDIT);
        newJob.getElement().appendChild(createJobAdIcon.getElement()); // Added Icon


        /*
        entryData

        formLayout.add(title, description, contactdetails, salary, location, entryDate, postButton);

        (remove formLayout.add(title, description, contactdetails, salary, location, postButton);)
         */

        verticalLayout.add(title, description, contactdetails, salary, location, postButton);

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
                Utils.makeDialog(getTranslation("view.newJobAd.error"));
                logger.info("Not all fields have been filled in");
            }
        });

        // add all components to View
        add(newJob);
        add(verticalLayout);
        this.setWidth("60%");

    }

    // Added character counts, placeholders and delete options

    private TextField createTitleField() {
        TextField title = new TextField(getTranslation("view.newJobAd.form.title"));
        int charLimitTitle = 75;    // smaller character Limit for title
        title.setWidthFull();
        title.setMaxLength(charLimitTitle);
        title.setValueChangeMode(ValueChangeMode.EAGER); // changing character counter while typing
        title.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + charLimitTitle));
        title.setPlaceholder(getTranslation("view.newJobAd.form.title.placeholder"));
        title.setClearButtonVisible(true); // opens oportunity to delete text
        return title;
    }

    private TextArea createDescriptionArea() {
        TextArea description = new TextArea(getTranslation("view.newJobAd.form.description"));
        description.getElement().setAttribute("name", "description");
        int charLimitDescr = 1024;
        description.setWidthFull();
        description.setMaxLength(charLimitDescr);
        description.setValueChangeMode(ValueChangeMode.EAGER);
        description.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + charLimitDescr));
        description.setPlaceholder(getTranslation("view.newJobAd.form.description.placeholder"));
        description.setClearButtonVisible(true);
        return description;
    }

    private EmailField  createEmailField() {
        EmailField email = new EmailField(getTranslation("view.newJobAd.form.contactDetails"));
        email.getElement().setAttribute("name", "email");
        email.setPlaceholder(getTranslation("view.newJobAd.form.contactDetails.placeholder"));
        return email;
    }

    private IntegerField  createSalaryField() {
        IntegerField euroField = new IntegerField(getTranslation("view.newJobAd.form.salary"));
        euroField.getElement().setAttribute("name", "salary");
        Div euroSuffix = new Div();
        euroSuffix.setText("€");
        euroField.setSuffixComponent(euroSuffix);
        euroField.setClearButtonVisible(true);
        return euroField;
    }

    private TextField createWorkLocation() {
        TextField location = new TextField(getTranslation("view.newJobAd.form.location"));
        int charLimitDescr = 100;
        location.setWidthFull();
        location.setMaxLength(charLimitDescr);
        location.setValueChangeMode(ValueChangeMode.EAGER);
        location.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + charLimitDescr));
        location.setPlaceholder(getTranslation("view.newJobAd.form.location.placeholder")); // added Placeholder for continuity in job offers
        location.setClearButtonVisible(true);
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
