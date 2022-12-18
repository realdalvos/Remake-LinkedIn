package org.hbrs.se2.project.selenium;

import jdk.jfr.Description;
import org.hbrs.se2.project.Application;
import org.hbrs.se2.project.dtos.CompanyDTO;
import org.hbrs.se2.project.dtos.UserDTO;
import org.hbrs.se2.project.repository.MajorRepository;
import org.hbrs.se2.project.repository.SkillRepository;
import org.hbrs.se2.project.repository.TopicRepository;
import org.hbrs.se2.project.repository.UserRepository;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.util.HelperForTests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

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
        Application.main(new String[]{});

        String os;
        try{
            os = System.getProperty("os.name").toUpperCase();
        }
        catch (NullPointerException x){
            throw new Globals.IllegalOSExcpetion("Your OS is not shown under the java system properties. Try to fix this problem on your own");
        }
            if(os.contains("WIN")){
            System.setProperty("webdriver.chrome.driver", "src/test/java/org/hbrs/se2/project/selenium/chromedriver.exe");
            } else if (os.contains("MAC")) {
                if(os.contains("ARM")) {
                    System.setProperty("webdriver.chrome.driver", "src/test/java/org/hbrs/se2/project/selenium/chromedriver_arm64");
                }else {
                    System.setProperty("webdriver.chrome.driver", "src/test/java/org/hbrs/se2/project/selenium/chromedriver_mac64");
                }
            }else if(os.contains("NIX") || os.contains("NUX") || os.contains("AIX")){
                System.setProperty("webdriver.chrome.driver", "src/test/java/org/hbrs/se2/project/selenium/chromedriver_linux64");
            } else {
                throw new Globals.IllegalOSExcpetion("You are using an uncommon OS: "+ System.getProperty("os.name")+" . please use either windows, mac or linux for this program");
            }
    }

    @BeforeEach
    void setUp(){
        companyDTO = h.registerTestCompany();
        userDTO = h.getUserDTOForCompany();
        webDriver = new ChromeDriver();
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        webDriver.manage().window().maximize();
        webDriver.get("http://localhost:" + port);
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
