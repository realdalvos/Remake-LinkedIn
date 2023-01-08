package org.hbrs.se2.project.views.companyViews;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.control.ProfileControl;
import org.hbrs.se2.project.control.UserControl;
import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.impl.CompanyDTOImpl;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.util.Utils;
import org.hbrs.se2.project.views.AppView;
import org.hbrs.se2.project.views.ProfileView;
import org.slf4j.Logger;

import java.util.stream.Stream;

@JsModule("@vaadin/vaadin-lumo-styles/badge.js")
@CssImport(value = "./themes/mytheme/styles.css", include = "lumo-badge")
@Route(value = Globals.Pages.COMPANY_PROFILE_VIEW, layout = AppView.class, registerAtStartup = false)
@PageTitle("Profile")
public class CompanyProfileView extends ProfileView {
    private final Logger logger = Utils.getLogger(this.getClass().getName());
    private boolean banned;
    private final TextField name = new TextField("Name des Unternehmens:");
    private final TextField industry = new TextField("Industrie:");

    private final Binder<CompanyDTOImpl> companyBinder = new BeanValidationBinder<>(CompanyDTOImpl.class);

    public CompanyProfileView(ProfileControl profileControl, UserControl userControl) {
        this.profileControl = profileControl;
        this.userControl = userControl;

        setSizeFull();
        setUserBinder();
        setCompanyBinder();
        viewLayout();
    }

    private void viewLayout() {
        FormLayout viewLayout = profileLayout();
        HorizontalLayout status = new HorizontalLayout();
        status.add("Accountstatus:");
        status.add(statusBadge());
        status.setPadding(true);
        viewLayout.add(status);
        // set value of text fields to read only for profile view
        Stream.of(username, name, email, industry).forEach(
                field -> {
                    field.setReadOnly(true);
                    viewLayout.add(field);
                }
        );
        Stream.of(name, industry).forEach(field -> field.setMaxLength(32));
        buttonLayout.add(edit, changePasswd, delete);
        layout.add(viewLayout, buttonLayout);
        edit.addClickListener(buttonClickEvent -> {
            buttonLayout.removeAll();
            layout.removeAll();
            editLayout();
        });
        viewLayout.setColspan(status, 2);
    }

    private void editLayout() {
        FormLayout editLayout = profileLayout();
        Stream.of(username, name, email, industry).forEach(field -> {
            field.setReadOnly(false);
            editLayout.add(field);
        });
        save = new Button("Profil speichern");
        buttonLayout.add(save);
        layout.add(editLayout, buttonLayout);
        save.addClickListener(buttonClickEvent -> {
            if (userBinder.isValid() && companyBinder.isValid()) {
                ui.makeConfirm("Möchtest du die Änderungen an deinem Profil speichern?",
                        event -> {
                            if (!userBinder.getBean().getUsername().equals(userControl.getCurrentUser().getUsername())) {
                                authorizationControl.logoutUser();
                            } else {
                                buttonLayout.removeAll();
                                layout.removeAll();
                                viewLayout();
                                ui.throwNotification("Profil erfolgreich gespeichert.");
                            }
                            try {
                                profileControl.saveCompanyData(userBinder.getBean(), companyBinder.getBean());
                            } catch (DatabaseUserException e) {
                                logger.info("failed to update company information in db");
                            }
                        });
            } else {
                ui.makeDialog("Überprüfe bitte deine Angaben auf Korrektheit");
            }
        });
    }

    private void setCompanyBinder() {
        companyBinder.setBean(mapper.map(userControl.getCompanyProfile(userControl.getCurrentUser().getUserid()), CompanyDTOImpl.class));
        companyBinder.bindInstanceFields(this);
        banned = companyBinder.getBean().getBanned();
    }

    private Span statusBadge() {
        Icon icon;
        Span info, badge;
        if (banned) {
            icon = new Icon(VaadinIcon.INFO_CIRCLE);
            icon.getStyle().set("padding", "var(--lumo-space-xs");
            info = new Span("Gesperrt");
            badge = new Span(info, icon);
            badge.getElement().getThemeList().add("badge error");
        } else {
            icon = new Icon(VaadinIcon.CHECK);
            icon.getStyle().set("padding", "var(--lumo-space-xs");
            info = new Span("OK");
            badge = new Span(info, icon);
            badge.getElement().getThemeList().add("badge success");
        }
        return badge;
    }

}
