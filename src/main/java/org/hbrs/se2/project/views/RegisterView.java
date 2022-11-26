package org.hbrs.se2.project.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import org.hbrs.se2.project.control.RegistrationControl;
import org.hbrs.se2.project.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.helper.navigateHandler;
import org.hbrs.se2.project.services.ui.CommonUIElementProvider;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class RegisterView extends VerticalLayout {
    @Autowired
    protected RegistrationControl registrationControl;
    @Autowired
    protected CommonUIElementProvider ui;
    protected H4 registerText = new H4();
    protected PasswordField userPassword = createPasswordField();
    protected PasswordField confirmPassword = new PasswordField("Passwort bestätigen");
    protected TextField username = new TextField("Benutzername");
    protected EmailField email = createEmailField();
    protected Binder<UserDTOImpl> userBinder = new BeanValidationBinder<>(UserDTOImpl.class);

    protected Button loginButton() {
        Button loginButton = new Button("Zum Login");
        loginButton.addClickListener(event -> navigateHandler.navigateToDefaultPage());
        return loginButton;
    }

    protected PasswordField createPasswordField(){
        PasswordField userpassword = new PasswordField("Passwort");
        userpassword.setRequired(true);
        userpassword.setMinLength(8);
        userpassword.setRevealButtonVisible(true);
        userpassword.setHelperText("Mindestens 8 Zeichen bestehend aus Buchstaben, Zahlen und Sonderzeichen");
        //The Pattern matches with from left to right: At least one letter, at least one digit, at lest one special character and at least 8 characters
        userpassword.setPattern("^(?=.+[a-zA-Z])(?=.+[\\d])(?=.+[\\W]).{8,}$");
        userpassword.setErrorMessage("Dein Passwort ist wahrscheinlich nicht sicher genug. Halte dich bitte an die Vorgaben");
        return userpassword;
    }

    protected EmailField createEmailField() {
        EmailField email = new EmailField("E-Mail");
        email.getElement().setAttribute("name", "email");
        email.setPlaceholder("username@example.com");
        email.setErrorMessage("Bitte gib eine gültige E-Mail-Adresse ein");
        email.setClearButtonVisible(true);
        email.setPattern("^(.+)@(\\S+)$");
        return email;
    }

    protected FormLayout createFormLayout(Component[] components) {
        FormLayout formLayout = new FormLayout();
        formLayout.add(components);
        formLayout.setResponsiveSteps(
                // Use two columns by default
                new FormLayout.ResponsiveStep("0", 2)
        );
        formLayout.setColspan(email, 2);
        return formLayout;
    }

    protected void setUserBinder() {
        // Map input field values to DTO variables based on chosen names
        userBinder.bindInstanceFields(this);
        // checks if both passwords are equal
        Binder.Binding<UserDTOImpl, String> confirmPasswordBinding =
                userBinder
                        .forField(confirmPassword)
                        .asRequired()
                        .withValidator(pw -> pw.equals(userPassword.getValue()), "Passwörter stimmen nicht überein")
                        .bind(UserDTOImpl::getPassword, UserDTOImpl::setPassword);
        //The Pattern matches from left to right: At least one letter, at least one digit, at lest one special character and at least 8 characters
        userBinder
                .withValidator(validation -> userPassword.getValue().matches("^(?=.+[a-zA-Z])(?=.+[\\d])(?=.+[\\W]).{8,}$"),"Dein Passwort ist wahrscheinlich nicht sicher genug. Halte dich bitte an die Vorgaben");
        userBinder
                .forField(username)
                .asRequired("Darf nicht leer sein")
                .withValidator(validation -> registrationControl.checkUsernameUnique(username.getValue()), "Benutzername existiert bereits")
                .bind(UserDTOImpl::getUsername, UserDTOImpl::setUsername);
        userBinder
                .forField(email)
                .asRequired("Darf nicht leer sein")
                .withValidator(new EmailValidator("Keine gültige EMail Adresse"))
                .withValidator(validation -> registrationControl.checkEmailUnique(email.getValue()), "Email existiert bereits")
                .bind(UserDTOImpl::getEmail, UserDTOImpl::setEmail);
        userPassword.addValueChangeListener(
                event -> confirmPasswordBinding.validate());
    }
}
