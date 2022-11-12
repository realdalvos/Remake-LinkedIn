package org.hbrs.se2.project.views.companyViews;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.control.JobControl;
import org.hbrs.se2.project.control.LoginControl;
import org.hbrs.se2.project.dtos.JobDTO;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.views.AppView;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Company - My Ads List View
 */
@Route(value = Globals.Pages.MYADS_VIEW, layout = AppView.class)
@PageTitle("Meine Stellen")
public class MyAdsView extends Div {
    @Autowired
    public MyAdsView(JobControl jobcontrol, LoginControl logincontrol){

        Grid<JobDTO> grid = new Grid<>();

        grid.setItems(jobcontrol.getAllCompanyJobs(jobcontrol.getCompanyByUserid(logincontrol.getCurrentUser().getUserid()).getCompanyid()));
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);


        grid.addColumn(JobDTO::getTitle).setHeader("Titel").setSortable(true);
        grid.addColumn(JobDTO::getDescription).setHeader("Beschreibung");
        grid.addColumn(JobDTO::getSalary).setHeader("Bezahlung").setSortable(true);


        add(grid);
    }
}
