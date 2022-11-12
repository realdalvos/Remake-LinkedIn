package org.hbrs.se2.project.views.companyViews;

import com.vaadin.flow.component.UI;
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
import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.dtos.impl.JobDTOImpl;
import org.hbrs.se2.project.helper.navigateHandler;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.util.Utils;
import org.hbrs.se2.project.views.AppView;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Company - Create new Job Post / Job Ad
 */
@Route(value = Globals.Pages.NEW_ADD_VIEW, layout = AppView.class)
@PageTitle("Joberstellung ")
public class NewJobAdView extends Div {
    @Autowired
    private JobControl jobControl;

    // Job title text area
    private TextArea title = createTitleArea();
    // Job Description text area
    private TextArea description = createDescriptionArea();
    // Salary text field
    private TextField salary = new TextField("Jahresgehalt");
    // Location text field
    private TextField location = new TextField("Arbeitsort");
    // post new job button
    private Button postButton = new Button("Anzeige erstellen");

    private UserDTO currentUser = (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
    private Binder<JobDTOImpl> binder = new BeanValidationBinder<>(JobDTOImpl.class);

    public NewJobAdView(JobControl jobControl) {
        this.jobControl = jobControl;
        setSizeFull();
        H3 newAdText = new H3();
        newAdText.setText("Neue Jobanzeige erstellen");

        // new job ad form
        FormLayout formLayout = new FormLayout();
        formLayout.add(title, description, salary, location, postButton);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1)
        );

        binder.setBean(new JobDTOImpl(getCompanyId()));
        // map input field values to DTO variables based on chosen names
        binder.bindInstanceFields(this);

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
        this.setWidth("30%");
    }

    private TextArea createTitleArea() {
        TextArea title = new TextArea("Titel");
        int charLimitTitle = 100;
        title.setMaxLength(charLimitTitle);
        title.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + charLimitTitle));
        return title;
    }

    private TextArea createDescriptionArea() {
        TextArea description = new TextArea("Beschreibung");
        int charLimitDescr = 1024;
        description.setMaxLength(charLimitDescr);
        description.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + charLimitDescr));
        return description;
    }

    private int getCompanyId() {
        UserDTO currentUser = (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
        CompanyDTO comp = jobControl.getCompanyByUserid(currentUser.getUserid());
        return comp.getCompanyid();
    }

}
