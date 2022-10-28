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
import org.hbrs.se2.project.views.studientViews.JobsView;

import java.util.Optional;

@Route(value="main")
@PWA(name="HBRS Collab", shortName = "HBRScollab", enableInstallPrompt = false)
public class AppView extends AppLayout implements BeforeEnterObserver {

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
        // Anzeige des Toggles über den Drawer
        setPrimarySection(Section.DRAWER);

        // Erstellung der horizontalen Statusleiste (Header)
        addToNavbar(true, createHeaderContent());

        // Erstellung der vertikalen Navigationsleiste (Drawer)
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));
    }

    private boolean checkIfUserIsLoggedIn() {
        // Falls der Benutzer nicht eingeloggt ist, dann wird er auf die Startseite gelenkt
        UserDTO userDTO = this.getCurrentUser();
        if (userDTO == null) {
            UI.getCurrent().navigate(Globals.Pages.LOGIN_VIEW);
            return false;
        }
        return true;
    }

    private Component createHeaderContent() {
        authorizationControl = new AuthorizationControl();
        // Ein paar Grund-Einstellungen. Alles wird in ein horizontales Layout gesteckt.
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        layout.getThemeList().set("dark", true);
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode( FlexComponent.JustifyContentMode.EVENLY );

        // Hinzufügen des Toogle ('Big Mac') zum Ein- und Ausschalten des Drawers
        layout.add(new DrawerToggle());
        viewTitle = new H1();
        viewTitle.setWidthFull();
        layout.add( viewTitle );

        // Interner Layout
        HorizontalLayout topRightPanel = new HorizontalLayout();
        topRightPanel.setWidthFull();
        topRightPanel.setJustifyContentMode( FlexComponent.JustifyContentMode.END );
        topRightPanel.setAlignItems( FlexComponent.Alignment.CENTER );

        // Der Name des Users wird später reingesetzt, falls die Navigation stattfindet
        helloUser = new H4();
        topRightPanel.add(helloUser);

        // Logout-Button am rechts-oberen Rand.
        MenuBar bar = new MenuBar();


        // Only if role is company
        if(this.authorizationControl.hasUserRole(this.getCurrentUser(), Globals.Roles.company)) {
            MenuItem item1 = bar.addItem("Create new Job Ad", e -> navigateToNewJob());
        } // else if for student case

        // for all roles
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
     * Hinzufügen der vertikalen Leiste (Drawer)
     * Diese besteht aus dem Logo ganz oben links sowie den Menu-Einträgen (menu items).
     * Die Menu Items sind zudem verlinkt zu den internen Tab-Components.
     * @param menu
     * @return
     */
    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);

        HorizontalLayout logoLayout = new HorizontalLayout();

        // Hinzufügen des Logos
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.add(new H1("HBRS Collab"));

        // Hinzufügen des Menus inklusive der Tabs
        layout.add(logoLayout, menu);
        return layout;
    }

    /**
     * Erzeugung des Menu auf der vertikalen Leiste (Drawer)
     * @return
     */
    private Tabs createMenu() {

        // Anlegen der Grundstruktur
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");

        // Anlegen der einzelnen Menuitems

        tabs.add(createMenuItems());
        return tabs;
    }


    private Component[] createMenuItems() {
        // get authentication control
        authorizationControl = new AuthorizationControl();
        Tab[] tabs = new Tab[]{};

        // has the user the role "student" he has the tab "Jobs"
        if(this.authorizationControl.hasUserRole(this.getCurrentUser(), Globals.Roles.student)) {
            System.out.println("User is student");
            //Tab[] studentTabs = new Tab[]{createTab("Jobs", JobsView.class)};
            // ToDo für die Teams: Weitere Tabs aus ihrem Projekt hier einfügen!
            // with Utils.append method
            tabs = Utils.append(tabs, createTab("Jobs", JobsView.class));
        } else
            // has the user the role "company" they have the tab "My Ads"
            if(this.authorizationControl.hasUserRole(this.getCurrentUser(), Globals.Roles.company)) {
                System.out.println("User is company");
                //Tab[] companyTabs = new Tab[]{createTab("My Ads", MyAdsView.class)};
                tabs = Utils.append(tabs, createTab("My Ads", MyAdsView.class));
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

        // Falls der Benutzer nicht eingeloggt ist, dann wird er auf die Startseite gelenkt
        if ( !checkIfUserIsLoggedIn() ) return;

        // Der aktuell-selektierte Tab wird gehighlighted.
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);

        // Setzen des aktuellen Names des Tabs
        viewTitle.setText(getCurrentPageTitle());

        // Setzen des Vornamens von dem aktuell eingeloggten Benutzer
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
     * Methode wird vor der eigentlichen Darstellung der UI-Components aufgerufen.
     * Hier kann man die finale Darstellung noch abbrechen, wenn z.B. der Nutzer nicht eingeloggt ist
     * Dann erfolgt hier ein ReDirect auf die Login-Seite. Eine Navigation (Methode navigate)
     * ist hier nicht möglich, da die finale Navigation noch nicht stattgefunden hat.
     * Diese Methode in der AppLayout sichert auch den un-authorisierten Zugriff auf die innerliegenden
     * Views (hier: ShowCarsView und EnterCarView) ab.
     *
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
