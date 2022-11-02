package org.hbrs.se2.project.views.studentViews;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.views.AppView;

/**
 * Student - Jobs list View
 */
@Route(value = Globals.Pages.JOBS_VIEW, layout = AppView.class)
@PageTitle("Jobs")
public class JobsView extends Div {
}
