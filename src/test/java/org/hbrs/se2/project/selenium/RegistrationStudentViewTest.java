package org.hbrs.se2.project.selenium;

import jdk.jfr.Description;
import org.hbrs.se2.project.Application;
import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.StudentDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.repository.*;
import org.hbrs.se2.project.util.HelperForTests;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hbrs.se2.project.util.Globals.MAXIMUM_PAGE_LOADINGTIME;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Ignore("Will not run on jenkins")
public class RegistrationStudentViewTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    MajorRepository majorRepository;
    @Autowired
    TopicRepository topicRepository;
    @Autowired
    SkillRepository skillRepository;
    @Autowired
    CompanyDTO companyDTO;
    @Autowired
    UserDTO userDTO;
    @Autowired
    HelperForTests h;
    static String browser;
    WebDriver webDriver;
    WebElement textfieldFirstName;
    WebElement textfieldLastName;
    WebElement textfieldUsername;
    WebElement textfieldMatrikelnumber;
    WebElement textfieldEmail;
    WebElement textfieldPassword;
    WebElement textfieldPasswordRepeat;
    WebElement buttonRegister;
    @Value("${server.port: 8080}")
    private int port;

    @BeforeAll
    static void start() throws Exception {

        //Starts the Webserver (Needs a running server for the tests but roughly 35 Seconds to build)
        Application.main(new String[]{});
        browser = "chrome";
        try{
            HelperForTests.setDriverSystemProperties(browser);
            WebDriver checkDriver = new ChromeDriver();
            checkDriver.quit();
        }catch(Exception x){
            browser = "edge";
        }
        HelperForTests.setDriverSystemProperties(browser);
    }

    @BeforeEach
    void setUp(){
        h.registerTestStudent();
        h.deleteRegisteredTestStudents();
        if(browser.equals("chrome")) {
            webDriver = new ChromeDriver();
        } else if (browser.equals("edge")) {
            webDriver = new EdgeDriver();
        } else if (browser.equals("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("-headless");
            options.setProfile(new FirefoxProfile());
            webDriver = new FirefoxDriver(options);
        }
        webDriver.manage().window().maximize();
        webDriver.get("http://localhost:" + port + "/");
        webDriver.get("http://localhost:" + port + "/register-student");
        HelperForTests.synchronizedwait(webDriver,1000);
        buttonRegister = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-button[1]"));
        textfieldFirstName = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-horizontal-layout[2]/vaadin-form-layout/vaadin-text-field[1]"))
                .getShadowRoot().findElement(By.cssSelector("#vaadin-text-field-input-0 > slot:nth-child(2) > input"));
        textfieldLastName = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-horizontal-layout[2]/vaadin-form-layout/vaadin-text-field[2]"))
                .getShadowRoot().findElement(By.cssSelector("#vaadin-text-field-input-1 > slot:nth-child(2) > input"));
        textfieldUsername = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-horizontal-layout[2]/vaadin-form-layout/vaadin-text-field[3]"))
                .getShadowRoot().findElement(By.cssSelector("#vaadin-text-field-input-2 > slot:nth-child(2) > input"));
        textfieldMatrikelnumber = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-horizontal-layout[2]/vaadin-form-layout/vaadin-text-field[4]"))
                .getShadowRoot().findElement(By.cssSelector("#vaadin-text-field-input-3 > slot:nth-child(2) > input"));
        textfieldEmail = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-horizontal-layout[2]/vaadin-form-layout/vaadin-email-field"))
                .getShadowRoot().findElement(By.cssSelector("#vaadin-email-field-input-4 > slot:nth-child(2) > input[type=email]"));
        textfieldPassword = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-horizontal-layout[2]/vaadin-form-layout/vaadin-password-field[1]"))
                .getShadowRoot().findElement(By.cssSelector("#vaadin-password-field-input-5 > slot:nth-child(2) > input[type=password]"));
        textfieldPasswordRepeat = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-horizontal-layout[2]/vaadin-form-layout/vaadin-password-field[2]"))
                .getShadowRoot().findElement(By.cssSelector("#vaadin-password-field-input-6 > slot:nth-child(2) > input[type=password]"));
    }
    @AfterEach
    void tearDown(){
        if(userRepository.findByUsername("TestUserStudent") != null){
            userRepository.deleteById(userRepository.findByUsername("TestUserStudent").getUserid());
        }
        webDriver.quit();
    }
    @Test
    @Description("Registering correctly as a student")
    public void registerAsStudent(){
        textfieldFirstName.sendKeys("Stan");
        textfieldLastName.sendKeys("Student");
        textfieldUsername.sendKeys("TestUserStudent");
        textfieldMatrikelnumber.sendKeys("123456");
        textfieldEmail.sendKeys("testUser2@JUnitTest.de");
        textfieldPassword.sendKeys("SicheresPasswort123!");
        textfieldPasswordRepeat.sendKeys("SicheresPasswort123!");
        buttonRegister.click();
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
        UserDTO createdUser = userRepository.findByUsername("TestUserStudent");
        assertNotNull(createdUser);
        assertEquals("TestUserStudent",createdUser.getUsername());
        assertEquals("testUser2@JUnitTest.de",createdUser.getEmail());
        StudentDTO createdStudent = studentRepository.findByUserid(createdUser.getUserid());
        assertNotNull(createdStudent);
        assertEquals("Stan",createdStudent.getFirstname());
        assertEquals("Student",createdStudent.getLastname());
        assertEquals("123456",createdStudent.getMatrikelnumber());
        assertEquals("http://localhost:" + port + "/", webDriver.getCurrentUrl());

        //Testing the login with created account
        WebElement textfieldLoginUsername = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-login-form/vaadin-login-form-wrapper/form/vaadin-text-field/input"));
        WebElement textfieldLoginPassword = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-login-form/vaadin-login-form-wrapper/form/vaadin-password-field/input"));
        WebElement buttonLogin = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-login-form/vaadin-login-form-wrapper/form/vaadin-button"));
        textfieldLoginUsername.sendKeys("TestUserStudent");
        textfieldLoginPassword.sendKeys("SicheresPasswort123!");
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
        buttonLogin.click();
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
        assertEquals("http://localhost:" + port + "/jobs", webDriver.getCurrentUrl());
        userRepository.deleteById(createdUser.getUserid());
    }
    @Test
    @Description("register with first name input field left empty")
    public void emptyFields1(){
        textfieldLastName.sendKeys("Student");
        textfieldUsername.sendKeys("TestUserStudent");
        textfieldMatrikelnumber.sendKeys("123456");
        textfieldEmail.sendKeys("testUser2@JUnitTest.de");
        textfieldPassword.sendKeys("SicheresPasswort123!");
        textfieldPasswordRepeat.sendKeys("SicheresPasswort123!");
        buttonRegister.click();
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
        assertEquals("http://localhost:" + port + "/register-student", webDriver.getCurrentUrl());
        UserDTO createdUser = userRepository.findByUsername("TestUserStudent");
        assertNull(createdUser);
        WebElement buttonGoBack = webDriver.findElement(By.xpath("/html/body/vaadin-dialog-overlay/flow-component-renderer/div/vaadin-vertical-layout/vaadin-button"))
                .getShadowRoot().findElement(By.cssSelector("#button"));
        buttonGoBack.click();
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
    }
    @Test
    @Description("register with last name input field left empty")
    public void emptyFields2(){
        textfieldFirstName.sendKeys("Stan");
        textfieldUsername.sendKeys("TestUserStudent");
        textfieldMatrikelnumber.sendKeys("123456");
        textfieldEmail.sendKeys("testUser2@JUnitTest.de");
        textfieldPassword.sendKeys("SicheresPasswort123!");
        textfieldPasswordRepeat.sendKeys("SicheresPasswort123!");
        buttonRegister.click();
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
        assertEquals("http://localhost:" + port + "/register-student", webDriver.getCurrentUrl());
        UserDTO createdUser = userRepository.findByUsername("TestUserStudent");
        assertNull(createdUser);
        WebElement buttonGoBack = webDriver.findElement(By.xpath("/html/body/vaadin-dialog-overlay/flow-component-renderer/div/vaadin-vertical-layout/vaadin-button"))
                .getShadowRoot().findElement(By.cssSelector("#button"));
        buttonGoBack.click();
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
    }
    @Test
    @Description("register with user name input field left empty")
    public void emptyFields3(){
        textfieldFirstName.sendKeys("Stan");
        textfieldLastName.sendKeys("Student");
        textfieldMatrikelnumber.sendKeys("123456");
        textfieldEmail.sendKeys("testUser2@JUnitTest.de");
        textfieldPassword.sendKeys("SicheresPasswort123!");
        textfieldPasswordRepeat.sendKeys("SicheresPasswort123!");
        buttonRegister.click();
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
        assertEquals("http://localhost:" + port + "/register-student", webDriver.getCurrentUrl());
        UserDTO createdUser = userRepository.findByUsername("TestUserStudent");
        assertNull(createdUser);
        WebElement buttonGoBack = webDriver.findElement(By.xpath("/html/body/vaadin-dialog-overlay/flow-component-renderer/div/vaadin-vertical-layout/vaadin-button"))
                .getShadowRoot().findElement(By.cssSelector("#button"));
        buttonGoBack.click();
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
    }
    @Test
    @Description("register with matrikel number input field left empty")
    public void emptyFields4(){
        textfieldFirstName.sendKeys("Stan");
        textfieldLastName.sendKeys("Student");
        textfieldUsername.sendKeys("TestUserStudent");
        textfieldEmail.sendKeys("testUser2@JUnitTest.de");
        textfieldPassword.sendKeys("SicheresPasswort123!");
        textfieldPasswordRepeat.sendKeys("SicheresPasswort123!");
        buttonRegister.click();
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
        assertEquals("http://localhost:" + port + "/register-student", webDriver.getCurrentUrl());
        UserDTO createdUser = userRepository.findByUsername("TestUserStudent");
        assertNull(createdUser);
        WebElement buttonGoBack = webDriver.findElement(By.xpath("/html/body/vaadin-dialog-overlay/flow-component-renderer/div/vaadin-vertical-layout/vaadin-button"))
                .getShadowRoot().findElement(By.cssSelector("#button"));
        buttonGoBack.click();
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
    }
    @Test
    @Description("register with email input field left empty")
    public void emptyFields5(){
        textfieldFirstName.sendKeys("Stan");
        textfieldLastName.sendKeys("Student");
        textfieldUsername.sendKeys("TestUserStudent");
        textfieldMatrikelnumber.sendKeys("123456");
        textfieldPassword.sendKeys("SicheresPasswort123!");
        textfieldPasswordRepeat.sendKeys("SicheresPasswort123!");
        buttonRegister.click();
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
        assertEquals("http://localhost:" + port + "/register-student", webDriver.getCurrentUrl());
        UserDTO createdUser = userRepository.findByUsername("TestUserStudent");
        assertNull(createdUser);
        WebElement buttonGoBack = webDriver.findElement(By.xpath("/html/body/vaadin-dialog-overlay/flow-component-renderer/div/vaadin-vertical-layout/vaadin-button"))
                .getShadowRoot().findElement(By.cssSelector("#button"));
        buttonGoBack.click();
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
    }
    @Test
    @Description("register with password input field left empty")
    public void emptyFields6(){
        textfieldFirstName.sendKeys("Stan");
        textfieldLastName.sendKeys("Student");
        textfieldUsername.sendKeys("TestUserStudent");
        textfieldMatrikelnumber.sendKeys("123456");
        textfieldEmail.sendKeys("testUser2@JUnitTest.de");
        textfieldPasswordRepeat.sendKeys("SicheresPasswort123!");
        buttonRegister.click();
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
        assertEquals("http://localhost:" + port + "/register-student", webDriver.getCurrentUrl());
        UserDTO createdUser = userRepository.findByUsername("TestUserStudent");
        assertNull(createdUser);
        WebElement buttonGoBack = webDriver.findElement(By.xpath("/html/body/vaadin-dialog-overlay/flow-component-renderer/div/vaadin-vertical-layout/vaadin-button"))
                .getShadowRoot().findElement(By.cssSelector("#button"));
        buttonGoBack.click();
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
    }
    @Test
    @Description("register with repeated password input field left empty")
    public void emptyFields7(){
        textfieldFirstName.sendKeys("Stan");
        textfieldLastName.sendKeys("Student");
        textfieldUsername.sendKeys("TestUserStudent");
        textfieldMatrikelnumber.sendKeys("123456");
        textfieldEmail.sendKeys("testUser2@JUnitTest.de");
        textfieldPassword.sendKeys("SicheresPasswort123!");
        buttonRegister.click();
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
        assertEquals("http://localhost:" + port + "/register-student", webDriver.getCurrentUrl());
        UserDTO createdUser = userRepository.findByUsername("TestUserStudent");
        assertNull(createdUser);
        WebElement buttonGoBack = webDriver.findElement(By.xpath("/html/body/vaadin-dialog-overlay/flow-component-renderer/div/vaadin-vertical-layout/vaadin-button"))
                .getShadowRoot().findElement(By.cssSelector("#button"));
        buttonGoBack.click();
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
    }
    @Test
    @Description("test already used parameters for registration")
    public void alreadyUsed(){
        textfieldFirstName.sendKeys("Stan");
        textfieldLastName.sendKeys("Student");
        textfieldUsername.sendKeys("TestUserStudent");
        textfieldMatrikelnumber.sendKeys("123456");
        textfieldEmail.sendKeys("testUser2@JUnitTest.de");
        textfieldPassword.sendKeys("SicheresPasswort123!");
        textfieldPasswordRepeat.sendKeys("SicheresPasswort123!");
        buttonRegister.click();
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
        UserDTO createdUser = userRepository.findByUsername("TestUserStudent");
        assertNotNull(createdUser);
        assertEquals("TestUserStudent",createdUser.getUsername());
        assertEquals("testUser2@JUnitTest.de",createdUser.getEmail());
        StudentDTO createdStudent = studentRepository.findByUserid(createdUser.getUserid());
        assertNotNull(createdStudent);
        assertEquals("Stan",createdStudent.getFirstname());
        assertEquals("Student",createdStudent.getLastname());
        assertEquals("123456",createdStudent.getMatrikelnumber());
        assertEquals("http://localhost:" + port + "/", webDriver.getCurrentUrl());
        WebElement buttonRegisterAsStudent = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-button[1]"));
        buttonRegisterAsStudent.click();
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
        assertEquals("http://localhost:" + port + "/register-student", webDriver.getCurrentUrl());
        buttonRegister = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-button[1]"));
        textfieldFirstName = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-horizontal-layout[2]/vaadin-form-layout/vaadin-text-field[1]"))
                .getShadowRoot().findElement(By.cssSelector("#vaadin-text-field-input-9 > slot:nth-child(2) > input"));
        textfieldLastName = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-horizontal-layout[2]/vaadin-form-layout/vaadin-text-field[2]"))
                .getShadowRoot().findElement(By.cssSelector("#vaadin-text-field-input-10 > slot:nth-child(2) > input"));
        textfieldUsername = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-horizontal-layout[2]/vaadin-form-layout/vaadin-text-field[3]"))
                .getShadowRoot().findElement(By.cssSelector("#vaadin-text-field-input-11 > slot:nth-child(2) > input"));
        textfieldMatrikelnumber = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-horizontal-layout[2]/vaadin-form-layout/vaadin-text-field[4]"))
                .getShadowRoot().findElement(By.cssSelector("#vaadin-text-field-input-12 > slot:nth-child(2) > input"));
        textfieldEmail = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-horizontal-layout[2]/vaadin-form-layout/vaadin-email-field"))
                .getShadowRoot().findElement(By.cssSelector("#vaadin-email-field-input-13 > slot:nth-child(2) > input[type=email]"));
        textfieldPassword = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-horizontal-layout[2]/vaadin-form-layout/vaadin-password-field[1]"))
                .getShadowRoot().findElement(By.cssSelector("#vaadin-password-field-input-14 > slot:nth-child(2) > input[type=password]"));
        textfieldPasswordRepeat = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-horizontal-layout[2]/vaadin-form-layout/vaadin-password-field[2]"))
                .getShadowRoot().findElement(By.cssSelector("#vaadin-password-field-input-15 > slot:nth-child(2) > input[type=password]"));
        textfieldFirstName.sendKeys("Stan");
        textfieldLastName.sendKeys("Student");
        textfieldUsername.sendKeys("TestUserStudent");
        textfieldMatrikelnumber.sendKeys("123456");
        textfieldEmail.sendKeys("testUser2@JUnitTest.de");
        textfieldPassword.sendKeys("SicheresPasswort123!");
        textfieldPasswordRepeat.sendKeys("SicheresPasswort123!");
        buttonRegister.click();
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
        assertEquals("http://localhost:" + port + "/register-student", webDriver.getCurrentUrl());
    }
    @Test
    @Description("typing Wrong input in fields")
    public void wrongInput(){
        textfieldFirstName.sendKeys("1 ist ein möglicher Vorname");
        textfieldLastName.sendKeys("! ist ein möglicher Nachname");
        textfieldUsername.sendKeys("TestUserStudent");
        textfieldMatrikelnumber.sendKeys("DasIstKeineNummer");
        textfieldEmail.sendKeys("MeineMailAdresse");
        textfieldPassword.sendKeys("Sicher");
        textfieldPasswordRepeat.sendKeys("NichtSicher");
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
        buttonRegister.click();
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
        assertEquals("http://localhost:" + port + "/register-student", webDriver.getCurrentUrl());
    }
}