package org.hbrs.se2.project.helper;

import com.vaadin.flow.router.RouteConfiguration;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.views.AppView;
import org.hbrs.se2.project.views.companyViews.MyAdsView;
import org.hbrs.se2.project.views.companyViews.NewJobAdView;
import org.hbrs.se2.project.views.companyViews.RegisterCompanyView;
import org.hbrs.se2.project.views.studentViews.JobsView;
import org.hbrs.se2.project.views.studentViews.ProfileView;
import org.hbrs.se2.project.views.studentViews.RegisterStudentView;

public class AccessHandler {
    /**
     * Set specific access for the user, depending on the role given to the user.*/
    public static void setAccess(UserDTO user) {
        RouteConfiguration configuration = RouteConfiguration
                .forSessionScope();

        Class defaultView;
        if (user.getRole().equals(Globals.Roles.company)) {  //If company
            configuration.setAnnotatedRoute(MyAdsView.class);
            configuration.setAnnotatedRoute(NewJobAdView.class);
            defaultView = MyAdsView.class;
        } else {    //else student
            configuration.setAnnotatedRoute(JobsView.class);
            configuration.setAnnotatedRoute(ProfileView.class);
            defaultView = JobsView.class;
        }

        configuration.setRoute("", defaultView, AppView.class);
        configuration.removeRoute(RegisterCompanyView.class);
        configuration.removeRoute(RegisterStudentView.class);
    }

    /**Sets the default access. A user that is not logged in can visit both register pages and the login page.*/
    public static void setDefaultAccess() {
        RouteConfiguration configuration = RouteConfiguration
                .forSessionScope();

        //Removing both register views, otherwise there is an error when reloading the login page because
        //the views would be bound twice to the same path
        if( !configuration.isRouteRegistered(RegisterStudentView.class) ) {
            configuration.setAnnotatedRoute(RegisterStudentView.class);
        }
        if ( !configuration.isRouteRegistered(RegisterCompanyView.class) ) {
            configuration.setAnnotatedRoute(RegisterCompanyView.class);
        }
    }
}
