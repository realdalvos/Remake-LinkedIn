package org.vaadin.example.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.control.LoginControl;
import org.vaadin.example.control.exception.DatabaseUserException;
import org.vaadin.example.dtos.UserDTO;
import org.vaadin.example.util.Globals;


/**
 * View zur Darstellung der Startseite. Diese zeigt dem Benutzer ein Login-Formular an.
 * ToDo: Integration einer Seite zur Registrierung von Benutzern
 */
@Route(value="")
@RouteAlias(value="login")
public class LoginView extends VerticalLayout {

    @Autowired
    private LoginControl loginControl;

    public LoginView() {
        setSizeFull();
        LoginForm component = new LoginForm();

        component.addLoginListener(e -> {
            boolean isAuthenticated = false;
            try {
                isAuthenticated = loginControl.authenticate(e.getUsername(), e.getPassword());
            } catch (DatabaseUserException ex) {
                Dialog dialog = new Dialog();
                dialog.add(new Text(ex.getReason()));
                dialog.setWidth("400px");
                dialog.setHeight("150px");
                dialog.open();
            }
            if(isAuthenticated) {
                grabAndSetUserIntoSession();
                navigateToMainPage();
            } else {
                component.setError(true);
            }
        });
        add(component);
        this.setAlignItems(Alignment.CENTER);
    }

    private void grabAndSetUserIntoSession() {
        UserDTO userDTO = loginControl.getCurrentUser();
        UI.getCurrent().getSession().setAttribute(Globals.CURRENT_USER, userDTO);
    }

    private void navigateToMainPage() {
        UI.getCurrent().navigate(Globals.Pages.Overview);
    }
}
