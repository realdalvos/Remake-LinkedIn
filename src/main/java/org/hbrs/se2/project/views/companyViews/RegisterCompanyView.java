package org.hbrs.se2.project.views.companyViews;

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
import org.hbrs.se2.project.control.RegistrationControl;
import org.hbrs.se2.project.entities.Company;
import org.hbrs.se2.project.entities.User;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "register-company")
@PageTitle("Register as a Company")
public class RegisterCompanyView extends VerticalLayout {

    @Autowired
    private RegistrationControl registrationControl;

    private H4 registerText = new H4();

    public RegisterCompanyView() {
        setSizeFull();
        registerText.setText("Register here");
        // text fields
        TextField companyName = new TextField("Company Name");
        TextField username = new TextField("Username");

        // email field
        EmailField email = new EmailField("Email address");
        email.getElement().setAttribute("name", "email");
        email.setPlaceholder("username@example.com");
        email.setErrorMessage("Please enter a valid example.com email address");
        email.setClearButtonVisible(true);
        email.setPattern("^(.+)@(\\S+)$");

        // password fields
        PasswordField password = new PasswordField("Password");
        PasswordField confirmPassword = new PasswordField("Confirm password");

        // buttons
        Button confirmButton = new Button("Register now as a company");
        Button loginButton = new Button("I already have an account - Log In");

        // register form for company
        FormLayout formLayout = new FormLayout();
        formLayout.add(
                companyName, username, email,
                password, confirmPassword
        );
        formLayout.setResponsiveSteps(
                // Use two columns by default
                new FormLayout.ResponsiveStep("0", 1)
        );

        // executed when button is clicked
        confirmButton.addClickListener(event -> {
            boolean isRegistered = false;
            // create new User entity with passed in values from register form
            User user = new User();
            user.setUsername(username.getValue().trim());
            user.setPassword(password.getValue().trim());
            user.setEmail(email.getValue().trim());
            user.setRole("company");

            // create new Company entity with passed in values from register form
            Company company = new Company();
            company.setName(companyName.getValue().trim());

            // checks if all input fields were filled out correctly
            if(!registrationControl.checkFormInputCompany(user.getUsername(), user.getPassword(),
                    user.getEmail(), company.getName())) {
                // error dialog
                Utils.makeDialog("Please fill out all text fields.");
                throw new Error("Not all input field were filled out.");
            }

            // checks if both passwords are equal
            if(!registrationControl.checkPasswordConfirmation(confirmPassword.getValue(), password.getValue())) {
                // error dialog
                Utils.makeDialog("Both passwords are not the same");
                throw new Error("The given two passwords are not equal");
            }

            // call function to save user and company data in db
            try {
                // function to register new company
                isRegistered = registrationControl.registerCompany(user, company);
            } catch (Exception e) {
                // get the root cause of an exception
                String message = Utils.getRootCause(e);
                // Error dialog
                Utils.makeDialog(message);
            }

            if(isRegistered) {
                navigateToLoginPage();
            } else {
                System.out.println("A Failure occurred while trying to save user and company data in the database");
            }
        });

        loginButton.addClickListener(event -> {
            navigateToLoginPage();
        });

        // add all elements/components to View
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
