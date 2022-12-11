package org.hbrs.se2.project.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import org.hbrs.se2.project.control.ProfileControl;
import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.services.ui.CommonUIElementProvider;
import org.hbrs.se2.project.util.Utils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ProfileView extends Div {

    private final Logger logger = Utils.getLogger(this.getClass().getName());

    protected ProfileControl profileControl;

    @Autowired
    protected CommonUIElementProvider ui;

    protected final UserDTO CURRENT_USER = Utils.getCurrentUser();

    protected final TextField username = new TextField("Benutzername:");
    protected final TextField email = new TextField("EMail-Adresse:");
    protected Button button;
    protected Button delete = new Button("Account löschen");
    protected final FormLayout formLayout = new FormLayout();

    protected final Binder<UserDTOImpl> userBinder = new Binder<>(UserDTOImpl.class);

    protected final ModelMapper mapper = new ModelMapper();

    public ProfileView() {
        delete.addClickListener(buttonClickEvent -> ui.makeDeleteConfirm("Bitte gib deinen Accountnamen zur Bestätigung ein:", event -> {
            try {
                profileControl.deleteUser(CURRENT_USER);
                this.getUI().ifPresent(ui -> {
                    ui.getSession().close();
                    ui.getPage().setLocation("/");
                });
            } catch (DatabaseUserException e) {
                logger.error("Something went wrong when deleting student from DB");
            }
        }));
    }

    protected void setUserBinder() {
        userBinder.bindInstanceFields(this);
        userBinder.setBean(mapper.map(Utils.getCurrentUser(), UserDTOImpl.class));
        userBinder
                .forField(username)
                .asRequired("Darf nicht leer sein")
                .withValidator(validation -> !username.getValue().isBlank(), "Darf nicht leer sein")
                .withValidator(validation -> username.getValue().equals(CURRENT_USER.getUsername())
                        || profileControl.checkUsernameUnique(username.getValue()), "Benutzername existiert bereits")
                .bind(UserDTOImpl::getUsername, UserDTOImpl::setUsername);
        userBinder
                .forField(email)
                .asRequired("Darf nicht leer sein")
                .withValidator(new EmailValidator("Keine gültige EMail Adresse"))
                .withValidator(validation -> email.getValue().equals(CURRENT_USER.getEmail())
                        || profileControl.checkEmailUnique(email.getValue()), "Email existiert bereits")
                .bind(UserDTOImpl::getEmail, UserDTOImpl::setEmail);
        username.addValueChangeListener(
                event -> userBinder.validate());
        email.addValueChangeListener(
                event -> userBinder.validate());
    }

}
