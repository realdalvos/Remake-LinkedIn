package org.hbrs.se2.project.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
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

        // Buttons
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

        // is executed when confirm button is clicked
        confirmButton.addClickListener(event -> {
            boolean isRegistered = false;


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

            // get and set matrikelnumber of student
            try {
                student.setMatrikelnumber(Integer.parseInt(matrikelNumber.getValue().trim()));
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Something went wrong with the passed matrikelnumber from the register form");
            }

            // checks if all input fields were filled out
            if(!registrationControl.checkFormInputStudent(user.getUsername(), user.getPassword(), user.getEmail(),
                    student.getFirstname(), student.getLastname())) {
                // Error dialog
                Dialog dialog = new Dialog();
                dialog.add(new Text("Please fill out all text fields."));
                dialog.setWidth("400px");
                dialog.setHeight("150px");
                dialog.open();
                throw new Error("Not all input field were filled out.");
            }

            // checks if both passwords are equal
            if(!registrationControl.checkPasswordConfirmation(confirmPassword.getValue(), password.getValue())) {
                // error dialog
                Dialog dialog = new Dialog();
                dialog.add(new Text("Both passwords are not the same"));
                dialog.setWidth("400px");
                dialog.setHeight("150px");
                dialog.open();
                throw new Error("The given two passwords are not equal.");
            }

            // call function to save user and student data in db
            try {
                // function to register new student
                isRegistered = registrationControl.registerStudent(user, student);
            } catch (Exception e) {
                // get the message of the root cause of the exception
                Throwable rootCause = e;
                while(rootCause.getCause() != null && rootCause.getCause() != rootCause) {
                    rootCause = rootCause.getCause();
                }
                // error dialog
                String message = rootCause.getMessage();
                String[] messArr = message.split("Key");
                message = messArr[messArr.length - 1];
                Dialog dialog = new Dialog();
                dialog.add(new Text(message));
                dialog.setWidth("400px");
                dialog.setHeight("150px");
                dialog.open();
            }

            if(isRegistered) {
                navigateToLoginPage();
            } else {
                System.out.println("Student is not registered.");
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

