package org.hbrs.se2.project.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.control.RegistrationControl;
import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.entities.Student;
import org.hbrs.se2.project.entities.User;
import org.hbrs.se2.project.util.Globals;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Register View - Form to register as a student
 */
@Route(value = "register-student")
@PageTitle("Register as a Student")
public class RegisterStudentView extends VerticalLayout {

    @Autowired
    private RegistrationControl registrationControl;

    private H4 registerText = new H4();

    public RegisterStudentView() {
        setSizeFull();
        registerText.setText("Register here");
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

        // register form for student
        FormLayout formLayout = new FormLayout();
        formLayout.add(
                firstname, lastname, matrikelNumber,
                username, email,
                password, confirmPassword
        );
        formLayout.setResponsiveSteps(
                // Use one column
                new FormLayout.ResponsiveStep("0", 1)
        );

        confirmButton.addClickListener(event -> {
            boolean isRegistered;

            // create new User entity with passed in values from register form
            User user = new User();
            user.setUsername(username.getValue().trim());
            user.setPassword(password.getValue().trim());
            user.setEmail(email.getValue().trim());
            user.setRole("student");

            // create new Student entity with passed in values from register form
            Student student = new Student();
            student.setFirstname(firstname.getValue().trim());
            student.setLastname(lastname.getValue().trim());
            try {
                student.setMatrikelnumber(Integer.parseInt(matrikelNumber.getValue().trim()));
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Something went wrong with the passed matrikelnumber from the register form");
            }

            // checks if all input fields were filled out correctly
            if(!registrationControl.checkFormInputStudent(user.getUsername(), user.getPassword(), user.getEmail(),
                    student.getFirstname(), student.getLastname(), confirmPassword.getValue().trim())) {
                throw new Error("The input from the register form was not filled out correctly");
            }

            // call function to save user and student data in db
            try {
                // function to register new student
                isRegistered = registrationControl.registerStudent(user, student);
            } catch (Exception e) {
                try {
                    throw new DatabaseUserException("Something went wrong with registering student");
                } catch (DatabaseUserException ex) {
                    throw new RuntimeException("Something went wrong with registering student");
                }
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

        // add all elements/components to View
        add(registerText);
        add(formLayout);
        add(confirmButton);
        add(loginButton);
        this.setWidth("70%");
        this.setAlignItems(Alignment.CENTER);
    }

    private void navigateToLoginPage() {
        UI.getCurrent().navigate(Globals.Pages.LOGIN_VIEW);
    }
}

