package org.hbrs.se2.project.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.PWA;
import org.hbrs.se2.project.control.AuthorizationControl;
import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.helper.NavigateHandler;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.util.Utils;
import org.hbrs.se2.project.views.companyViews.CompanyInboxView;
import org.hbrs.se2.project.views.companyViews.CompanyProfileView;
import org.hbrs.se2.project.views.companyViews.MyAdsView;
import org.hbrs.se2.project.views.companyViews.SearchStudentsView;
import org.hbrs.se2.project.views.studentViews.JobsView;
import org.hbrs.se2.project.views.studentViews.StudentInboxView;
import org.hbrs.se2.project.views.studentViews.StudentProfileView;
import org.slf4j.Logger;

import java.util.Optional;

@PWA(name="HBRS Collab", shortName = "HBRScollab", enableInstallPrompt = false)
public class AppView extends AppLayout implements BeforeEnterObserver {
    private final Logger logger = Utils.getLogger(this.getClass().getName());
    private Tabs menu;
    private H1 viewTitle;
    private H4 helloUser;

    private final AuthorizationControl authorizationControl;

    public AppView(AuthorizationControl authorizationControl) {
        this.authorizationControl = authorizationControl;

        if(authorizationControl.getCurrentUser() == null) {
            logger.info("In Constructor of App View - No User given");
        } else {
            setUpUI();
        }
    }
    public void setUpUI() {
        // show toggles above the drawer
        setPrimarySection(Section.DRAWER);

        // create a horizontal status bar (header)
        addToNavbar(true, createHeaderContent());

        // create a vertical navigation bar (drawer)
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));
    }

    private Component createHeaderContent() {
        // a few basic settings
        // everything is set into the horizontal layout
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        layout.getThemeList().set("dark", true);
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode( FlexComponent.JustifyContentMode.EVENLY );

        // add toggle (big mac) to on and off drawer
        layout.add(new DrawerToggle());
        viewTitle = new H1();
        viewTitle.setWidthFull();
        layout.add( viewTitle );

        // intern layout
        HorizontalLayout topRightPanel = new HorizontalLayout();
        topRightPanel.setWidthFull();
        topRightPanel.setJustifyContentMode( FlexComponent.JustifyContentMode.END );
        topRightPanel.setAlignItems( FlexComponent.Alignment.CENTER );

        // name of user will be placed in if navigation succeeds
        helloUser = new H4();
        topRightPanel.add(helloUser);

        // log out button in the right upper corner
        MenuBar bar = new MenuBar();


        // Only if role is equal to company
        if(authorizationControl.hasUserRole(authorizationControl.getCurrentUser(), Globals.Roles.COMPANY)) {
            bar.addItem(getTranslation("view.main.bar.newJob"), e -> NavigateHandler.navigateToNewJob());
        }

        // for all roles add following bar items
        bar.addItem(getTranslation("view.main.bar.logout"), e -> authorizationControl.logoutUser());
        topRightPanel.add(bar);

        layout.add( topRightPanel );
        return layout;
    }

    /**
     * add vertical bar (drawer)
     * it contains the menu entries
     * menu items are linked to intern tab components
     */
    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);

        HorizontalLayout logoLayout = new HorizontalLayout();

        // add logo with alignment
        logoLayout.setId("logo");
        H1 hbrsc = new H1("HBRS Collab");
        hbrsc.getElement().getStyle().set("font-size","45px"); // changed font size
        hbrsc.getElement().getStyle().set("text-align","center"); // logo is now in center
        logoLayout.add(hbrsc);

        // add menu with tabs
        layout.add(logoLayout, menu);
        return layout;
    }

    /**
     * Erzeugung des Menu auf der vertikalen Leiste (Drawer)
     * @return
     *
     * create menu on vertical bar (drawer)
     */
    private Tabs createMenu() {

        // create basic structure
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");

        // add menu items
        tabs.add(createMenuItems());
        return tabs;
    }


    private Component[] createMenuItems() {
        // tabs for vertical bar (drawer)
        Tab[] tabs = new Tab[]{};

        // if the user has the role "student" he has the tabs "Jobs"
        if(authorizationControl.hasUserRole(authorizationControl.getCurrentUser(), Globals.Roles.STUDENT)) {
            logger.info("User is student");
            tabs = Utils.append(tabs, createTab(getTranslation("view.main.nav.jobs"), JobsView.class));
            tabs = Utils.append(tabs, createTab(getTranslation("view.main.nav.profile"), StudentProfileView.class));
            tabs = Utils.append(tabs, createTab("Kommunikation", StudentInboxView.class));
        } else
            // has the user the role "company" they have the tabs "My Ads"
            if(authorizationControl.hasUserRole(authorizationControl.getCurrentUser(), Globals.Roles.COMPANY)) {
                logger.info("User is company");
                boolean banned = authorizationControl.isBannedCompany(authorizationControl.getCurrentUser());

                if(!banned) {
                    logger.info("User is company and not banned");
                    tabs = Utils.append(tabs, createTab(getTranslation("view.main.nav.myjobs"), MyAdsView.class));
                    tabs = Utils.append(tabs, createTab("Studentensuche", SearchStudentsView.class));
                    tabs = Utils.append(tabs, createTab(getTranslation("view.main.nav.profile"), CompanyProfileView.class));
                    tabs = Utils.append(tabs, createTab("Kommunikation", CompanyInboxView.class));
                } else {
                    logger.info("User is company and banned!");
                }
            }
        return tabs;
    }
    private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();

        // selected tab is highlighted
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);

        // set name of current tab
        viewTitle.setText(getCurrentPageTitle());

        // set firstname of the current user
        helloUser.setText(getTranslation("view.main.h1.greeting") + " " + this.getCurrentNameOfUser() );
    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren().filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }

    private String getCurrentNameOfUser() {
        return authorizationControl.getCurrentUser().getUsername();
    }

    /**
     * method is called before component call
     * final view can be canceled if user is not logged in
     * redirect to log in view
     * secures unauthorized access to intern views
     * @param beforeEnterEvent Takes an BeforeEnterEvent object
     */
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (authorizationControl.getCurrentUser() == null){
            beforeEnterEvent.rerouteTo(Globals.Pages.LOGIN_VIEW);
        }
    }
}
