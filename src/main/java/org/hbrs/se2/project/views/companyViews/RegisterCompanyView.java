package org.hbrs.se2.project.views.companyViews;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.dtos.impl.CompanyDTOImpl;
import org.hbrs.se2.project.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.helper.navigateHandler;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.util.Utils;
import org.hbrs.se2.project.views.RegisterView;
import org.slf4j.Logger;

@Route(value = Globals.Pages.REGISTER_COMPANY_VIEW)
@PageTitle("Register as a Company")
public class RegisterCompanyView extends RegisterView {
    private final Logger logger = Utils.getLogger(this.getClass().getName());

    // text fields
    private TextField name = new TextField("Unternehmensname");
    private Binder<CompanyDTOImpl> concreteUserBinder = new BeanValidationBinder<>(CompanyDTOImpl.class);

    public RegisterCompanyView() {
        setSizeFull();
        registerText.setText("Registrierung");

        Button confirmButton = new Button("Registrierung als Unternehmen");

        userBinder.setBean(new UserDTOImpl(Globals.Roles.company));
        //The Pattern matches from left to right: At least one letter, at least one digit, at lest one special character and at least 8 characters
        userBinder.withValidator(validation -> userPassword.getValue().matches("^(?=.+[a-zA-Z])(?=.+[\\d])(?=.+[\\W]).{8,}$"),"Dein Passwort ist wahrscheinlich nicht sicher genug. Halte dich bitte an die Vorgaben");

        concreteUserBinder.setBean(new CompanyDTOImpl());

        add(registerText);
        add(createFormLayout(new Component[]{name,username,email,userPassword,confirmPassword}));
        add(confirmButton);
        add(loginButton());
        this.setWidth("30%");
        this.setAlignItems(Alignment.CENTER);

        // Map input field values to DTO variables based on chosen names
        userBinder.bindInstanceFields(this);
        // checks if both passwords are equal
        Binder.Binding<UserDTOImpl, String> confirmPasswordBinding =
                userBinder
                        .forField(confirmPassword)
                        .asRequired()
                        .withValidator(pw -> pw.equals(userPassword.getValue()), "Passwörter stimmen nicht überein")
                        .bind(UserDTOImpl::getPassword, UserDTOImpl::setPassword);
        concreteUserBinder.bindInstanceFields(this);

        userPassword.addValueChangeListener(
                event -> confirmPasswordBinding.validate());

        confirmButton.addClickListener(event -> {
            // register new Company with passed in values from register form
            try {
                if (userBinder.isValid() && concreteUserBinder.isValid()) {
                    // function to register new company
                    registrationControl.registerCompany(userBinder.getBean(), concreteUserBinder.getBean());
                    navigateHandler.navigateToLoginPage();
                } else {
                    Utils.makeDialog("Fülle bitte alle Felder aus");
                    logger.info("Not all fields have been filled in");
                }
            } catch (Exception e) {
                // get the root cause of an exception
                String message = Utils.getRootCause(e);
                // Error dialog
                Utils.makeDialog(message);
                logger.error("An error has occurred while saving to the database", e);
            }
        });
    }
}