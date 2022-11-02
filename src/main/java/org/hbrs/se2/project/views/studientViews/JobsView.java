package org.hbrs.se2.project.views.studientViews;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.control.JobControl;
import org.hbrs.se2.project.dtos.impl.JobDTOImpl;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.views.AppView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Student - Jobs list View
 */
@Route(value = Globals.Pages.JOBS_VIEW, layout = AppView.class)
@PageTitle("Jobs")
public class JobsView extends Div {

    @Autowired
    private JobControl jobControl;

    public JobsView() {
        setSizeFull();

        TextField textField = new TextField("Search Jobs");

        Button button = new Button("Search");

        // Create a Grid bound to the list
        Grid<JobDTOImpl> grid = new Grid<>();
        grid.addColumn(JobDTOImpl::getTitle).setHeader("Title");
        grid.addColumn(JobDTOImpl::getSalary).setHeader("Salary");
        grid.addItemClickListener(event -> {
           System.out.println(event.getItem());
        });

        button.addClickListener(event -> {
            String keyword = textField.getValue();
            List<JobDTOImpl> jobs = jobControl.getJobsMatchingKeyword(keyword);
            grid.setItems(jobs);
        });
        add(textField);
        add(button);
        add(grid);
    }
}
