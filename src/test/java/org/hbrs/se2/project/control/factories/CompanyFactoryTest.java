package org.hbrs.se2.project.control.factories;

import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.dtos.impl.CompanyDTOImpl;
import org.hbrs.se2.project.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.entities.Company;
import org.hbrs.se2.project.util.Globals;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompanyFactoryTest {

    UserDTO testUser;
    CompanyDTO testCompany;

    @BeforeEach
    void setUp() {
        UserDTOImpl testUserImpl = new UserDTOImpl();
        testUserImpl.setUsername("JUnitTest");
        testUserImpl.setPassword("SicheresPasswort");
        testUserImpl.setEmail("testUser@JUnitTest.de");
        testUserImpl.setUserid(99999);
        testUserImpl.setRole(Globals.Roles.company);

        testUser = testUserImpl;

        CompanyDTOImpl testCompanyImpl = new CompanyDTOImpl(testUser.getUserid(), "JUnitTestCompany", "Test-Industry", false);

        testCompany = testCompanyImpl;
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Testing if the CompanyFactory works as expected.")
    void testCreateCompany() {
        Company companyFromFactory = CompanyFactory.createCompany(testCompany, testUser);
        assertNotNull(companyFromFactory, "createCompany Method should return a Instance of Company and not null.");

        //Checking the values of the returned company
        assertEquals(testCompany.getUserid(), companyFromFactory.getUserid(), "UserId should match.");
        assertEquals(testCompany.getName(), companyFromFactory.getName(), "Name should match.");
        assertEquals(testCompany.getIndustry(), companyFromFactory.getIndustry(), "Industry should match.");
        assertEquals(testCompany.getCompanyid(), companyFromFactory.getCompanyid(), "CompanyID should match.");
        assertEquals(testCompany.getBanned(), companyFromFactory.isBanned(), "Banned boolean should match.");
    }
}