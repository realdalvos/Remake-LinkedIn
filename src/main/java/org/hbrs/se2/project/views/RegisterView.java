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
import org.hbrs.se2.project.control.RegistrationControl;
import org.hbrs.se2.project.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.helper.navigateHandler;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class RegisterView extends VerticalLayout {
    @Autowired
    protected RegistrationControl registrationControl;
    protected H4 registerText = new H4();
    protected PasswordField userPassword = createPasswordField();
    protected PasswordField confirmPassword = new PasswordField("Passwort bestätigen");
    protected TextField username = new TextField("Benutzername");
    protected EmailField email = createEmailField();
    protected Binder<UserDTOImpl> userBinder = new BeanValidationBinder<>(UserDTOImpl.class);

    protected Button loginButton() {
        Button loginButton = new Button("Zum Login");
        loginButton.addClickListener(event -> navigateHandler.navigateToLoginPage());
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

    protected Component createFormLayout(Component[] components) {
        FormLayout formLayout = new FormLayout();
        formLayout.add(components);
        formLayout.setResponsiveSteps(
                // Use two columns by default
                new FormLayout.ResponsiveStep("0", 2)
        );
        formLayout.setColspan(email, 2);
        return formLayout;
    }
}

