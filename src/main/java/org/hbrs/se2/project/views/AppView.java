package org.hbrs.se2.project.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.contextmenu.MenuItem;
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
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.util.Utils;
import org.hbrs.se2.project.views.companyViews.MyAdsView;
import org.hbrs.se2.project.views.studentViews.JobsView;
import org.hbrs.se2.project.views.studentViews.ProfileView;

import java.util.Optional;

@Route(value="main")
@PWA(name="HBRS Collab", shortName = "HBRScollab", enableInstallPrompt = false)
public class AppView extends AppLayout implements BeforeEnterObserver {
//EditProfile branch123
    private Tabs menu;
    private H1 viewTitle;
    private H4 helloUser;
    private AuthorizationControl authorizationControl;

    public AppView() {
        if(getCurrentUser() == null) {
            System.out.println("Log: In Constructor of App View - No User given");
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
    private boolean checkIfUserIsLoggedIn() {
        // if user is not logged in navigate to login view
        UserDTO userDTO = this.getCurrentUser();
        if (userDTO == null) {
            UI.getCurrent().navigate(Globals.Pages.LOGIN_VIEW);
            return false;
        }
        return true;
    }
    private Component createHeaderContent() {
        authorizationControl = new AuthorizationControl();
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
        if(this.authorizationControl.hasUserRole(this.getCurrentUser(), Globals.Roles.company)) {
            MenuItem item1 = bar.addItem("Create new Job Ad", e -> navigateToNewJob());
        }

        // for all roles add following bar items
        MenuItem item2 = bar.addItem("Logout" , e -> logoutUser());
        topRightPanel.add(bar);

        layout.add( topRightPanel );
        return layout;
    }

    private void logoutUser() {
        UI ui = this.getUI().get();
        ui.getSession().close();
        ui.getPage().setLocation("/");
    }

    /**
     * add vertical bar (drawer)
     * it contains the menu entries
     * menu items are linked to intern tab components
     */
    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);

        HorizontalLayout logoLayout = new HorizontalLayout();

        // add logo
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.add(new H1("HBRS Collab"));

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
        // get authentication control
        authorizationControl = new AuthorizationControl();
        // tabs for vertical bar (drawer)
        Tab[] tabs = new Tab[]{};

        // if the user has the role "student" he has the tabs "Jobs"
        if(this.authorizationControl.hasUserRole(this.getCurrentUser(), Globals.Roles.student)) {
            System.out.println("User is student");
            tabs = Utils.append(tabs, createTab("Jobs", JobsView.class));
            tabs = Utils.append(tabs, createTab("Profile", ProfileView.class));


        } else
            // has the user the role "company" they have the tabs "My Ads"
            if(this.authorizationControl.hasUserRole(this.getCurrentUser(), Globals.Roles.company)) {
                System.out.println("User is company");
                tabs = Utils.append(tabs, createTab("My Ads", MyAdsView.class));
                tabs = Utils.append(tabs, createTab("Profile", ProfileView.class));

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

        // if user is not logged in navigate to login view
        if ( !checkIfUserIsLoggedIn() ) return;

        // selected tab is highlighted
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);

        // set name of current tab
        viewTitle.setText(getCurrentPageTitle());

        // set firstname of the current user
        helloUser.setText("Hello "  + this.getCurrentNameOfUser() );
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
        return getCurrentUser().getUsername();
    }

    private UserDTO getCurrentUser() {
        return (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
    }

    @Override
    /**
     * method is called before component call
     * final view can be canceled if user is not logged in
     * redirect to log in view
     * secures unauthorized access to intern views
     */
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (getCurrentUser() == null){
            beforeEnterEvent.rerouteTo(Globals.Pages.LOGIN_VIEW);
        }
    }

    private void navigateToNewJob() {
        UI.getCurrent().navigate(Globals.Pages.NEW_ADD_VIEW);
    }
}
