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
@PageTitle("My Ads")
public class MyAdsView extends Div {
    private final JobControl jobcontrol;
    private final LoginControl logincontrol;

    @Autowired
    public MyAdsView(JobControl jobcontrol, LoginControl logincontrol){
        this.jobcontrol = jobcontrol;
        this.logincontrol = logincontrol;

        Grid<JobDTO> grid = new Grid<>();

        grid.setItems(this.jobcontrol.getAllCompanyJobs(this.jobcontrol.getCompanyByUserid(this.logincontrol.getCurrentUser().getUserid()).getCompanyid()));
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);


        grid.addColumn(JobDTO::getTitle).setHeader("Title").setSortable(true);
        grid.addColumn(JobDTO::getDescription).setHeader("Description");
        grid.addColumn(JobDTO::getSalary).setHeader("Salary").setSortable(true);


        add(grid);
    }
}
