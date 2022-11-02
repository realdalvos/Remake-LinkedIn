package org.hbrs.se2.project.views.studientViews;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.dtos.impl.StudentDTOImpl;
import org.hbrs.se2.project.helper.navigateHandler;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.util.Utils;
import org.hbrs.se2.project.views.RegisterView;

/**
 * Register View - Form to register as a student
 */
@Route(value = Globals.Pages.REGISTER_STUDENT_VIEW)
@PageTitle("Register as a Student")
public class RegisterStudentView extends RegisterView {
    // text fields
    private TextField firstname = new TextField("First name");
    private TextField lastname = new TextField("Last name");
    private TextField matrikelnumber = new TextField("Matrikel number");
    Binder<StudentDTOImpl> concreteUserBinder = new Binder(StudentDTOImpl.class);

    public RegisterStudentView() {
        setSizeFull();
        registerText.setText("Register here");

        Button confirmButton = new Button("Register now as a user");

        userBinder.setBean(setUserDTO(Globals.Roles.student));
        concreteUserBinder.setBean(new StudentDTOImpl());

        // add all elements/components to View
        add(registerText);
        add(createFormLayout());
        add(confirmButton);
        add(loginButton());
        this.setWidth("30%");
        this.setAlignItems(Alignment.CENTER);

        // Map input field values to DTO variables based on chosen names
        userBinder.bindInstanceFields(this);
        concreteUserBinder.bindInstanceFields(this);

        confirmButton.addClickListener(event -> {
            boolean isRegistered = false;

            // checks if all input fields were filled out correctly
            checkInput();

            // checks if both passwords are equal
            checkPassword();

            // register new Company with passed in values from register form
            try {
                // function to register new company
                isRegistered = registrationControl.registerStudent(userBinder.getBean(), concreteUserBinder.getBean());
            } catch (Exception e) {
                // get the root cause of an exception
                String message = Utils.getRootCause(e);
                // Error dialog
                Utils.makeDialog(message);
            }

            if(isRegistered) {
                navigateHandler.navigateToLoginPage();
            } else {
                System.out.println("A Failure occurred while trying to save data in the database");
            }
        });
    }

    @Override
    protected Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(
                firstname, lastname, matrikelnumber,
                username, email,
                password, confirmPassword
        );
        formLayout.setResponsiveSteps(
                // Use one column
                new FormLayout.ResponsiveStep("0", 1)
        );
        return formLayout;
    }

    @Override
    protected void checkInput() {
        if(!Utils.checkIfInputEmpty(
                new String[]{
                        userBinder.getBean().getUsername(),
                        userBinder.getBean().getEmail(),
                        userBinder.getBean().getPassword(),
                        concreteUserBinder.getBean().getFirstname(),
                        concreteUserBinder.getBean().getLastname()
                })) {
            // error dialog
            Utils.makeDialog("Please fill out all text fields.");
            throw new Error("Not all input field were filled out.");
        }
    }
}


