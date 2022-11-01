package org.hbrs.se2.project.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.hbrs.se2.project.control.RegistrationControl;
import org.hbrs.se2.project.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.util.Globals;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class RegisterView extends VerticalLayout {

    @Autowired
    protected RegistrationControl registrationControl;

    protected PasswordField password = new PasswordField("Password");
    protected PasswordField confirmPassword = new PasswordField("Confirm password");

    protected TextField username = new TextField("Username");

    protected EmailField email = createEmailField();

    protected Binder<UserDTOImpl> userBinder = new Binder(UserDTOImpl.class);
    protected Binder<Object> concreteUserBinder;

    protected abstract Component createFormLayout();

    protected Button loginButton() {
        Button loginButton = new Button("I already have an account - Log In");
        loginButton.addClickListener(event -> {
            navigateToLoginPage();
        });
        return loginButton;
    }

    protected EmailField createEmailField() {
        EmailField email = new EmailField("Email address");
        email.getElement().setAttribute("name", "email");
        email.setPlaceholder("username@example.com");
        email.setErrorMessage("Please enter a valid example.com email address");
        email.setClearButtonVisible(true);
        email.setPattern("^(.+)@(\\S+)$");
        return email;
    }

    protected void navigateToLoginPage() {
        UI.getCurrent().navigate(Globals.Pages.LOGIN_VIEW);
    }

}
