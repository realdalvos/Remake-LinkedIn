package org.hbrs.se2.project.views.studentViews;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.textfield.TextField;
import org.hbrs.se2.project.control.ProfileControl;
import org.hbrs.se2.project.control.UserControl;
import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.*;
import org.hbrs.se2.project.dtos.impl.StudentDTOImpl;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.util.Utils;
import org.hbrs.se2.project.views.AppView;
import org.hbrs.se2.project.views.ProfileView;
import org.slf4j.Logger;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Route(value = Globals.Pages.STUDENT_PROFILE_VIEW, layout = AppView.class, registerAtStartup = false)
@PageTitle("Profile")
public class StudentProfileView extends ProfileView {

    private final Logger logger = Utils.getLogger(this.getClass().getName());

    private final TextField newMajor = new TextField("Füge einen neuen Studiengang hinzu:");
    private final TextField newTopic = new TextField("Füge ein neues Interessengebiet hinzu:");
    private final TextField newSkill = new TextField("Füge eine neue Fähigkeit hinzu:");
    private final TextField firstname = new TextField("Vorname:");
    private final TextField lastname = new TextField("Nachname:");
    private final TextField university = new TextField("Universität:");
    private final TextField matrikelnumber = new TextField("Matrikelnummer:");

    private Grid<MajorDTO> gridMajors;
    private Grid<TopicDTO> gridTopics;
    private Grid<SkillDTO> gridSkills;
    private Set<MajorDTO> majors;
    private Set<TopicDTO> topics;
    private Set<SkillDTO> skills;
    private Set<String> newMajors;
    private Set<String> newTopics;
    private Set<String> newSkills;
    private final Set<MajorDTO> removeMajors = new HashSet<>();
    private final Set<TopicDTO> removeTopics = new HashSet<>();
    private final Set<SkillDTO> removeSkills = new HashSet<>();

    private final String DELETE = "Entfernen";

    private final Binder<StudentDTOImpl> studentBinder = new BeanValidationBinder<>(StudentDTOImpl.class);

    public StudentProfileView(ProfileControl profileControl, UserControl userControl) {
        this.profileControl = profileControl;
        this.userControl = userControl;

        setSizeFull();
        setUserBinder();
        setStudentBinder();
        viewLayout();
    }

    private void setAllGrids() {
        gridMajors = new Grid<>();
        gridTopics = new Grid<>();
        gridSkills = new Grid<>();
        // Create grids for skills, topics and majors
        gridMajors.addColumn(MajorDTO::getMajor).setHeader("Studiengänge:");
        gridMajors.setItems(majors);
        gridTopics.addColumn(TopicDTO::getTopic).setHeader("Interessen:");
        gridTopics.setItems(topics);
        gridSkills.addColumn(SkillDTO::getSkill).setHeader("Fähigkeiten:");
        gridSkills.setItems(skills);
    }

    private void editLayout() {
        setAllGrids();
        Grid<String> newMajorsGrid;
        Grid<String> newTopicsGrid;
        Grid<String> newSkillsGrid;
        FormLayout editLayout = profileLayout();
        Stream.of(username, firstname, lastname, email, university, matrikelnumber).forEach(field -> {
            editLayout.add(field);
            field.setReadOnly(false);
        });
        gridMajors.addComponentColumn(major -> {
            Button deleteButton = new Button(DELETE);
            deleteButton.addClickListener(e -> {
                removeMajors.add(major);
                majors.remove(major);
                gridMajors.setItems(majors);
            });
            return deleteButton;
        });
        newMajors = new HashSet<>();
        newMajorsGrid = newEntriesGrid(newMajors);
        editLayout.add(gridMajors, newMajorsGrid, newEntryLayout(newMajor, newMajors, newMajorsGrid));
        gridTopics.addComponentColumn(topic -> {
            Button deleteButton = new Button(DELETE);
            deleteButton.addClickListener(e -> {
                removeTopics.add(topic);
                topics.remove(topic);
                gridTopics.setItems(topics);
            });
            return deleteButton;
        });
        newTopics = new HashSet<>();
        newTopicsGrid = newEntriesGrid(newTopics);
        editLayout.add(gridTopics, newTopicsGrid, newEntryLayout(newTopic, newTopics, newTopicsGrid));
        gridSkills.addComponentColumn(skill -> {
            Button deleteButton = new Button(DELETE);
            deleteButton.addClickListener(e -> {
                removeSkills.add(skill);
                skills.remove(skill);
                gridSkills.setItems(skills);
            });
            return deleteButton;
        });
        newSkills = new HashSet<>();
        newSkillsGrid = newEntriesGrid(newSkills);
        editLayout.add(gridSkills, newSkillsGrid, newEntryLayout(newSkill, newSkills, newSkillsGrid));
        Stream.of(gridMajors, newMajorsGrid, gridTopics, newTopicsGrid, gridSkills, newSkillsGrid).forEach(grid -> {
            grid.setAllRowsVisible(true);
            editLayout.setColspan(grid, 2);
        });
        saveChanges = new Button("Profil speichern");
        buttonLayout.add(saveChanges);
        layout.add(editLayout, buttonLayout);
        saveChanges.addClickListener(buttonClickEvent -> {
            if (userBinder.isValid() && studentBinder.isValid()) {
                ui.makeConfirm("Möchtest du die Änderungen an deinem Profil speichern?",
                        event -> {
                            try {
                                if (!userBinder.getBean().getUsername().equals(userControl.getCurrentUser().getUsername())) {
                                    authorizationControl.logoutUser();
                                } else {
                                    buttonLayout.removeAll();
                                    layout.removeAll();
                                    ui.throwNotification("Profil erfolgreich gespeichert.");
                                }
                                profileControl.saveStudentData(
                                        userBinder.getBean(), studentBinder.getBean(),
                                        newMajors, newTopics, newSkills);
                            } catch (DatabaseUserException e) {
                                logger.error("Something went wrong with saving student data");
                            }
                            removeMajors.forEach(majorDTO -> profileControl.removeMajor(userControl.getCurrentUser().getUserid(), majorDTO.getMajorid()));
                            removeTopics.forEach(topicDTO -> profileControl.removeTopic(userControl.getCurrentUser().getUserid(), topicDTO.getTopicid()));
                            removeSkills.forEach(skillDTO -> profileControl.removeSkill(userControl.getCurrentUser().getUserid(), skillDTO.getSkillid()));
                            viewLayout();
                        });

            } else {
                ui.makeDialog("Überprüfe bitte deine Angaben auf Korrektheit");
            }
        });
    }

