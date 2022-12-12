package org.hbrs.se2.project.helper;

import com.vaadin.flow.router.RouteConfiguration;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.views.AppView;
import org.hbrs.se2.project.views.companyViews.*;
import org.hbrs.se2.project.views.studentViews.JobsView;
import org.hbrs.se2.project.views.studentViews.StudentInboxView;
import org.hbrs.se2.project.views.studentViews.StudentProfileView;
import org.hbrs.se2.project.views.studentViews.RegisterStudentView;

public class AccessHandler {
    /**
     * Set specific access for the user, depending on the role given to the user.*/
    public static void setAccess(UserDTO user) {
        RouteConfiguration configuration = RouteConfiguration
                .forSessionScope();

        /*setAnnotatedRoute activates the Route specified in given View for this session
            When adding a new view, its Route also has to be set to "RegisterAtStartup = false" and
            get activated here with setAnnotatedRoute(NewView.class). */
        Class defaultView;
        if (user.getRole().equals(Globals.Roles.company)) {  //If company
            configuration.setAnnotatedRoute(MyAdsView.class);
            configuration.setAnnotatedRoute(NewJobAdView.class);
            configuration.setAnnotatedRoute(SearchStudentsView.class);
            configuration.setAnnotatedRoute(CompanyProfileView.class);
            configuration.setAnnotatedRoute(CompanyInboxView.class);

            defaultView = MyAdsView.class;
        } else {    //else student
            configuration.setAnnotatedRoute(JobsView.class);
            configuration.setAnnotatedRoute(StudentProfileView.class);
            configuration.setAnnotatedRoute(StudentInboxView.class);

            defaultView = JobsView.class;
        }

        //mapping path "", which previously led to the LoginView, to defaultView which differs in case of student and company
        configuration.setRoute("", defaultView, AppView.class);
        //removing Route to RegisterViews
        configuration.removeRoute(RegisterCompanyView.class);
        configuration.removeRoute(RegisterStudentView.class);
    }

    /**
     * Sets the default access. A user that is not logged in can visit both register pages and the login page.*/
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
