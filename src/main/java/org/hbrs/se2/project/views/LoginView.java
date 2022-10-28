package org.hbrs.se2.project.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.hbrs.se2.project.control.LoginControl;
import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * View zur Darstellung der Startseite. Diese zeigt dem Benutzer ein Login-Formular an.
 *
 */
@Route(value="")
@RouteAlias(value="login")
public class LoginView extends VerticalLayout {

    @Autowired
    private LoginControl loginControl;

    public LoginView() {
        setSizeFull();
        LoginForm component = new LoginForm();

        // buttons
        Button buttonStudent = new Button("Register as a Student");
        Button buttonCompany = new Button("Register as a Company");

        component.addLoginListener(e -> {
            boolean isAuthenticated = false;
            try {
                // authenticate user
                isAuthenticated = loginControl.authenticate(e.getUsername(), e.getPassword());
            } catch (DatabaseUserException ex) {
                // Error dialog
                Utils.makeDialog(ex.getReason());
            }
            if(isAuthenticated) {
                // create session for user
                grabAndSetUserIntoSession();
                navigateToMainPage();
            } else {
                component.setError(true);
            }
        });
        // navigate to student or company register page
        buttonStudent.addClickListener(event -> {
            navigateToRegisterStudentPage();
        });
        buttonCompany.addClickListener(event -> {
            navigateToRegisterCompanyPage();
        });
        // add components to View
        add(component);
        add(buttonStudent);
        add(buttonCompany);
        this.setAlignItems(Alignment.CENTER);
    }

    private void grabAndSetUserIntoSession() {
        UserDTO userDTO = loginControl.getCurrentUser();
        UI.getCurrent().getSession().setAttribute(Globals.CURRENT_USER, userDTO);
    }

    private void navigateToMainPage() {
        UI.getCurrent().navigate(Globals.Pages.MAIN_VIEW);
    }

    private void navigateToRegisterCompanyPage() {
        UI.getCurrent().navigate(Globals.Pages.REGISTER_COMPANY_VIEW);
    }

    private void navigateToRegisterStudentPage() {
        UI.getCurrent().navigate(Globals.Pages.REGISTER_STUDENT_VIEW);
    }
}
