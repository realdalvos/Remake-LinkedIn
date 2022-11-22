package org.hbrs.se2.project.views.studentViews;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.textfield.TextField;
import org.hbrs.se2.project.control.ProfileControl;
import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.*;
import org.hbrs.se2.project.dtos.impl.StudentDTOImpl;
import org.hbrs.se2.project.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.util.Utils;
import org.hbrs.se2.project.views.AppView;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Route(value = Globals.Pages.PROFILE_VIEW, layout = AppView.class)
@PageTitle("Profile")
public class ProfileView extends Div {

    private final int CURRENT_USER = Utils.getCurrentUser().getUserid();

    private final Logger logger = Utils.getLogger(this.getClass().getName());

    private final TextField major = new TextField("Füge neuen Major hinzu:");
    private final TextField topic = new TextField("Füge neuen Topic hinzu:");
    private final TextField skill = new TextField("Füge neuen Skills hinzu:");
    private final TextField username = new TextField("Benutzername:");
    private final TextField firstname = new TextField("Vorname:");
    private final TextField lastname = new TextField("Nachname:");
    private final TextField email = new TextField("EMail-Adresse:");
    private final TextField university = new TextField("Universität:");
    private final TextField matrikelnumber = new TextField("Matrikelnummer:");

    private final Grid<MajorDTO> gridMajors = new Grid<>();
    private final Grid<TopicDTO> gridTopics = new Grid<>();
    private final Grid<SkillDTO> gridSkills = new Grid<>();

    private Button button;

    private final FormLayout formLayout = new FormLayout();

    private final Binder<UserDTOImpl> userBinder = new BeanValidationBinder<>(UserDTOImpl.class);
    private final Binder<StudentDTOImpl> studentBinder = new BeanValidationBinder<>(StudentDTOImpl.class);

    private final ModelMapper mapper = new ModelMapper();

    private final ProfileControl profileControl;

    public ProfileView(ProfileControl profileControl) {
        this.profileControl = profileControl;

        setSizeFull();
        add(formLayout);
        setAllGrids();
        viewLayout();
    }

    private void setAllGrids() {
        // Create grids for skills, topics and majors
        gridMajors.setHeightByRows(true);
        gridMajors.addColumn(MajorDTO::getMajor).setHeader("Majors:");
        gridMajors.setItems(profileControl.getMajorOfStudent(CURRENT_USER));
        gridTopics.setHeightByRows(true);
        gridTopics.addColumn(TopicDTO::getTopic).setHeader("Topics:");
        gridTopics.setItems(profileControl.getTopicOfStudent(CURRENT_USER));
        gridSkills.setHeightByRows(true);
        gridSkills.addColumn(SkillDTO::getSkill).setHeader("Skills:");
        gridSkills.setItems(profileControl.getSkillOfStudent(CURRENT_USER));
    }

