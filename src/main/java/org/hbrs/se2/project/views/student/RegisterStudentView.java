package org.hbrs.se2.project.views.student;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.dtos.impl.StudentDTOImpl;
import org.hbrs.se2.project.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.helper.NavigateHandler;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.util.Utils;
import org.hbrs.se2.project.views.RegisterView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.slf4j.Logger;
import java.util.stream.Stream;

/**
 * Register View - Form to register as a student
 */

@Route(value = Globals.Pages.REGISTER_STUDENT_VIEW, registerAtStartup = false)
@PageTitle("Als Student registrieren")
public class RegisterStudentView extends RegisterView {
    private final Logger logger = Utils.getLogger(this.getClass().getName());

    // text fields
    private final TextField firstname = new TextField("Vorname");
    private final TextField lastname = new TextField("Nachname");
    private final TextField matrikelnumber = new TextField("Matrikelnummer");
    private final Binder<StudentDTOImpl> studentBinder = new BeanValidationBinder<>(StudentDTOImpl.class);

    public RegisterStudentView() {
        setSizeFull();
        registerText.setText("Registrieren als");

        //create buttons and set layout
        Button registerStudentButton = new Button("Student");
        registerStudentButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button registerCompanyButton = new Button("Unternehmen");
        registerCompanyButton.setEnabled(true);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(registerStudentButton,registerCompanyButton);
        buttonLayout.setAlignItems(Alignment.CENTER);

        //set layout for text input
        HorizontalLayout inputTextLayout = new HorizontalLayout();
        Stream.of(firstname, lastname, matrikelnumber).forEach(field -> field.setMaxLength(32));
        inputTextLayout.add(createFormLayout(new Component[]{firstname,lastname,username,matrikelnumber,email ,userPassword,confirmPassword}));
        inputTextLayout.setAlignItems(Alignment.CENTER);

        Button confirmButton = new Button("Jetzt als Student registrieren");

        registerCompanyButton.addClickListener(event -> NavigateHandler.navigateToRegisterCompanyPage());

        userBinder.setBean(new UserDTOImpl(Globals.Roles.STUDENT));
        setUserBinder();
        setStudentBinder();

        // add all elements/components to View
        add(registerText);
        add(buttonLayout);
        add(inputTextLayout);
        add(confirmButton);
        add(loginButton());
        this.setAlignItems(Alignment.CENTER);

        confirmButton.addClickListener(event -> {
            // register new Company with passed in values from register form
            try {
                if (userBinder.isValid() && studentBinder.isValid()) {
                    // function to register new company
                    registrationControl.registerStudent(userBinder.getBean(), studentBinder.getBean());
                    NavigateHandler.navigateToDefaultPage();
                    ui.throwNotification("Erfolgreich registriert. Du kannst dich nun einloggen.");
                } else {
                    ui.makeDialog("Überprüfe bitte deine Eingaben");
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

    private void setStudentBinder() {
        studentBinder.setBean(new StudentDTOImpl());
        studentBinder
                .forField(matrikelnumber)
                .asRequired(NOTEMPTY)
                .withValidator(mn -> mn.matches("-?\\d+")
                        && mn.length() <= 7, "Keine gültige Matrikelnummer")
                .withValidator(mn -> userControl.checkMatrikelnumberUnique(mn), "Matrikelnummer existiert bereits")
                .bind(StudentDTOImpl::getMatrikelnumber, StudentDTOImpl::setMatrikelnumber);
        studentBinder.bindInstanceFields(this);
    }
}