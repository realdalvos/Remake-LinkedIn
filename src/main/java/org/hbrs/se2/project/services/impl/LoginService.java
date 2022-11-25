package org.hbrs.se2.project.services.impl;

import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.RouteConfiguration;
import org.hbrs.se2.project.control.exception.DatabaseUserException;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.repository.UserRepository;
import org.hbrs.se2.project.services.LoginServiceInterface;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.views.AppView;
import org.hbrs.se2.project.views.LoginView;
import org.hbrs.se2.project.views.companyViews.MyAdsView;
import org.hbrs.se2.project.views.companyViews.NewJobAdView;
import org.hbrs.se2.project.views.companyViews.RegisterCompanyView;
import org.hbrs.se2.project.views.studentViews.JobsView;
import org.hbrs.se2.project.views.studentViews.ProfileView;
import org.hbrs.se2.project.views.studentViews.RegisterStudentView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements LoginServiceInterface {

    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserDTO userDTO = null;

    @Override
    public boolean authenticate(String username, String password) throws DatabaseUserException {
        UserDTO tmpUser = this.getUserWithJPA(username);

        if (tmpUser == null || !passwordEncoder.matches(password, tmpUser.getPassword().trim())) {
            return false;
        }
        this.userDTO = tmpUser;
        createRoutes(tmpUser);
        return true;
    }

    private void createRoutes(UserDTO user) {
        RouteConfiguration configuration = RouteConfiguration
                .forSessionScope();

        Class defaultView;
        if(user.getRole().equals(Globals.Roles.company)){  //If company
            configuration.setAnnotatedRoute(MyAdsView.class);
            configuration.setAnnotatedRoute(NewJobAdView.class);
            defaultView = MyAdsView.class;
        }
        else {    //else student
            configuration.setAnnotatedRoute(JobsView.class);
            configuration.setAnnotatedRoute(ProfileView.class);
            defaultView = JobsView.class;
        }
        configuration.setAnnotatedRoute(AppView.class);
        /*
        configuration.setRoute(Globals.Pages.LOGIN_VIEW, defaultView, AppView.class);
        configuration.setRoute(Globals.Pages.REGISTER_COMPANY_VIEW, defaultView, AppView.class);
        configuration.setRoute(Globals.Pages.REGISTER_STUDENT_VIEW, defaultView, AppView.class);*/
    }

    @Override
    public UserDTO getCurrentUser() {
        return this.userDTO;
    }

    /**Get User Object with jpa api from database.
     *
     * @param username Username
     * @return UserDTO from Datebase if there is a User in the Database with given username, otherwise null.
     * @throws DatabaseUserException Something went wrong with the Database
     */
    private UserDTO getUserWithJPA(String username) throws DatabaseUserException {
        UserDTO userTmp;
        try {
            userTmp = repository.findUserByUsername(username);
        } catch (org.springframework.dao.DataAccessResourceFailureException e) {
            throw new DatabaseUserException("A Failure occured while trying to connect to database with JPA");
        }
        return userTmp;
    }
}
