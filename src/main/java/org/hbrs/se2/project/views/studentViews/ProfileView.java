package org.hbrs.se2.project.views.studentViews;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.views.AppView;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;

@Route(value = Globals.Pages.PROFILE_VIEW, layout = AppView.class)
@PageTitle("Profile")
public class ProfileView extends Div {

    @Autowired
    private ProfileView(){
        setSizeFull();
        LoginForm component = new LoginForm();

        //buttons
        Button buttonStudent = new Button("Change Username");
        Button buttonStudent = new Button("Change Password");
    }


}
