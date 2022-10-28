package org.hbrs.se2.project.views.companyViews;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.views.AppView;

/**
 * Company - My Ads List View
 */
@Route(value = Globals.Pages.MYADS_VIEW, layout = AppView.class)
@PageTitle("My Ads")
public class MyAdsView extends Div {
}
