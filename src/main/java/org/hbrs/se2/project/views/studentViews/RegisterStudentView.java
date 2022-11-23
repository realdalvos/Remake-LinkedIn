package org.hbrs.se2.project.views.studentViews;


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
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.slf4j.Logger;

/**
 * Register View - Form to register as a student
 */

@Route(value = Globals.Pages.REGISTER_STUDENT_VIEW)
@PageTitle("Als Student registrieren")
public class RegisterStudentView extends RegisterView {
    private final Logger logger = Utils.getLogger(this.getClass().getName());

    // text fields
    private final TextField firstname = new TextField("Vorname");
    private final TextField lastname = new TextField("Nachname");
    private final TextField matrikelnumber = new TextField("Matrikelnummer");
    private final Binder<StudentDTOImpl> concreteUserBinder = new BeanValidationBinder<>(StudentDTOImpl.class);

    public RegisterStudentView() {
        setSizeFull();
        registerText.setText("Registrieren als");

        //create buttons and set layout
        Button registerStudentButton = new Button("Student");
        registerStudentButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button registerCompanyButton = new Button("Unternehmen");
        registerCompanyButton.setEnabled(true);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(new Component[]{registerStudentButton,registerCompanyButton});
        buttonLayout.setAlignItems(Alignment.CENTER);

        //set layout for text input
        HorizontalLayout inputTextLayout = new HorizontalLayout();
        inputTextLayout.add(createFormLayout(new Component[]{firstname,lastname,username,matrikelnumber,email ,userPassword,confirmPassword}));
        inputTextLayout.setAlignItems(Alignment.CENTER);

        Button confirmButton = new Button("Jetzt als Student registrieren");

        registerCompanyButton.addClickListener(event -> navigateHandler.navigateToRegisterCompanyPage());

        userBinder.setBean(new UserDTOImpl(Globals.Roles.student));
        //The Pattern matches from left to right: At least one letter, at least one digit, at lest one special character and at least 8 characters
        userBinder.withValidator(validation -> userPassword.getValue().matches("^(?=.+[a-zA-Z])(?=.+[\\d])(?=.+[\\W]).{8,}$"),"Dein Passwort ist wahrscheinlich nicht sicher genug. Halte dich bitte an die Vorgaben");

        concreteUserBinder.setBean(new StudentDTOImpl());
        // add all elements/components to View
        add(registerText);
        add(buttonLayout);
        add(inputTextLayout);
        add(confirmButton);
        add(loginButton());
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
                    ui.makeDialog("Fülle bitte alle Felder aus");
                    logger.info("Not all fields have been filled in");
                }
            } catch (Exception e) {
                // get the root cause of an exception
                // Error dialog
                ui.makeDialog(Utils.getRootCause(e));
                logger.error("An error has occurred while saving to the database", e);
            }
        });
    }
}
