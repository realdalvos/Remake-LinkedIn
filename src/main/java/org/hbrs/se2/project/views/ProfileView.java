package org.hbrs.se2.project.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import org.hbrs.se2.project.control.AuthorizationControl;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.hbrs.se2.project.control.ProfileControl;
import org.hbrs.se2.project.control.UserControl;
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
    protected UserControl userControl;
    @Autowired
    protected AuthorizationControl authorizationControl;

    @Autowired
    protected CommonUIElementProvider ui;

    protected TextField username = new TextField("Benutzername");
    protected TextField email = new TextField("Email-Adresse");
    protected PasswordField password = new PasswordField("Passwort");
    protected PasswordField passwordConfirm = new PasswordField("Passwort bestätigen");

    protected Button editUser = new Button("Profil bearbeiten");
    protected Button saveChanges;
    protected Button deleteUser = new Button("Account löschen");
    protected Button changePasswd = new Button("Passwort ändern");
    protected VerticalLayout layout = new VerticalLayout();
    protected FormLayout buttonLayout = new FormLayout();

    protected Binder<UserDTOImpl> userBinder = new BeanValidationBinder<>(UserDTOImpl.class);
    protected Binder<UserDTOImpl> passwordBinder = new BeanValidationBinder<>(UserDTOImpl.class);


    protected final ModelMapper mapper = new ModelMapper();

    public ProfileView() {
        username.setMaxLength(32);
        email.setMaxLength(100);
        buttonLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 3));
        buttonLayout.setWidth("80%");
        layout.getStyle().set("margin-left", "var(--lumo-space-xl");
        add(layout);

        deleteUser.addClickListener(buttonClickEvent -> {
            VerticalLayout deleteLayout = new VerticalLayout();
            HorizontalLayout buttons = new HorizontalLayout();
            Dialog dialog = ui.makeGenericDialog(deleteLayout, buttons);
            Text message = new Text("Bitte gib deinen Accountnamen zur Bestätigung ein");
            String user = userControl.getCurrentUser().getUsername();
            TextField confirmField = new TextField();
            confirmField.setPlaceholder(user);
            confirmField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
            Button close = new Button("Abbrechen");
            close.addClickListener(event -> dialog.close());
            Button delete = new Button(getTranslation("view.button.search"));
            delete.setEnabled(false);
            confirmField.setValueChangeMode(ValueChangeMode.EAGER);
            confirmField.addValueChangeListener(event -> delete.setEnabled(confirmField.getValue().equals(user)));
            delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            delete.addClickListener(event -> {
                try {
                    UserDTO currentUser = userControl.getCurrentUser();
                    authorizationControl.logoutUser();
                    profileControl.deleteUser(currentUser);
                } catch (Exception e) {
                    logger.error("Something went wrong when deleting student from DB");
                }
                dialog.close();
            });
            buttons.add(close, delete);
            deleteLayout.add(message, confirmField, buttons);
            dialog.open();
        });

        changePasswd.addClickListener(buttonClickEvent -> {
            VerticalLayout changeLayout = new VerticalLayout();
            HorizontalLayout buttons = new HorizontalLayout();
            Dialog dialog = ui.makeGenericDialog(changeLayout, buttons);
            Text message = new Text("Bitte gib dein neues Passwort ein und bestätige es");
            password.setHelperText("Mindestens 8 Zeichen bestehend aus Buchstaben, Zahlen und Sonderzeichen");
            Button close = new Button("Abbrechen");
            close.addClickListener(event -> dialog.close());
            Button save = new Button("Speichern");
            save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            save.addClickListener(event -> {
                if (passwordBinder.isValid()) {
                    try {
                        profileControl.changeUserPassword(passwordBinder.getBean());
                        ui.makeDialog("Passwort erfolgreich geändert");
                    } catch (Exception e) {
                        logger.error("Something went wrong while changing the password", e);
                        ui.makeDialog("Passwort NICHT geändert");
                    }
                    dialog.close();
                } else {
                    ui.makeDialog("Bitte überprüfe deine Eingaben");
                }
            });
            buttons.add(close, save);
            changeLayout.add(message, password, passwordConfirm, buttons);
            dialog.open();
        });
    }

    protected FormLayout profileLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("80%");
        formLayout.setResponsiveSteps(
                // Use two columns by default
                new FormLayout.ResponsiveStep("0", 2)
        );
        return formLayout;
    }

    protected void setUserBinder() {
        passwordBinder.setBean(mapper.map(userControl.getCurrentUser(), UserDTOImpl.class));
        passwordBinder
                .forField(password)
                .asRequired()
                .withValidator(pw -> pw.matches("^(?=.+[a-zA-Z])(?=.+\\d)(?=.+\\W).{8,}$"),"Dein Passwort ist wahrscheinlich nicht sicher genug. Halte dich bitte an die Vorgaben")
                .bind(UserDTOImpl::getPassword, UserDTOImpl::setPassword);
        passwordBinder
                .forField(passwordConfirm)
                .asRequired()
                .withValidator(pw -> pw.equals(password.getValue()), "Passwörter stimmen nicht überein")
                .bind(UserDTOImpl::getPassword, UserDTOImpl::setPassword);
        password.clear();
        passwordConfirm.clear();

        userBinder.setBean(mapper.map(userControl.getCurrentUser(), UserDTOImpl.class));
        userBinder
                .forField(username)
                .asRequired("Darf nicht leer sein")
                .withValidator(user -> user.equals(userControl.getCurrentUser().getUsername())
                        || userControl.checkUsernameUnique(user), "Benutzername existiert bereits")
                .bind(UserDTOImpl::getUsername, UserDTOImpl::setUsername);
        userBinder
                .forField(email)
                .asRequired("Darf nicht leer sein")
                .withValidator(new EmailValidator("Keine gültige EMail Adresse"))
                .withValidator(mail -> mail.equals(userControl.getCurrentUser().getEmail())
                        || userControl.checkEmailUnique(mail), "Email existiert bereits")
                .bind(UserDTOImpl::getEmail, UserDTOImpl::setEmail);
    }

}
