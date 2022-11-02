package org.hbrs.se2.project.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.hbrs.se2.project.control.RegistrationControl;
import org.hbrs.se2.project.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.helper.navigateHandler;
import org.hbrs.se2.project.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class RegisterView extends VerticalLayout {

    @Autowired
    protected RegistrationControl registrationControl;
    protected H4 registerText = new H4();
    protected PasswordField password = new PasswordField("Password");
    protected PasswordField confirmPassword = new PasswordField("Confirm password");
    protected TextField username = new TextField("Username");
    protected EmailField email = createEmailField();
    protected Binder<UserDTOImpl> userBinder = new Binder(UserDTOImpl.class);
    protected Binder<Object> concreteUserBinder;

    protected abstract Component createFormLayout();

    protected abstract void checkInput();

    protected Button loginButton() {
        Button loginButton = new Button("I already have an account - Log In");
        loginButton.addClickListener(event -> {
            navigateHandler.navigateToLoginPage();
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

    protected void checkPassword() {
        if(!confirmPassword.getValue().equals(password.getValue())) {
            // error dialog
            Utils.makeDialog("Both passwords are not the same");
            throw new Error("The given two passwords are not equal");
        }
    }

    protected UserDTOImpl setUserDTO(String role) {
        UserDTOImpl bean = new UserDTOImpl();
        bean.setRole(role);
        return bean;
    }
}
