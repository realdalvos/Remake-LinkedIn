package org.hbrs.se2.project.views.studentViews;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.textfield.TextField;
import org.hbrs.se2.project.control.ProfileControl;
import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.MajorDTO;
import org.hbrs.se2.project.dtos.SkillDTO;
import org.hbrs.se2.project.dtos.TopicDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.views.AppView;

import java.util.List;

@Route(value = Globals.Pages.PROFILE_VIEW, layout = AppView.class)
@PageTitle("Profile")
public class ProfileView extends Div {
    private TextField university = new TextField("University");
    private TextField major = new TextField("Major");
    private TextField topic = new TextField("Topics");
    private TextField skill = new TextField("Skills");

    Button save = new Button("save");

    private final ProfileControl profileControl;

    public ProfileView(ProfileControl profileControl){
        this.profileControl = profileControl;

        setSizeFull();
        setLayout();
        setAllGrids();

        // set value of the text field university
        university.setValue(profileControl.getUniversityOfStudent(this.getCurrentUser().getUserid()));

        // save student data
        save.addClickListener(buttonClickEvent -> {
            try {
                profileControl.saveStudentData(
                        this.getCurrentUser().getUserid(),
                        major.getValue(),
                        university.getValue(),
                        topic.getValue(),
                        skill.getValue());
            } catch (DatabaseUserException e) {
                // should be replaced with logger
                throw new Error("Something went wrong with saving student data");
            }
            // reload page to get updated view
            UI.getCurrent().getPage().reload();
        });
    }
    public UserDTO getCurrentUser() {
        return (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
    }

    private void setAllGrids() {

        // Create grids for skills, topics and majors
        Grid<MajorDTO> gridMajors = new Grid<>();
        gridMajors.setDataProvider(profileControl.getMajorOfStudent(this.getCurrentUser().getUserid()));
        gridMajors.addColumn(MajorDTO::getMajor).setHeader("Majors");

        Grid<TopicDTO> gridTopics = new Grid<>();
        gridTopics.setDataProvider(profileControl.getTopicOfStudent(this.getCurrentUser().getUserid()));
        gridTopics.addColumn(TopicDTO::getTopic).setHeader("Topics");

        Grid<SkillDTO> gridSkills = new Grid<>();
        gridSkills.setDataProvider(profileControl.getSkillOfStudent(this.getCurrentUser().getUserid()));
        gridSkills.addColumn(SkillDTO::getSkill).setHeader("Skills");

        // add to view
        add(gridMajors);
        add(gridTopics);
        add(gridSkills);
    }

    private void setLayout() {
        FormLayout formLayout =  new FormLayout();
        formLayout.add(university,major,topic,skill,save);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0",1));
        add(formLayout);
    }
}
