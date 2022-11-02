package org.hbrs.se2.project.views.companyViews;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.dtos.impl.CompanyDTOImpl;
import org.hbrs.se2.project.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.helper.navigateHandler;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.util.Utils;
import org.hbrs.se2.project.views.RegisterView;

@Route(value = Globals.Pages.REGISTER_COMPANY_VIEW)
@PageTitle("Register as a Company")
public class RegisterCompanyView extends RegisterView {

    private H4 registerText = new H4();
    // text fields
    private TextField name = new TextField("Company Name");
    Binder<CompanyDTOImpl> concreteUserBinder = new Binder(CompanyDTOImpl.class);

    public RegisterCompanyView() {
        setSizeFull();
        registerText.setText("Register here");

        Button confirmButton = new Button("Register now as a company");

        userBinder.setBean(new UserDTOImpl());
        concreteUserBinder.setBean(new CompanyDTOImpl());

        add(registerText);
        add(createFormLayout());
        add(confirmButton);
        add(loginButton());
        this.setWidth("30%");
        this.setAlignItems(Alignment.CENTER);

        // WIP! - mapping of attributes and the names of this View based on variable names not working
        userBinder.forField(username).bind(UserDTOImpl::getUsername, UserDTOImpl::setUsername);
        userBinder.forField(password).bind(UserDTOImpl::getPassword, UserDTOImpl::setPassword);
        userBinder.forField(email).bind(UserDTOImpl::getEmail, UserDTOImpl::setEmail);

        concreteUserBinder.forField(name).bind(CompanyDTOImpl::getName, CompanyDTOImpl::setName);

        confirmButton.addClickListener(event -> {
            boolean isRegistered = false;

            // checks if all input fields were filled out correctly
            if(!Utils.checkIfInputEmpty(
                    new String[]{
                            userBinder.getBean().getUsername(),
                            userBinder.getBean().getPassword(),
                            userBinder.getBean().getEmail(),
                            concreteUserBinder.getBean().getName()
                    })) {
                // error dialog
                Utils.makeDialog("Please fill out all text fields.");
                throw new Error("Not all input field were filled out.");
            }

            // checks if both passwords are equal
            if(!confirmPassword.getValue().equals(password.getValue())) {
                // error dialog
                Utils.makeDialog("Both passwords are not the same");
                throw new Error("The given two passwords are not equal");
            }

            // register new Company with passed in values from register form
            try {
                // function to register new company
                isRegistered = registrationControl.registerCompany(userBinder.getBean(), concreteUserBinder.getBean());
            } catch (Exception e) {
                // get the root cause of an exception
                String message = Utils.getRootCause(e);
                // Error dialog
                Utils.makeDialog(message);
            }

            if(isRegistered) {
                navigateHandler.navigateToLoginPage();
            } else {
                System.out.println("A Failure occurred while trying to save data in the database");
            }
        });

    }

    @Override
    protected Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(
                name, username, email,
                password, confirmPassword
        );
        formLayout.setResponsiveSteps(
                // Use two columns by default
                new FormLayout.ResponsiveStep("0", 1)
        );
        return formLayout;
    }
}