    private void viewLayout() {
        FormLayout viewLayout = profileLayout();
        majors = profileControl.getMajorOfStudent(userControl.getCurrentUser().getUserid());
        topics = profileControl.getTopicOfStudent(userControl.getCurrentUser().getUserid());
        skills = profileControl.getSkillOfStudent(userControl.getCurrentUser().getUserid());
        // set value of text fields to read only for profile view
        Stream.of(username, email, firstname, lastname, university, matrikelnumber).forEach(field -> {
            viewLayout.add(field);
            field.setReadOnly(true);
        });
        Stream.of(firstname, lastname, university, matrikelnumber).forEach(field -> field.setMaxLength(32));
        setAllGrids();
        Stream.of(gridMajors, gridTopics, gridSkills).forEach(grid -> {
            grid.setAllRowsVisible(true);
            viewLayout.add(grid);
            viewLayout.setColspan(grid, 2);
        });
        buttonLayout.add(editUser, changePasswd, deleteUser);
        layout.add(viewLayout, buttonLayout);
        editUser.addClickListener(buttonClickEvent -> {
            buttonLayout.removeAll();
            layout.removeAll();
            editLayout();
        });
    }

    private Grid<String> newEntriesGrid(Set<String> entries) {
        Grid<String> grid = new Grid<>();
        grid.addColumn(String::valueOf);
        grid.setAllRowsVisible(true);
        grid.addComponentColumn(newEntry -> {
            Button deleteButton = new Button(DELETE);
            deleteButton.addClickListener(e -> {
                entries.remove(newEntry);
                grid.setItems(entries);
            });
            return deleteButton;
        });
        return grid;
    }

    private FormLayout newEntryLayout(TextField input, Set<String> entries, Grid<String> grid) {
        FormLayout entryForm = new FormLayout();
        input.setMaxLength(32);
        Button saveButton = new Button("Hinzufügen");
        saveButton.addClickListener(e -> {
            if (!input.getValue().isBlank()) {
                entries.add(input.getValue());
                grid.setItems(entries);
            }
            input.clear();
        });
        entryForm.add(input, saveButton);
        return entryForm;
    }

    private void setStudentBinder() {
        studentBinder.setBean(mapper.map(userControl.getStudentProfile(userControl.getCurrentUser().getUserid()), StudentDTOImpl.class));
        studentBinder
                .forField(matrikelnumber)
                .asRequired("Darf nicht leer sein")
                .withValidator(mn -> mn.matches("-?\\d+")
                        && mn.length() <= 7, "Keine gültige Matrikelnummer")
                .withValidator(mn -> mn.equals(userControl.getStudentProfile(userControl.getCurrentUser().getUserid()).getMatrikelnumber())
                        || userControl.checkMatrikelnumberUnique(mn), "Matrikelnummer existiert bereits")
                .bind(StudentDTOImpl::getMatrikelnumber, StudentDTOImpl::setMatrikelnumber);
        studentBinder.bindInstanceFields(this);
    }

}
