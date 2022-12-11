package org.hbrs.se2.project.views.companyViews;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.dtos.impl.CompanyDTOImpl;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.hbrs.se2.project.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.helper.navigateHandler;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.util.Utils;
import org.hbrs.se2.project.views.RegisterView;
import org.slf4j.Logger;

@Route(value = Globals.Pages.REGISTER_COMPANY_VIEW, registerAtStartup = false)
@PageTitle("Als Unternehmen registrieren")
public class RegisterCompanyView extends RegisterView {
    private final Logger logger = Utils.getLogger(this.getClass().getName());

    // text fields
    private final TextField name = new TextField("Unternehmensname");
    private final Binder<CompanyDTOImpl> companyBinder = new BeanValidationBinder<>(CompanyDTOImpl.class);

    public RegisterCompanyView() {
        setSizeFull();
        registerText.setText("Registrieren als");

        //create buttons and set layout
        Button registerStudentButton = new Button("Student");
        Button registerCompanyButton = new Button("Unternehmen");
        registerCompanyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        registerCompanyButton.setEnabled(true);
        registerStudentButton.setEnabled(true);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(registerStudentButton,registerCompanyButton);
        buttonLayout.setAlignItems(Alignment.CENTER);

        //set layout for text input
        HorizontalLayout inputTextLayout = new HorizontalLayout();
        inputTextLayout.add(createFormLayout(new Component[]{name,username,email,userPassword,confirmPassword}));
        inputTextLayout.setAlignItems(Alignment.CENTER);

        Button confirmButton = new Button("Jetzt als Unternehmen registrieren");

        registerStudentButton.addClickListener(event -> navigateHandler.navigateToRegisterStudentPage());

        userBinder.setBean(new UserDTOImpl(Globals.Roles.company));
        setUserBinder();
        companyBinder.setBean(new CompanyDTOImpl());
        companyBinder.bindInstanceFields(this);

        add(registerText);
        add(buttonLayout);
        add(inputTextLayout);
        add(confirmButton);
        add(loginButton());
        this.setAlignItems(Alignment.CENTER);

        confirmButton.addClickListener(event -> {
            // register new Company with passed in values from register form
            try {
                if (userBinder.isValid() && companyBinder.isValid()) {
                    // function to register new company
                    registrationControl.registerCompany(userBinder.getBean(), companyBinder.getBean());
                    navigateHandler.navigateToDefaultPage();
                } else {
                    ui.makeDialog("Überprüfen Sie bitte Ihre Eingaben");
                    logger.info("Not all fields have been filled in");
                }
            } catch (Exception e) {
                // get the root cause of an exception
                // Error dialog
                ui.makeDialog(Utils.getRootCause(e));
                logger.error("An error has occurred while saving to the database", e);
            }
        });
    }
}