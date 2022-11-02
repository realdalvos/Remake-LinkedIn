package org.hbrs.se2.project.views.studentViews;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.views.AppView;

import java.awt.*;

@Route(value = Globals.Pages.PROFILE_VIEW, layout = AppView.class)
@PageTitle("Profile")
public class ProfileView extends Div {

    Button buttonStudent = new Button("Register as a Student");

}
