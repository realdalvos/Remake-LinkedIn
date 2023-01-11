package org.hbrs.se2.project.views.companyViews;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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

    // A Company can have all students displayed with the following button
    private final  Button allStudentsButton = new Button("Alle Studenten");

    private final Grid<StudentDTO> grid = new Grid<>();
    private Grid<MajorDTO> gridMajors;
    private Grid<TopicDTO> gridTopics;
    private Grid<SkillDTO> gridSkills;
    private final HorizontalLayout layout = new HorizontalLayout();
    private final HorizontalLayout topLayout = new HorizontalLayout();
    private final ProfileControl profileControl;

    public SearchStudentsView(ProfileControl profileControl, UserControl userControl, CommonUIElementProvider ui) {
        this.ui = ui;
        this.profileControl = profileControl;

        grid.setItemDetailsRenderer(new ComponentRenderer<>(student -> {
            FormLayout formLayout = new FormLayout();

            final TextField username = new TextField("Benutzername");
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
            FormLayout gridLayout = new FormLayout(gridMajors, gridTopics, gridSkills);
            gridLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 3));
            Button contact = new Button("Kontaktieren");
            contact.addClickListener(event ->
                    ui.makeConversationDialogCompany(userControl.getCompanyProfile(userControl.getCurrentUser().getUserid()).getCompanyid(), student.getStudentid()));
            return new VerticalLayout(formLayout, gridLayout, contact);
        }));

        // Row Stripes and Column Borders for improved usability and continuity (similar to Job List View of student)
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        createLayouts();
        /*
        with introductionText() below the career page will show information and help the user
        guide through the option of either filtering the job ads or showing all available job ads
        */
        add(ui.introductionText("Finde hier deinen zukünftigen Angestellten",
                "Finde Studenten mit passenden Fähigkeiten, Themen, Studiengängen und Universität. " ));

        // Adding contactText to inform the company of the possibility of starting a conversation with a student after seeing the profile
        H3 contactText = new H3("Du hast auch die Möglichkeit nach dem Ansehen des Profils eine Person zu kontaktieren! ");

        contactText.getElement().getStyle().set("font-size", "20px");
        contactText.getElement().getStyle().set("text-align", "center");


        add(contactText);
        add(topLayout);
        add(grid);
    }

    private void createGrids(StudentDTO student) {
        gridMajors = new Grid<>();
        gridTopics = new Grid<>();
        gridSkills = new Grid<>();
        // Create grids for skills, topics and majors
        gridMajors.setAllRowsVisible(true);
        gridMajors.addColumn(MajorDTO::getMajor).setHeader("Studiengänge:");
        gridMajors.setItems(profileControl.getMajorOfStudent(student.getUserid()));
        gridTopics.setAllRowsVisible(true);
        gridTopics.addColumn(TopicDTO::getTopic).setHeader("Interessengebiete:");
        gridTopics.setItems(profileControl.getTopicOfStudent(student.getUserid()));
        gridSkills.setAllRowsVisible(true);
        gridSkills.addColumn(SkillDTO::getSkill).setHeader("Fähigkeiten:");
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
        searchField.setWidth("450px");
        searchButton.setWidth("15%");
        allStudentsButton.setWidth("15%");

        searchField.setPlaceholder("Studentensuche");
        Stream.of(searchField, searchButton, allStudentsButton).forEach(element -> element.getStyle().set("margin-center", "auto"));

        // Center Alignment
        topLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        topLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        // adding TextField and 2 buttons
        topLayout.add(searchField, searchButton, allStudentsButton);

        // improved spacing - visible on buttons and textfield
        layout.setSpacing(false);
        layout.getThemeList().add("spacing-m");

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

        // allStudentsButton will show all registrated students
        allStudentsButton.addClickListener(event -> grid.setItems(profileControl.getStudentsMatchingKeyword("")));
    }

}
