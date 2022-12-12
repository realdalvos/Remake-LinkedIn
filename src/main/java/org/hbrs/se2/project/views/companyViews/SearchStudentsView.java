package org.hbrs.se2.project.views.companyViews;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.control.ProfileControl;
import org.hbrs.se2.project.control.UserControl;
import org.hbrs.se2.project.dtos.*;
import org.hbrs.se2.project.services.ui.CommonUIElementProvider;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.views.AppView;

import java.util.stream.Stream;

/**
 * Company - Students List View
 */
@Route(value = Globals.Pages.SEARCH_STUDENT_VIEW, layout = AppView.class, registerAtStartup = false)
@PageTitle("Student")
public class SearchStudentsView extends Div {
    CommonUIElementProvider ui;
    private final TextField searchField = new TextField();
    private final Button searchButton = new Button("Suchen");
    private final Grid<StudentDTO> grid = new Grid<>();
    private Grid<MajorDTO> gridMajors;
    private Grid<TopicDTO> gridTopics;
    private Grid<SkillDTO> gridSkills;
    private final HorizontalLayout layout = new HorizontalLayout();
    private final HorizontalLayout topLayout = new HorizontalLayout();
    private final ProfileControl profileControl;
    private final UserControl userControl;

    public SearchStudentsView(ProfileControl profileControl, UserControl userControl, CommonUIElementProvider ui) {
        this.ui = ui;
        this.profileControl = profileControl;
        this.userControl = userControl;

        grid.setItemDetailsRenderer(new ComponentRenderer<>(student -> {
            FormLayout formLayout = new FormLayout();

            final TextField username = new TextField("Username");
            final TextField email = new TextField("Email");

            UserDTO userDTO = userControl.getUserByUserid(student.getUserid());
            // get username of student
            username.setValue(userDTO.getUsername());
            // get email of student
            email.setValue(userDTO.getEmail());

            Stream.of(username, email).forEach(
                    field -> {
                        field.setReadOnly(true);
                        formLayout.add(field);
                    }
            );
            createGrids(student);
            formLayout.add(gridMajors);
            formLayout.add(gridSkills);
            formLayout.add(gridTopics);
            Button contact = new Button("Kontaktieren");
            contact.addClickListener(event ->
                    ui.makeConversationDialogCompany(userControl.getCompanyProfile(userControl.getCurrentUser().getUserid()).getCompanyid(), student.getStudentid()));
            formLayout.add(contact);
            return formLayout;
        }));

        createLayouts();
        /*
        with introductionText() below the career page will show information and help the user
        guide through the option of either filtering the job ads or showing all available job ads
        */
        add(ui.introductionText("Finden Sie hier Ihren zukünftigen Angestellten", "Finden Sie Studenten mit passenden Skills, Themen, Studiengang und Universität"));
        add(topLayout);
        add(grid);
    }

    private void createGrids(StudentDTO student) {
        gridMajors = new Grid<>();
        gridSkills = new Grid<>();
        gridTopics = new Grid<>();
        // Create grids for skills, topics and majors
        gridMajors.setAllRowsVisible(true);
        gridMajors.addColumn(MajorDTO::getMajor).setHeader("Majors:");
        gridMajors.setItems(profileControl.getMajorOfStudent(student.getUserid()));
        gridTopics.setAllRowsVisible(true);
        gridTopics.addColumn(TopicDTO::getTopic).setHeader("Topics:");
        gridTopics.setItems(profileControl.getTopicOfStudent(student.getUserid()));
        gridSkills.setAllRowsVisible(true);
        gridSkills.addColumn(SkillDTO::getSkill).setHeader("Skills:");
        gridSkills.setItems(profileControl.getSkillOfStudent(student.getUserid()));
    }

    private void createLayouts() {
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        searchField.setClearButtonVisible(true); // possibility to delete filter words

        // changing width of textField, buttonFilter and buttonAllJobs to improve on usability
        searchField.setWidth("25");
        searchButton.setWidth("25%");

        searchField.setPlaceholder("Studentensuche");

        searchField.getStyle().set("margin-center", "auto");
        searchButton.getStyle().set("margin-center", "auto");

        // Center Alignment
        topLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        topLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        // adding TextField and 2 buttons
        topLayout.add(searchField);
        topLayout.add(searchButton);

        layout.add(topLayout);
        layout.add(new Label());
        layout.setWidth("100%");

        grid.addColumn(StudentDTO::getFirstname).setHeader("Vorname").setTextAlign(ColumnTextAlign.CENTER);
        grid.addColumn(StudentDTO::getLastname).setHeader("Nachname").setTextAlign(ColumnTextAlign.CENTER);
        grid.addColumn(StudentDTO::getUniversity).setHeader("Universität").setTextAlign(ColumnTextAlign.CENTER);
        grid.setAllRowsVisible(true);

        searchField.addKeyPressListener(Key.ENTER, event -> searchButton.clickInClient());
        // to implement
        searchButton.addClickListener(event -> {grid.setItems(profileControl.getStudentsMatchingKeyword(searchField.getValue())); searchField.clear();});

    }
}
