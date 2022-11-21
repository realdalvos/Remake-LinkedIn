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
import org.hbrs.se2.project.util.Utils;
import org.hbrs.se2.project.views.AppView;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Route(value = Globals.Pages.PROFILE_VIEW, layout = AppView.class)
@PageTitle("Profile")
public class ProfileView extends Div {

    private final Logger logger = Utils.getLogger(this.getClass().getName());
    private final TextField university = new TextField("Universität:");
    private final TextField major = new TextField("Füge neuen Major hinzu:");
    private final TextField topic = new TextField("Füge neuen Topic hinzu:");
    private final TextField skill = new TextField("Füge neuen Skills hinzu:");

    private final Grid<MajorDTO> gridMajors = new Grid<>();
    private final Grid<TopicDTO> gridTopics = new Grid<>();
    private final Grid<SkillDTO> gridSkills = new Grid<>();

    private Button button;

    private final FormLayout formLayout = new FormLayout();

    private final ProfileControl profileControl;

    public ProfileView(ProfileControl profileControl) {
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
        gridMajors.addColumn(MajorDTO::getMajor).setHeader("Majors:");
        gridMajors.setItems(profileControl.getMajorOfStudent(this.getCurrentUser().getUserid()));
        gridTopics.setHeightByRows(true);
        gridTopics.addColumn(TopicDTO::getTopic).setHeader("Topics:");
        gridTopics.setItems(profileControl.getTopicOfStudent(this.getCurrentUser().getUserid()));
        gridSkills.setHeightByRows(true);
        gridSkills.addColumn(SkillDTO::getSkill).setHeader("Skills:");
        gridSkills.setItems(profileControl.getSkillOfStudent(this.getCurrentUser().getUserid()));
    }

    private void editLayout() {
        Grid<String> newMajorsGrid, newTopicsGrid, newSkillsGrid;
        List<String> newMajors, newTopics, newSkills;

        university.setReadOnly(false);
        university.setLabel("Deine aktuelle Universität:");

        gridMajors.addComponentColumn(major -> {
            Button deleteButton = new Button("Entfernen");
            deleteButton.addClickListener(e -> {
                profileControl.removeMajor(this.getCurrentUser().getUserid(), major.getMajorid());
                gridMajors.setItems(profileControl.getMajorOfStudent(this.getCurrentUser().getUserid()));
            });
            return deleteButton;
        });
        formLayout.addComponentAtIndex(2, newMajorsGrid = newEntriesGrid(newMajors = new ArrayList<>()));
        formLayout.addComponentAtIndex(3, newEntryLayout(major, newMajors, newMajorsGrid));

        gridTopics.addComponentColumn(topic -> {
            Button deleteButton = new Button("Entfernen");
            deleteButton.addClickListener(e -> {
                profileControl.removeTopic(this.getCurrentUser().getUserid(), topic.getTopicid());
                gridTopics.setItems(profileControl.getTopicOfStudent(this.getCurrentUser().getUserid()));
            });
            return deleteButton;
        });
        formLayout.addComponentAtIndex(5, newTopicsGrid = newEntriesGrid(newTopics = new ArrayList<>()));
        formLayout.addComponentAtIndex(6, newEntryLayout(topic, newTopics, newTopicsGrid));

        gridSkills.addComponentColumn(skill -> {
            Button deleteButton = new Button("Entfernen");
            deleteButton.addClickListener(e -> {
                profileControl.removeSkill(this.getCurrentUser().getUserid(), skill.getSkillid());
                gridSkills.setItems(profileControl.getSkillOfStudent(this.getCurrentUser().getUserid()));
            });
            return deleteButton;
        });
        formLayout.addComponentAtIndex(8, newSkillsGrid = newEntriesGrid(newSkills = new ArrayList<>()));
        formLayout.addComponentAtIndex(9, newEntryLayout(skill, newSkills, newSkillsGrid));

        formLayout.add(button = new Button("Profil speichern"));
        button.addClickListener(buttonClickEvent -> {
            try {
                profileControl.saveStudentData(
                        this.getCurrentUser().getUserid(),
                        university.getValue(),
                        newMajors, newTopics, newSkills);
            } catch (DatabaseUserException e) {
                logger.error("Something went wrong with saving student data");
            }
            // reload page to get updated view
            UI.getCurrent().getPage().reload();
        });
    }

    private void viewLayout() {
        university.setReadOnly(true);
        button = new Button("Profil bearbeiten");
        formLayout.add(university, gridMajors, gridTopics, gridSkills, button);
        button.addClickListener(buttonClickEvent -> {
            formLayout.remove(button);
            editLayout();
        });
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
    }

    private Grid<String> newEntriesGrid(List<String> entries) {
        Grid<String> grid = new Grid<>();
        grid.addColumn(String::valueOf);
        grid.setHeightByRows(true);
        grid.addComponentColumn(newEntry -> {
            Button deleteButton = new Button("Entfernen");
            deleteButton.addClickListener(e -> {
                entries.remove(newEntry);
                deleteButton.setText("Entfernt");
                grid.setItems(entries);
            });
            return deleteButton;
        });
        return grid;
    }

    private FormLayout newEntryLayout(TextField input, List<String> entries, Grid<String> grid) {
        FormLayout entryForm = new FormLayout();
        Button saveButton = new Button("Hinzufügen");
        saveButton.addClickListener(e -> {
            if (!input.getValue().isBlank()) {
                entries.add(input.getValue());
                grid.setItems(entries);
            }
            input.clear();
        });
        entryForm.add(input);
        entryForm.add(saveButton);
        return entryForm;
    }

}