    private void editLayout() {
        Grid<String> newMajorsGrid, newTopicsGrid, newSkillsGrid;
        List<String> newMajors, newTopics, newSkills;

        Stream.of(username, firstname, lastname, email, university, matrikelnumber).forEach(
                field -> {
                    field.setReadOnly(false);
                }
        );
        Binder.Binding<UserDTOImpl, String> checkUsernameBinding =
                userBinder
                        .forField(username)
                        .asRequired()
                        .withValidator(validation -> {
                            if (!username.getValue().equals(Utils.getCurrentUser().getUsername())){
                                return profileControl.checkUsernameUnique(username.getValue());
                            }
                            return true;
                        }, "Benutzername existiert bereits")
                        .bind(UserDTOImpl::getUsername, UserDTOImpl::setUsername);
        Binder.Binding<UserDTOImpl, String> checkEmailBinding =
                userBinder
                        .forField(email)
                        .asRequired()
                        .withValidator(validation -> {
                            if (!email.getValue().equals(Utils.getCurrentUser().getEmail())){
                                return profileControl.checkEmailUnique(email.getValue());
                            }
                            return true;
                        }, "Email existiert bereits")
                        .bind(UserDTOImpl::getEmail, UserDTOImpl::setEmail);
        Binder.Binding<StudentDTOImpl, String> checkMatrikelnumberBinding =
                studentBinder
                        .forField(matrikelnumber)
                        .asRequired()
                        .withValidator(validation -> {
                            if (!matrikelnumber.getValue().equals(profileControl.getStudentProfile(CURRENT_USER).getMatrikelnumber())){
                                return profileControl.checkMatrikelnumberUnique(matrikelnumber.getValue());
                            }
                            return true;
                        }, "Matrikelnummer existiert bereits")
                        .bind(StudentDTOImpl::getMatrikelnumber, StudentDTOImpl::setMatrikelnumber);

        username.addValueChangeListener(
                event -> checkUsernameBinding.validate());
        email.addValueChangeListener(
                event -> checkEmailBinding.validate());
        matrikelnumber.addValueChangeListener(
                event -> checkMatrikelnumberBinding.validate());

        gridMajors.addComponentColumn(major -> {
            Button deleteButton = new Button("Entfernen");
            deleteButton.addClickListener(e -> {
                profileControl.removeMajor(CURRENT_USER, major.getMajorid());
                gridMajors.setItems(profileControl.getMajorOfStudent(CURRENT_USER));
            });
            return deleteButton;
        });
        formLayout.addComponentAtIndex(7, newMajorsGrid = newEntriesGrid(newMajors = new ArrayList<>()));
        formLayout.addComponentAtIndex(8, newEntryLayout(major, newMajors, newMajorsGrid));

        gridTopics.addComponentColumn(topic -> {
            Button deleteButton = new Button("Entfernen");
            deleteButton.addClickListener(e -> {
                profileControl.removeTopic(CURRENT_USER, topic.getTopicid());
                gridTopics.setItems(profileControl.getTopicOfStudent(CURRENT_USER));
            });
            return deleteButton;
        });
        formLayout.addComponentAtIndex(10, newTopicsGrid = newEntriesGrid(newTopics = new ArrayList<>()));
        formLayout.addComponentAtIndex(11, newEntryLayout(topic, newTopics, newTopicsGrid));

        gridSkills.addComponentColumn(skill -> {
            Button deleteButton = new Button("Entfernen");
            deleteButton.addClickListener(e -> {
                profileControl.removeSkill(CURRENT_USER, skill.getSkillid());
                gridSkills.setItems(profileControl.getSkillOfStudent(CURRENT_USER));
            });
            return deleteButton;
        });
        formLayout.addComponentAtIndex(13, newSkillsGrid = newEntriesGrid(newSkills = new ArrayList<>()));
        formLayout.addComponentAtIndex(14, newEntryLayout(skill, newSkills, newSkillsGrid));

        button = new Button("Profil speichern");
        formLayout.addComponentAtIndex(15, button);
        button.addClickListener(buttonClickEvent -> {
            try {
                if (userBinder.isValid() && studentBinder.isValid()) {
                    profileControl.saveStudentData(
                            CURRENT_USER, userBinder.getBean(), studentBinder.getBean(), university.getValue(),
                            newMajors, newTopics, newSkills);
                    // reload page to get updated view
                    UI.getCurrent().getPage().reload();
                } else {
                    logger.info("Invalid Data Input");
                }
            } catch (DatabaseUserException e) {
                logger.error("Something went wrong with saving student data");
            }
        });
    }

    private void viewLayout() {
        // set value of text fields to read only for profile view
        Stream.of(username, firstname, lastname, email, university, matrikelnumber).forEach(
                field -> {
                    field.setReadOnly(true);
                    formLayout.add(field);
                }
        );
        userBinder.bindInstanceFields(this);
        studentBinder.bindInstanceFields(this);
        userBinder.setBean(mapper.map(Utils.getCurrentUser(), UserDTOImpl.class));
        studentBinder.setBean(mapper.map(profileControl.getStudentProfile(CURRENT_USER), StudentDTOImpl.class));
        button = new Button("Profil bearbeiten");
        formLayout.add(gridMajors, gridTopics, gridSkills, button);
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