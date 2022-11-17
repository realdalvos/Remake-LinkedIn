package org.hbrs.se2.project.views.studentViews;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.textfield.TextField;
import org.hbrs.se2.project.control.ProfileControl;
import org.hbrs.se2.project.dtos.MajorDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.views.AppView;

import java.util.List;

@Route(value = Globals.Pages.PROFILE_VIEW, layout = AppView.class)
@PageTitle("Profile")
public class ProfileView extends Div {

    private TextField major = new TextField("Major");
    private TextField university = new TextField("University");

    private TextField topic = new TextField("Topics");
    private TextField skill = new TextField("Skills");

    Button save = new Button("save");
    Button delete = new Button("delete");
    Button undo = new Button("undo");
    Button showInputs = new Button("Show Inputs");

    Button deleteSelectedMajors = new Button("Delete Selected Majors");

    Button deleteSelectedTopics = new Button("Delete Selected Topics");

    Button deleteSelectedSkills = new Button("Delete Selected Skills");

    private final ProfileControl profileControl;

    public ProfileView(ProfileControl profileControl){
        this.profileControl = profileControl;
        setSizeFull();

        university.setValue(profileControl.getUniversityOfStudent(this.getCurrentUser().getUserid()));

        FormLayout formLayout =  new FormLayout();
        formLayout.add(major,university,delete,undo,topic,skill,save,showInputs);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0",1));
        add(formLayout);

        delete.addClickListener((buttonClickEvent -> {
            String universitySaved = profileControl.getUniversityOfStudent(this.getCurrentUser().getUserid());
            profileControl.updateUniversity("", this.getCurrentUser().getUserid());
            university.setValue(profileControl.getUniversityOfStudent(this.getCurrentUser().getUserid()));
            undo.addClickListener(buttonClickEvent1 -> {
                profileControl.updateUniversity(universitySaved, this.getCurrentUser().getUserid());
                university.setValue(universitySaved);
            });
        }));

        save.addClickListener(buttonClickEvent -> {
            if (major.getValue() != null && !major.getValue().equals("")) {
                profileControl.updateStudyMajor(major.getValue(), this.getCurrentUser().getUserid());
            }
            if (university.getValue() != null && !university.getValue().equals("")) {
                profileControl.updateUniversity(university.getValue(), this.getCurrentUser().getUserid());
            }
            // topic input is just being printed out in the console
            // there is no call of the function profileControl.updateTopics to update topics in the database
            if (topic.getValue() != null && !topic.getValue().equals("")) {
                profileControl.updateTopics(topic.getValue(), this.getCurrentUser().getUserid());
            }
            // skills input is just being printed out in the console
            // there is no call of the function profileControl.updateSkills to update skills in the database
            if (skill.getValue() != null && !skill.getValue().equals("")) {
                profileControl.updateSkills(skill.getValue(), this.getCurrentUser().getUserid());
            }
            UI.getCurrent().getPage().reload();
        });
        showInputs.addClickListener(buttonClickEvent -> {
            setAllGrids();
            showInputs.setText("Hide Inputs");
        });
    }
    public UserDTO getCurrentUser() {
        UserDTO userDTO = (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
        System.out.println(userDTO.getUserid());
        return userDTO;
    }

    private void setAllGrids() {
        List<String> majors = profileControl.getMajorOfStudent(this.getCurrentUser().getUserid());
        List<String> topics = profileControl.getTopicOfStudent(this.getCurrentUser().getUserid());
        List<String> skills = profileControl.getSkillOfStudent(this.getCurrentUser().getUserid());
        /*List<MajorDTO> majorDTOList = profileControl.getMajorOfStudentAsDTOLIST((this.getCurrentUser().getUserid()));

        Grid<MajorDTO> gridMajorDTO = new Grid<>();
        gridMajorDTO.setItems(majorDTOList);
        gridMajorDTO.setSelectionMode((Grid.SelectionMode.MULTI));
        gridMajorDTO.addColumn(MajorDTO::getMajor).setHeader("MajorDTOGRID");


         */
        Grid<String> gridMajors = new Grid<>();
        gridMajors.setSelectionMode(Grid.SelectionMode.MULTI);
        gridMajors.addColumn(ValueProvider.identity()).setHeader("Majors");
        gridMajors.setItems(majors);

        Grid<String> gridTopics = new Grid<>();
        gridTopics.setSelectionMode(Grid.SelectionMode.MULTI);
        gridTopics.addColumn(ValueProvider.identity()).setHeader("Topics");
        gridTopics.setItems(topics);

        Grid<String> gridSkills = new Grid<>();
        gridSkills.setSelectionMode(Grid.SelectionMode.MULTI);
        gridSkills.addColumn(ValueProvider.identity()).setHeader("Skills");
        gridSkills.setItems(skills);

        add(    gridMajors,deleteSelectedMajors,
                gridTopics,deleteSelectedTopics,
                gridSkills, deleteSelectedSkills
                );
    }
}
