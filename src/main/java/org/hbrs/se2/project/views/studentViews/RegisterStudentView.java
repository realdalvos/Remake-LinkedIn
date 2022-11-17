package org.hbrs.se2.project.views.studentViews;

<<<<<<< HEAD
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
import org.hbrs.se2.project.entities.Student;
import org.hbrs.se2.project.entities.User;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
=======
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.dtos.impl.StudentDTOImpl;
import org.hbrs.se2.project.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.helper.navigateHandler;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.util.Utils;
import org.hbrs.se2.project.views.RegisterView;
import org.slf4j.Logger;
>>>>>>> main

/**
 * Register View - Form to register as a student
 */
<<<<<<< HEAD
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
                // Error dialog
                Utils.makeDialog("Please fill out the matrikelnumber field");
                throw new Error("Something is wrong with your filled in matrikel number");
            }

            // checks if all input fields were filled out
            if(!registrationControl.checkFormInputStudent(user.getUsername(), user.getPassword(), user.getEmail(),
                    student.getFirstname(), student.getLastname())) {
                // Error dialog
                Utils.makeDialog("Please fill out all text fields.");
                throw new Error("Not all input field were filled out.");
            }

            // checks if both passwords are equal
            if(!registrationControl.checkPasswordConfirmation(confirmPassword.getValue(), password.getValue())) {
                // error dialog
                Utils.makeDialog("Both passwords are not the same");
                throw new Error("The given two passwords are not equal.");
            }

            // call function to save user and student data in db
            try {
                // function to register new student
                isRegistered = registrationControl.registerStudent(user, student);
            } catch (Exception e) {
                // get the message of the root cause of the exception
                String message = Utils.getRootCause(e);
                Utils.makeDialog(message);
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

=======
@Route(value = Globals.Pages.REGISTER_STUDENT_VIEW)
@PageTitle("Als Student registrieren")
public class RegisterStudentView extends RegisterView {
    private final Logger logger = Utils.getLogger(this.getClass().getName());

    // text fields
    private TextField firstname = new TextField("Vorname");
    private TextField lastname = new TextField("Nachname");
    private TextField matrikelnumber = new TextField("Matrikelnummer");
    private Binder<StudentDTOImpl> concreteUserBinder = new BeanValidationBinder<>(StudentDTOImpl.class);

    public RegisterStudentView() {
        setSizeFull();
        registerText.setText("Registrierung");

        userBinder.setBean(new UserDTOImpl(Globals.Roles.student));
        //The Pattern matches from left to right: At least one letter, at least one digit, at lest one special character and at least 8 characters
        userBinder.withValidator(validation -> userPassword.getValue().matches("^(?=.+[a-zA-Z])(?=.+[\\d])(?=.+[\\W]).{8,}$"),"Dein Passwort ist wahrscheinlich nicht sicher genug. Halte dich bitte an die Vorgaben");

        concreteUserBinder.setBean(new StudentDTOImpl());
        // add all elements/components to View
        add(registerText);
        add(createFormLayout(new Component[]{firstname,lastname,matrikelnumber,username,email,userPassword,confirmPassword}));
        add(confirmButton);
        add(loginButton());
        this.setWidth("30%");
        this.setAlignItems(Alignment.CENTER);

        // Map input field values to DTO variables based on chosen names
        userBinder.bindInstanceFields(this);
        // checks if both passwords are equal
        Binder.Binding<UserDTOImpl, String> confirmPasswordBinding =
                userBinder
                        .forField(confirmPassword)
                        .asRequired()
                        .withValidator(pw -> pw.equals(userPassword.getValue()), "Passwörter stimmen nicht überein")
                        .bind(UserDTOImpl::getPassword, UserDTOImpl::setPassword);
        concreteUserBinder.bindInstanceFields(this);

        userPassword.addValueChangeListener(
                event -> confirmPasswordBinding.validate());

        confirmButton.addClickListener(event -> {
            // register new Company with passed in values from register form
            try {
                if (userBinder.isValid() && concreteUserBinder.isValid()) {
                    // function to register new company
                    registrationControl.registerStudent(userBinder.getBean(), concreteUserBinder.getBean());
                    navigateHandler.navigateToLoginPage();
                } else {
                    Utils.makeDialog("Fülle bitte alle Felder aus");
                    logger.info("Not all fields have been filled in");
                }
            } catch (Exception e) {
                // get the root cause of an exception
                String message = Utils.getRootCause(e);
                // Error dialog
                Utils.makeDialog(message);
                logger.error("An error has occurred while saving to the database", e);
            }
        });
    }
}
>>>>>>> main
