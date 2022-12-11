package org.hbrs.se2.project.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import org.hbrs.se2.project.control.ProfileControl;
import org.hbrs.se2.project.control.UserControl;
import org.hbrs.se2.project.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.services.ui.CommonUIElementProvider;
import org.hbrs.se2.project.util.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ProfileView extends Div {

    protected ProfileControl profileControl;
    protected UserControl userControl;

    @Autowired
    protected CommonUIElementProvider ui;

    protected final TextField username = new TextField("Benutzername:");
    protected final TextField email = new TextField("EMail-Adresse:");
    protected Button button;
    protected final FormLayout formLayout = new FormLayout();

    protected final Binder<UserDTOImpl> userBinder = new Binder<>(UserDTOImpl.class);

    protected final ModelMapper mapper = new ModelMapper();

    protected void setUserBinder() {
        userBinder.bindInstanceFields(this);
        userBinder.setBean(mapper.map(userControl.getCurrentUser(), UserDTOImpl.class));
        userBinder
                .forField(username)
                .asRequired("Darf nicht leer sein")
                .withValidator(validation -> !username.getValue().isBlank(), "Darf nicht leer sein")
                .withValidator(validation -> username.getValue().equals(userControl.getCurrentUser().getUsername())
                        || profileControl.checkUsernameUnique(username.getValue()), "Benutzername existiert bereits")
                .bind(UserDTOImpl::getUsername, UserDTOImpl::setUsername);
        userBinder
                .forField(email)
                .asRequired("Darf nicht leer sein")
                .withValidator(new EmailValidator("Keine gültige EMail Adresse"))
                .withValidator(validation -> email.getValue().equals(userControl.getCurrentUser().getEmail())
                        || profileControl.checkEmailUnique(email.getValue()), "Email existiert bereits")
                .bind(UserDTOImpl::getEmail, UserDTOImpl::setEmail);
        username.addValueChangeListener(
                event -> userBinder.validate());
        email.addValueChangeListener(
                event -> userBinder.validate());
    }

}
