package org.hbrs.se2.project.control;

import com.vaadin.flow.component.UI;
import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.StudentDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.repository.CompanyRepository;
import org.hbrs.se2.project.repository.StudentRepository;
import org.hbrs.se2.project.repository.UserRepository;
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

    public UserDTO getCurrentUser() {
        return (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
    }

    public UserDTO getUserByUserid(int userid) {
        return userRepository.findByUserid(userid);
    }

    public StudentDTO getStudentProfile(int userid) {
        return studentRepository.findByUserid(userid);
    }

    public CompanyDTO getCompanyProfile(int userid) {
        return companyRepository.findByUserid(userid);
    }

}
