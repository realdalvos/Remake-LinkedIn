package org.hbrs.se2.project.control;

import com.vaadin.flow.component.UI;
import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.StudentDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.repository.CompanyRepository;
import org.hbrs.se2.project.repository.StudentRepository;
import org.hbrs.se2.project.repository.UserRepository;
import org.hbrs.se2.project.services.impl.ValidationService;
import org.hbrs.se2.project.util.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserControl {

    @Autowired
    UserRepository userRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    ValidationService validationService;

    /**
     * Get the currently logged-in user
     * @return UserDTO of the current user
     */
    public UserDTO getCurrentUser() {
        return (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
    }

    /**
     * Get the user of a specific user ID
     * @param userid User ID
     * @return UserDTO of the user matching the ID
     */
    public UserDTO getUserByUserid(int userid) {
        return userRepository.findByUserid(userid);
    }

    /**
     * Get the student of a specific user ID
     * @param userid User ID
     * @return StudentDTO of the student matching the user ID
     */
    public StudentDTO getStudentProfile(int userid) {
        return studentRepository.findByUserid(userid);
    }

    /**
     * Get the company of a specific user ID
     * @param userid User ID
     * @return CompanyDTO of the company matching the user ID
     */
    public CompanyDTO getCompanyProfile(int userid) {
        return companyRepository.findByUserid(userid);
    }

    /**
     * Checks if a given username is already taken
     * @param username Username String
     * @return Boolean indicating uniqueness status
     */
    public boolean checkUsernameUnique(String username) {
        return validationService.checkUsernameUnique(username);
    }

    /**
     * Checks if a given email is already taken
     * @param email Email String
     * @return Boolean indicating uniqueness status
     */
    public boolean checkEmailUnique(String email) {
        return validationService.checkEmailUnique(email);
    }

    /**
     * Checks if a given matrikelnumber is already taken
     * @param matrikelnumber Matrikelnumber String
     * @return Boolean indicating uniqueness status
     */
    public boolean checkMatrikelnumberUnique(String matrikelnumber) {
        return validationService.checkMatrikelnumberUnique(matrikelnumber);
    }

}
