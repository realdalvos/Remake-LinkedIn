package org.hbrs.se2.project.control.factories;

import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.dtos.impl.CompanyDTOImpl;
import org.hbrs.se2.project.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.entities.Company;
import org.hbrs.se2.project.services.factory.EntityCreationService;
import org.hbrs.se2.project.util.Globals;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class CompanyFactoryTest {
    static EntityCreationService entityCreationService;
    UserDTO testUser;
    CompanyDTO testCompany;

    @BeforeAll
    static void init() {
        entityCreationService = new EntityCreationService();
    }

    @BeforeEach
    void setUp() {
        testUser = new UserDTOImpl("JUnitTest", "SicheresPasswort", "testUser@JUnitTest.de", Globals.Roles.company);
        testUser.setUserid(99999);

        testCompany = new CompanyDTOImpl(testUser.getUserid(), "JUnitTestCompany", "Test-Industry", false);
        testCompany.setCompanyid(1);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Testing if the CompanyFactory works as expected.")
    void testCreateCompany() {
        Company companyFromFactory = entityCreationService.companyFactory(testUser, testCompany).createEntity();
        assertNotNull(companyFromFactory, "createCompany Method should return a Instance of Company and not null.");

        //Checking the values of the returned company
        assertEquals(testCompany.getUserid(), companyFromFactory.getUserid(), "UserId should match.");
        assertEquals(testCompany.getName(), companyFromFactory.getName(), "Name should match.");
        assertEquals(testCompany.getIndustry(), companyFromFactory.getIndustry(), "Industry should match.");
        assertEquals(testCompany.getCompanyid(), companyFromFactory.getCompanyid(), "CompanyID should match.");
        assertEquals(testCompany.getBanned(), companyFromFactory.isBanned(), "Banned boolean should match.");
    }
}