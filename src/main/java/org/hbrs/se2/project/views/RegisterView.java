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
import org.hbrs.se2.project.control.UserControl;
import org.hbrs.se2.project.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.helper.NavigateHandler;
import org.hbrs.se2.project.services.ui.CommonUIElementProvider;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class RegisterView extends VerticalLayout {
    @Autowired
    protected RegistrationControl registrationControl;
    @Autowired
    protected UserControl userControl;
    @Autowired
    protected CommonUIElementProvider ui;
    protected H4 registerText = new H4();
    protected PasswordField userPassword = createPasswordField();
    protected PasswordField confirmPassword = new PasswordField("Passwort bestätigen");
    protected TextField username = new TextField("Benutzername");
    protected EmailField email = createEmailField();

    protected static final String NOTEMPTY = "Darf nicht leer sein";
    protected Binder<UserDTOImpl> userBinder = new BeanValidationBinder<>(UserDTOImpl.class);

    protected Button loginButton() {
        Button loginButton = new Button("Zum Login");
        loginButton.addClickListener(event -> NavigateHandler.navigateToDefaultPage());
        return loginButton;
    }

    protected PasswordField createPasswordField(){
        PasswordField userpassword = new PasswordField("Passwort");
        userpassword.setRevealButtonVisible(true);
        userpassword.setHelperText("Mindestens 8 Zeichen bestehend aus Buchstaben, Zahlen und Sonderzeichen");
        //The Pattern matches with from left to right: At least one letter, at least one digit, at lest one special character and at least 8 characters
        return userpassword;
    }

    protected EmailField createEmailField() {
        EmailField emailField = new EmailField("E-Mail");
        emailField.getElement().setAttribute("name", "email");
        emailField.setPlaceholder("username@example.com");
        emailField.setClearButtonVisible(true);
        emailField.setMaxLength(100);
        return emailField;
    }

    protected FormLayout createFormLayout(Component[] components) {
        username.setMaxLength(32);
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
        //The Pattern matches from left to right: At least one letter, at least one digit, at lest one special character and at least 8 characters
        userBinder
                .forField(userPassword)
                .asRequired(NOTEMPTY)
                .withValidator(pw -> pw.matches("^(?=.+[a-zA-Z])(?=.+\\d)(?=.+\\W).{8,}$"),"Dein Passwort ist wahrscheinlich nicht sicher genug. Halte dich bitte an die Vorgaben")
                .bind(UserDTOImpl::getPassword, UserDTOImpl::setPassword);
        // checks if both passwords are equal
        userBinder
                .forField(confirmPassword)
                .asRequired()
                .withValidator(pw -> pw.equals(userPassword.getValue()), "Passwörter stimmen nicht überein")
                .bind(UserDTOImpl::getPassword, UserDTOImpl::setPassword);
        userBinder
                .forField(username)
                .asRequired(NOTEMPTY)
                .withValidator(user -> userControl.checkUsernameUnique(user), "Benutzername existiert bereits")
                .bind(UserDTOImpl::getUsername, UserDTOImpl::setUsername);
        userBinder
                .forField(email)
                .asRequired(NOTEMPTY)
                .withValidator(new EmailValidator("Keine gültige Email Adresse"))
                .withValidator(mail -> userControl.checkEmailUnique(mail), "Email existiert bereits")
                .bind(UserDTOImpl::getEmail, UserDTOImpl::setEmail);
        // Map input field values to DTO variables based on chosen names
        userBinder.bindInstanceFields(this);
    }

}
