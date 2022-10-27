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
import org.hbrs.se2.project.control.RegistrationControl;
import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.entities.Student;
import org.hbrs.se2.project.entities.User;
import org.hbrs.se2.project.util.Globals;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Register View - Form to register as a student
 */
@Route(value = "register-student")
@PageTitle("Register as a Student")
public class RegisterStudentView extends VerticalLayout {

    // register controller
    @Autowired
    private RegistrationControl registrationControl;

    private H4 registerText = new H4();

    public RegisterStudentView() {
        setSizeFull();
        // text fields
        TextField firstname = new TextField("First name");
        TextField lastname = new TextField("Last name");
        TextField username = new TextField("Username");
        TextField matrikelNumber = new TextField("Matrikel number");

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

        Button confirmButton = new Button("Register now as a student");
        Button loginButton = new Button("I already have an account - Log In");

        registerText.setText("Register here");

        FormLayout formLayout = new FormLayout();
        formLayout.add(
                firstname, lastname, matrikelNumber,
                username, email,
                password, confirmPassword
        );
        formLayout.setResponsiveSteps(
                // Use two columns by default
                new FormLayout.ResponsiveStep("0", 1)
        );

        confirmButton.addClickListener(event -> {
            boolean isRegistered;
            // ToDO: password check necessary
            // ToDo: Input Fields not empty checks
            // function to register new account
            User user = new User();
            user.setUsername(username.getValue().trim());
            user.setPassword(password.getValue().trim());
            user.setEmail(email.getValue().trim());
            user.setRole("student");

            Student student = new Student();
            student.setFirstname(firstname.getValue().trim());
            student.setLastname(lastname.getValue().trim());
            student.setUniversity("");
            student.setStudyMajor("");
            student.setMatrikelnumber(Integer.parseInt(matrikelNumber.getValue().trim()));

            // call function to save user and student data in db
            try {
                isRegistered = registrationControl.registerStudent(user, student);
            } catch (DatabaseUserException e) {
                throw new RuntimeException(e);
            }
            if(isRegistered) {
                navigateToLoginPage();
            } else {
                System.out.println("Something went wrong with the registration process");
            }
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

