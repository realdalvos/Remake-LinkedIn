package org.hbrs.se2.project.selenium;

import jdk.jfr.Description;
import org.hbrs.se2.project.Application;
import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.repository.MajorRepository;
import org.hbrs.se2.project.repository.SkillRepository;
import org.hbrs.se2.project.repository.TopicRepository;
import org.hbrs.se2.project.repository.UserRepository;
import org.hbrs.se2.project.util.HelperForTests;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class LoginViewTest {
    @Autowired
    UserRepository userRepository;
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
    WebElement textfieldUsername;
    WebElement textfieldPassword;
    WebElement buttonLogin;
    WebElement buttonRegisterAsStudent;
    WebElement buttonRegisterAsCompany;

    @Value("${server.port: 8080}")
    private int port;

    @BeforeAll
    static void start() throws Exception {

        //Starts the Webserver (Needs a running server for the tests but roughly 35 Seconds to build)
        try {
            Application.main(new String[]{});
        }catch(Exception x){}
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
        companyDTO = h.registerTestCompany();
        userDTO = h.getUserDTOForCompany();
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
        HelperForTests.synchronizedwait(webDriver,1000);
        textfieldUsername = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-login-form/vaadin-login-form-wrapper/form/vaadin-text-field/input"));
        textfieldPassword = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-login-form/vaadin-login-form-wrapper/form/vaadin-password-field/input"));
        buttonLogin = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-login-form/vaadin-login-form-wrapper/form/vaadin-button"));
        buttonRegisterAsStudent = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-button[1]"));
        buttonRegisterAsCompany = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-button[2]"));
    }
    @AfterEach
    void tearDown(){
        h.deleteTestUsers();
        webDriver.quit();
    }

    @Test
    @Description("Tests Login with wrong password")
    public void wronglogin(){
        textfieldUsername.sendKeys(userDTO.getUsername());
        textfieldPassword.sendKeys(userDTO.getUsername()+"X");
        buttonLogin.click();
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
        assertEquals("Login", webDriver.getTitle());
        assertEquals("http://localhost:" + port + "/", webDriver.getCurrentUrl());
    }
    @Test
    @Description("Tests Login as Testuser")
    public void login() throws InterruptedException {
        textfieldUsername.sendKeys(userDTO.getUsername());
        textfieldPassword.sendKeys(userDTO.getPassword());
        buttonLogin.click();
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
        assertEquals("Meine Stellen",webDriver.getTitle());
        assertEquals("http://localhost:" + port + "/myads", webDriver.getCurrentUrl());
    }
    @Test
    @Description("Tests website navigation with registerAsStudent button")
    public void testButtonRegisterAsStudent(){
        buttonRegisterAsStudent.click();
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
        assertEquals("Als Student registrieren", webDriver.getTitle());
        assertEquals("http://localhost:" + port + "/register-student", webDriver.getCurrentUrl());
    }
    @Test
    @Description("Tests website naviagtion with registerAsCompany button")
    public void testButtonRegisterAsCompany(){
        buttonRegisterAsCompany.click();
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
        assertEquals("Als Unternehmen registrieren", webDriver.getTitle());
        assertEquals("http://localhost:" + port + "/register-company", webDriver.getCurrentUrl());
    }
}
