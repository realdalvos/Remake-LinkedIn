package org.hbrs.se2.project.helper;

import com.vaadin.flow.component.UI;
import org.hbrs.se2.project.util.Globals;

public class navigateHandler {

    public static void navigateToLoginPage() {
        UI.getCurrent().navigate(Globals.Pages.LOGIN_VIEW);
    }
    public static void navigateToMainPage() {
        UI.getCurrent().navigate(Globals.Pages.MAIN_VIEW);
    }

    public static void navigateToRegisterCompanyPage() {
        UI.getCurrent().navigate(Globals.Pages.REGISTER_COMPANY_VIEW);
    }

    public static void navigateToRegisterStudentPage() {
        UI.getCurrent().navigate(Globals.Pages.REGISTER_STUDENT_VIEW);
    }

    public static void navigateToNewJob() {
        UI.getCurrent().navigate(Globals.Pages.NEW_ADD_VIEW);
    }
    public static void navigateToMyAdsView() {
        UI.getCurrent().navigate(Globals.Pages.MYADS_VIEW);
    }
}
