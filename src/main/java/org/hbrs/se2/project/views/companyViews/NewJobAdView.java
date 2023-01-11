package org.hbrs.se2.project.views.companyViews;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
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
import org.hbrs.se2.project.control.UserControl;
import org.hbrs.se2.project.dtos.impl.JobDTOImpl;
import org.hbrs.se2.project.helper.NavigateHandler;
import org.hbrs.se2.project.services.ui.CommonUIElementProvider;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.util.Utils;
import org.hbrs.se2.project.views.AppView;
import org.slf4j.Logger;

/**
 * Company - Create new Job Post / Job Ad
 */

@Route(value = Globals.Pages.NEW_ADD_VIEW, layout = AppView.class, registerAtStartup = false)
@PageTitle("Anzeige erstellen")
public class NewJobAdView extends Div {

    CommonUIElementProvider ui;

    private final Logger logger = Utils.getLogger(this.getClass().getName());

    // Job title text area
    private final TextField title = createTitleField();
    // Job Description text area
    private final TextArea description = createDescriptionArea();
    // Contact details
    private final EmailField contactdetails = createEmailField();
    // Salary text field
    private final IntegerField salary = createSalaryField();
    // Location text field
    private final TextField location = createWorkLocation();
    // post new job button
    private final Button postButton = new Button(getTranslation("view.button.create"));

    private final Binder<JobDTOImpl> binder = new BeanValidationBinder<>(JobDTOImpl.class);

    public NewJobAdView(JobControl jobControl, UserControl userControl, CommonUIElementProvider ui) {
        this.ui = ui;

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setAlignSelf(FlexComponent.Alignment.CENTER);
        verticalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        verticalLayout.add(title, description, contactdetails, salary, location, postButton);

        binder.setBean(new JobDTOImpl(userControl.getCompanyProfile(userControl.getCurrentUser().getUserid()).getCompanyid()));
        // map input field values to DTO variables based on chosen names
        binder.bindInstanceFields(this);

        contactdetails.setValue(userControl.getCurrentUser().getEmail());

        postButton.addClickListener(e -> {
            if (binder.isValid()) {
                ui.makeConfirm("Möchtest du die Jobanzeige so veröffentlichen?", event -> {
                    jobControl.createNewJobPost(binder.getBean());
                    NavigateHandler.navigateToMyAdsView();
                    ui.throwNotification("Jobanzeige erfolgreich veröffentlicht.");
                });
            } else {
                ui.makeDialog("Überprüfe bitte deine Angaben auf Korrektheit.");
                logger.info("Not all fields have been filled in");
            }
        });

        // add an introduction text for further information
        add(ui.introductionText("Hier kannst du eine neue Anzeige anfertigen!",
                "Fülle die unteren Felder aus und bestätige mit dem Button unten, um die Anzeige öffentlich zu machen."));
        // add verticalLayout
        add(verticalLayout);
        this.setWidth("60%");

    }

    // Added character counts, placeholders and delete options

    private TextField createTitleField() {
        TextField titleField = new TextField(getTranslation("view.job.text.title"));
        int charLimitTitle = 75;    // smaller character Limit for title
        titleField.setWidthFull();
        titleField.setMaxLength(charLimitTitle);
        titleField.setValueChangeMode(ValueChangeMode.EAGER); // changing character counter while typing
        titleField.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + charLimitTitle));
        titleField.setPlaceholder(getTranslation("view.job.text.placeholder.title"));
        titleField.setClearButtonVisible(true); // opens oportunity to delete text
        return titleField;
    }

    private TextArea createDescriptionArea() {
        TextArea descriptionArea = new TextArea(getTranslation("view.job.text.description"));
        descriptionArea.getElement().setAttribute("name", "description");
        int charLimitDescr = 1024;
        descriptionArea.setWidthFull();
        descriptionArea.setMaxLength(charLimitDescr);
        descriptionArea.setValueChangeMode(ValueChangeMode.EAGER);
        descriptionArea.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + charLimitDescr));
        descriptionArea.setPlaceholder(getTranslation("view.job.text.placeholder.description"));
        descriptionArea.setClearButtonVisible(true);
        return descriptionArea;
    }

    private EmailField  createEmailField() {
        EmailField email = new EmailField(getTranslation("view.job.text.contactDetails"));
        email.getElement().setAttribute("name", "email");
        email.setPlaceholder(getTranslation("view.job.text.placeholder.contactDetails"));
        email.setMaxLength(100);
        return email;
    }

    private IntegerField  createSalaryField() {
        IntegerField euroField = new IntegerField(getTranslation("view.job.text.salary"));
        euroField.getElement().setAttribute("name", "salary");
        Div euroSuffix = new Div();
        euroSuffix.setText("€");
        euroField.setSuffixComponent(euroSuffix);
        euroField.setClearButtonVisible(true);
        return euroField;
    }

    private TextField createWorkLocation() {
        TextField locationField = new TextField(getTranslation("view.job.text.location"));
        int charLimitDescr = 100;
        locationField.setWidthFull();
        locationField.setMaxLength(charLimitDescr);
        locationField.setValueChangeMode(ValueChangeMode.EAGER);
        locationField.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + charLimitDescr));
        locationField.setPlaceholder(getTranslation("view.job.text.placeholder.location")); // added Placeholder for continuity in job offers
        locationField.setClearButtonVisible(true);
        return locationField;
    }

}
