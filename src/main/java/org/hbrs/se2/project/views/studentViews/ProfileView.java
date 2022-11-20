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

@Route(value = Globals.Pages.PROFILE_VIEW, layout = AppView.class)
@PageTitle("Profile")
public class ProfileView extends Div {
    private final TextField university = new TextField("Universität:");
    private final TextField major = new TextField("Füge neuen Major hinzu:");
    private final TextField topic = new TextField("Füge neuen Topic hinzu:");
    private final TextField skill = new TextField("Füge neuen Skills hinzu:");

    private final Grid<MajorDTO> gridMajors = new Grid<>();
    private final Grid<TopicDTO> gridTopics = new Grid<>();
    private final Grid<SkillDTO> gridSkills = new Grid<>();

    private Button button;

    private final FormLayout formLayout =  new FormLayout();

    private final ProfileControl profileControl;

    public ProfileView(ProfileControl profileControl){
        this.profileControl = profileControl;

        setSizeFull();
        add(formLayout);
        setAllGrids();

        viewLayout();

        // set value of the text field university
        university.setValue(profileControl.getUniversityOfStudent(this.getCurrentUser().getUserid()));
    }

    public UserDTO getCurrentUser() {
        return (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
    }

    private void setAllGrids() {
        // Create grids for skills, topics and majors
        gridMajors.setHeightByRows(true);
        gridMajors.setDataProvider(profileControl.getMajorOfStudent(this.getCurrentUser().getUserid()));
        gridMajors.addColumn(MajorDTO::getMajor).setHeader("Majors:");
        gridTopics.setHeightByRows(true);
        gridTopics.setDataProvider(profileControl.getTopicOfStudent(this.getCurrentUser().getUserid()));
        gridTopics.addColumn(TopicDTO::getTopic).setHeader("Topics:");
        gridSkills.setHeightByRows(true);
        gridSkills.setDataProvider(profileControl.getSkillOfStudent(this.getCurrentUser().getUserid()));
        gridSkills.addColumn(SkillDTO::getSkill).setHeader("Skills:");
    }

    private void editLayout() {
        university.setReadOnly(false);
        university.setLabel("Deine aktuelle Universität:");

        gridMajors.addComponentColumn(major -> {
            Button deleteButton = new Button("Entfernen");
            deleteButton.addClickListener(e -> {
                profileControl.removeMajor(this.getCurrentUser().getUserid(), major.getMajorid());
                deleteButton.setText("Entfernt");
                deleteButton.setEnabled(false);
            });
            return deleteButton;
        });

        formLayout.addComponentAtIndex(2, major);

        gridTopics.addComponentColumn(topic -> {
            Button deleteButton = new Button("Entfernen");
            deleteButton.addClickListener(e -> {
                profileControl.removeTopic(this.getCurrentUser().getUserid(), topic.getTopicid());
                deleteButton.setText("Entfernt");
                deleteButton.setEnabled(false);
            });
            return deleteButton;
        });
        formLayout.addComponentAtIndex(4, topic);

        gridSkills.addComponentColumn(skill -> {
            Button deleteButton = new Button("Entfernen");
            deleteButton.addClickListener(e -> {
                profileControl.removeSkill(this.getCurrentUser().getUserid(), skill.getSkillid());
                deleteButton.setText("Entfernt");
                deleteButton.setEnabled(false);
            });
            return deleteButton;
        });
        formLayout.addComponentAtIndex(6, skill);

        formLayout.add(button = new Button("Profil speichern"));
        button.addClickListener(buttonClickEvent -> {
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

    private void viewLayout() {
        university.setReadOnly(true);
        button = new Button("Profil bearbeiten");
        formLayout.add(university,gridMajors,gridTopics,gridSkills,button);
        button.addClickListener(buttonClickEvent -> {
            formLayout.remove(button);
            editLayout();
        });
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0",1));
    }
}
