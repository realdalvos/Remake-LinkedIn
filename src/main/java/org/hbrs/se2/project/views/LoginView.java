package org.hbrs.se2.project.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.hbrs.se2.project.control.LoginControl;
import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.helper.AccessHandler;
import org.hbrs.se2.project.helper.navigateHandler;
import org.hbrs.se2.project.services.ui.CommonUIElementProvider;
import org.hbrs.se2.project.util.Globals;
import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Show Login View to user when not logged in
 */
@Route(value="")
@Theme(themeFolder = "mytheme", variant = Lumo.DARK)
@PageTitle("Login")
public class LoginView extends VerticalLayout {

    private final LoginControl loginControl;

    private LoginI18n createLoginI18n(){
        LoginI18n i18n = LoginI18n.createDefault();

	    /*  not sure if needed
	    i18n.setHeader(new LoginI18n.Header());
	    i18n.setForm(new LoginI18n.Form());
	    i18n.setErrorMessage(new LoginI18n.ErrorMessage());
        i18n.getHeader().setTitle("HEADER");
        i18n.getHeader().setDescription("HEADER DESCRIPTION");
	     */
        i18n.getForm().setUsername(getTranslation("view.login.text.username"));
        i18n.getForm().setTitle(getTranslation("view.login.text.title"));
        i18n.getForm().setSubmit(getTranslation("view.login.text.submit"));
        i18n.getForm().setPassword(getTranslation("view.login.text.password"));
        i18n.getForm().setForgotPassword(getTranslation("view.login.text.forgotPassword"));
        i18n.getErrorMessage().setTitle(getTranslation("view.login.error.loginFailed"));
        i18n.getErrorMessage().setMessage(getTranslation("view.login.error.loginFailed.message"));
        i18n.setAdditionalInformation("");
        return i18n;
    }

    public LoginView(LoginControl loginControl, CommonUIElementProvider ui) {
        this.loginControl = loginControl;

        AccessHandler.setDefaultAccess();
        setHeight("80%");
        LoginForm component = new LoginForm();
        component.setI18n(createLoginI18n());

        AtomicBoolean understood = new AtomicBoolean(false);
        Cookie[] cookies = VaadinRequest.getCurrent().getCookies();
        if (cookies != null) {
            Arrays.stream(cookies).forEach(cookie -> {
                if (cookie.getName().equals("understood") && cookie.getValue().equals("true")) {
                    understood.set(true);
                }
            });
        }
        if (!understood.get()) {
            cookieBanner();
        }

        // buttons
        Button buttonStudent = new Button(getTranslation("view.login.button.student"));
        buttonStudent.addThemeVariants(ButtonVariant.LUMO_LARGE);           // changing size for better usability
        buttonStudent.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        Icon studentIcon = new Icon(VaadinIcon.NOTEBOOK);
        buttonStudent.getElement().appendChild(studentIcon.getElement());

        Button buttonCompany = new Button(getTranslation("view.login.button.company"));
        buttonCompany.addThemeVariants(ButtonVariant.LUMO_LARGE);           // changing size for better usability
        buttonCompany.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        Icon companyIcon = new Icon(VaadinIcon.HOME_O);
        buttonCompany.getElement().appendChild(companyIcon.getElement());

        component.addLoginListener(e -> {
            boolean isAuthenticated = false;
            try {
                // authenticate user
                isAuthenticated = loginControl.authenticate(e.getUsername(), e.getPassword());
            } catch (DatabaseUserException ex) {
                // Error dialog
                ui.makeDialog(ex.getMessage());
            }
            if(isAuthenticated) {
                //Generate new sessionid to prevent session fixation
                VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
                // create session for user
                grabAndSetUserIntoSession();
                AccessHandler.setAccess(loginControl.getCurrentUser());

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
        buttonStudent.addClickListener(event -> navigateHandler.navigateToRegisterStudentPage());
        buttonCompany.addClickListener(event -> navigateHandler.navigateToRegisterCompanyPage());
        // add components to View
        add(component);
        add(buttonStudent);
        add(buttonCompany);
        this.setAlignItems(Alignment.CENTER);
        this.setJustifyContentMode(JustifyContentMode.CENTER);
    }

    private void grabAndSetUserIntoSession() {
        UserDTO userDTO = loginControl.getCurrentUser();
        UI.getCurrent().getSession().setAttribute(Globals.CURRENT_USER, userDTO);
    }

    private void cookieBanner() {
        Notification notification = new Notification();
        notification.setPosition(Notification.Position.BOTTOM_STRETCH);
        Span text = new Span("Diese Website verwendet für die technische Funktionalität notwendige Cookies.");
        Anchor link = new Anchor("https://www.cookiesandyou.com/", "Erfahre mehr über Cookies");
        Button consent = new Button("Verstanden!");
        consent.addClickListener(event -> {
            Cookie cookie = new Cookie("understood", "true");
            cookie.setPath("/");
            VaadinService.getCurrentResponse().addCookie(cookie);
            notification.close();
        });
        HorizontalLayout info = new HorizontalLayout(text, link, consent);
        info.setWidthFull();
        info.setVerticalComponentAlignment(Alignment.CENTER, text, link);
        consent.getElement().getStyle().set("margin-left", "auto");
        notification.add(info);
        notification.open();
    }

}
