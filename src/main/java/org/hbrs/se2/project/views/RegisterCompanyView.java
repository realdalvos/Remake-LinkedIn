package org.hbrs.se2.project.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.util.Globals;

@Route(value = "register-company")
@PageTitle("Register as a Company")
public class RegisterCompanyView extends VerticalLayout {

    // register controller

    private H4 registerText = new H4();

    public RegisterCompanyView() {
        setSizeFull();
        // text fields
        TextField companyName = new TextField("Company Name");
        TextField username = new TextField("Username");

        // email field
        EmailField email = new EmailField("Email address");
        email.getElement().setAttribute("name", "email");
        email.setPlaceholder("username@example.com");
        email.setErrorMessage("Please enter a valid example.com email address");
        email.setClearButtonVisible(true);
        email.setPattern("^.+@example\\.com$");

        // password fields
        PasswordField password = new PasswordField("Password");
        PasswordField confirmPassword = new PasswordField("Confirm password");

        Button confirmButton = new Button("Register now as a company");
        Button loginButton = new Button("I already have an account - Log In");

        registerText.setText("Register here");

        FormLayout formLayout = new FormLayout();
        formLayout.add(
                companyName, username, email,
                password, confirmPassword
        );
        formLayout.setResponsiveSteps(
                // Use two columns by default
                new FormLayout.ResponsiveStep("0", 1)
        );

        confirmButton.addClickListener(event -> {
            // function to register new account
        });

        loginButton.addClickListener(event -> {
            navigateToLoginPage();
        });

        add(registerText);
        add(formLayout);
        add(confirmButton);
        add(loginButton);
        this.setWidth("30%");
        this.setAlignItems(Alignment.CENTER);
    }

    private void navigateToLoginPage() {
        UI.getCurrent().navigate(Globals.Pages.LOGIN_VIEW);
    }
}
