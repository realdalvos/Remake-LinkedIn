package org.hbrs.se2.project.views.studentViews;
//package com.vaadin.demo.component.textfield;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinRequest;
import org.hbrs.se2.project.control.exception.ProfileControl;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.views.AppView;
import org.springframework.beans.factory.annotation.Autowired;
/*
import com.vaadin.ui.UI;
import com.vaadin.ui.Table;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
*/

import java.awt.*;

@Route(value = Globals.Pages.PROFILE_VIEW, layout = AppView.class)
@PageTitle("Profile")
public class ProfileView extends Div {

    //@Autowired
    //private ProfileControl profileControl;
    //private GridLayout form;
    //private Table table;
    @Autowired
    private ProfileView(){
        setSizeFull();
        LoginForm component = new LoginForm();

        //buttons
        Button buttonUsername = new Button("Change Username");
        Button buttonPassword = new Button("Change Password");
        add(buttonUsername);
        add(buttonPassword);

        TextField textfield = new TextField();
        textfield.setPlaceholder("first name");
        add(textfield);

     /*   protected void init(){
            VerticalLayout mainLayout = new VerticalLayout();

            mainLayout.setSpacing(true);
            mainLayout.setMargin(true);
            mainLayout.addComponent(buildTable());
            mainLayout.addComponent(buildForm());

            setContent(mainLayout);

        }
        */
    }


}
