package org.hbrs.se2.project.views.studentViews;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
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
    private Binder<StudentDTOImpl> concreteUserBinder = new BeanValidationBinder<>(StudentDTOImpl.class);

    public RegisterStudentView() {
        setSizeFull();
        registerText.setText("Register here");
        userPassword.setRequired(true);

        Button confirmButton = new Button("Register now as a user");

        userBinder.setBean(new UserDTOImpl(Globals.Roles.student));
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
            boolean success = true;

            // register new Company with passed in values from register form
            try {
                if (userBinder.isValid() && concreteUserBinder.isValid()) {
                    // function to register new company
                    registrationControl.registerStudent(userBinder.getBean(), concreteUserBinder.getBean());
                } else {
                    Utils.makeDialog("Please fill out all text fields.");
                    throw new Error("Not all input field were filled out.");
                }
            } catch (Exception e) {
                // get the root cause of an exception
                String message = Utils.getRootCause(e);
                // Error dialog
                Utils.makeDialog(message);
                success = false;
            }

            if(success) {
                navigateHandler.navigateToLoginPage();
            } else {
                System.out.println("A Failure occurred while trying to save data in the database");
            }
        });
    }

}


