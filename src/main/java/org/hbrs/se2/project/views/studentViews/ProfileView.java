package org.hbrs.se2.project.views.studentViews;
//package com.vaadin.demo.component.textfield;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.textfield.TextField;
import org.hbrs.se2.project.control.ProfileControl;
import org.hbrs.se2.project.dtos.StudentDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.entities.User;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.util.Utils;
import org.hbrs.se2.project.views.AppView;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
/*
import com.vaadin.ui.UI;
import com.vaadin.ui.Table;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
*/

@Route(value = Globals.Pages.PROFILE_VIEW, layout = AppView.class)
@PageTitle("Profile")
public class ProfileView extends Div {

    @Autowired
    private ProfileControl profileControl;
    public ProfileView(){
        setSizeFull();

        Button save = new Button("save");

        TextField major = new TextField("Major");
        major.setPlaceholder("Major");

        TextField university = new TextField("University");
        university.setPlaceholder("University");

        TextField topics = new TextField("Topics");
        topics.setPlaceholder("Topics");

        TextField skills = new TextField("Skills");
        skills.setPlaceholder("Skills");

        FormLayout formLayout =  new FormLayout();
        formLayout.add(major,university,topics,skills);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0",1));
        add(formLayout);
        add(save);

        save.addClickListener   (buttonClickEvent -> {
            if (major.getValue() != null && !major.getValue().equals("")) {
                profileControl.updateStudyMajor(major.getValue(), this.getCurrentUser().getUserid());
            }
            if (university.getValue() != null && !university.getValue().equals("")) {
                profileControl.updateUniversity(university.getValue(), this.getCurrentUser().getUserid());
            }
            if (topics.getValue() != null && !topics.getValue().equals("")) {
                System.out.println(topics.getValue());
            }
            if (skills.getValue() != null && !skills.getValue().equals("")) {
                System.out.println(skills.getValue());
            }
        });

    }
    public UserDTO getCurrentUser() {
        UserDTO userDTO = (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
        System.out.println(userDTO.getUserid());
        return userDTO;
    }
}
