package org.hbrs.se2.project.helper;

import com.vaadin.flow.component.UI;
import org.hbrs.se2.project.util.Globals;

public interface NavigateHandler {

    /**You cant go to the login page while being logged in, that would make no sense and lead to errors.*/
    static void navigateToLoginPage() {
        UI.getCurrent().navigate(Globals.Pages.LOGIN_VIEW);
    }
    static void navigateToMainPage() {
        UI.getCurrent().navigate(Globals.Pages.MAIN_VIEW);
    }

    /**Navigating to default page. If not logged in, the default page is the login page.
     * If logged in as company the default page is the MyAdsView, as student it is the JobsView.*/
    static void navigateToDefaultPage(){
        UI.getCurrent().navigate("");
    }

    static void navigateToRegisterCompanyPage() {
        UI.getCurrent().navigate(Globals.Pages.REGISTER_COMPANY_VIEW);
    }

    static void navigateToRegisterStudentPage() {
        UI.getCurrent().navigate(Globals.Pages.REGISTER_STUDENT_VIEW);
    }

    static void navigateToNewJob() {
        UI.getCurrent().navigate(Globals.Pages.NEW_ADD_VIEW);
    }
    public static void navigateToMyAdsView() {
        UI.getCurrent().navigate(Globals.Pages.MYADS_VIEW);
    }

    static void navigateToJobsView() {
        UI.getCurrent().navigate(Globals.Pages.JOBS_VIEW);
    }
}
