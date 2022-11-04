package org.hbrs.se2.project.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.VaadinService;
import org.hbrs.se2.project.control.LoginControl;
import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.helper.navigateHandler;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * View zur Darstellung der Startseite. Diese zeigt dem Benutzer ein Login-Formular an.
 *
 */
@Route(value="")
@RouteAlias(value=Globals.Pages.LOGIN_VIEW)
public class LoginView extends VerticalLayout {

    @Autowired
    private LoginControl loginControl;

    public LoginView() {
        setSizeFull();
        Avatar avatarBasic = new Avatar();
        LoginForm component = new LoginForm();

        // buttons
        Button buttonStudent = new Button("Register as a Student");
        buttonStudent.addThemeVariants(ButtonVariant.LUMO_LARGE);           // changing size for better usability
        buttonStudent.addThemeVariants(ButtonVariant.LUMO_SUCCESS);         // Different colour to seperate Log In and Register
        Button buttonCompany = new Button("Register as a Company");
        buttonCompany.addThemeVariants(ButtonVariant.LUMO_LARGE);           // changing size for better usability
        buttonCompany.addThemeVariants(ButtonVariant.LUMO_SUCCESS);         // Different colour to seperate Log In and Register

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
                //Generate new sessionid to prevent session fixation
                VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
                // create session for user
                grabAndSetUserIntoSession();

                if(loginControl.getCurrentUser().getRole().equals(Globals.Roles.student)) {
                    // navigate to jobs view for student
                    navigateHandler.navigateToJobsView();
                } else if(loginControl.getCurrentUser().getRole().equals(Globals.Roles.company)) {
                    // navigate to my ads view for companies
                    navigateHandler.navigateToMyAdsView();
                }
            } else {
                component.setError(true);
            }
        });
        // navigate to student or company register page
        buttonStudent.addClickListener(event -> {
            navigateHandler.navigateToRegisterStudentPage();
        });
        buttonCompany.addClickListener(event -> {
            navigateHandler.navigateToRegisterCompanyPage();
        });
        // add components to View
        add(avatarBasic);
        add(component);
        add(buttonStudent);
        add(buttonCompany);
        this.setAlignItems(Alignment.CENTER);

    }

    private void grabAndSetUserIntoSession() {
        UserDTO userDTO = loginControl.getCurrentUser();
        UI.getCurrent().getSession().setAttribute(Globals.CURRENT_USER, userDTO);
    }
}

