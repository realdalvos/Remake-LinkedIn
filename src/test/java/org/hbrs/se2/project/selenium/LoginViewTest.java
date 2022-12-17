package org.hbrs.se2.project.selenium;

import jdk.jfr.Description;
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
import org.springframework.beans.factory.annotation.Autowired;
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

    @BeforeAll
    static void start(){
            /*if (System.getenv().get("OS") != null && System.getenv().get("OS").matches("^.*[wW]indows.*$")) {
                System.setProperty("webdriver.chrome.driver", "src/test/java/org/hbrs/se2/project/selenium/chromedriver.exe");
            } else {
                System.out.println("\n\nYou might use linux or mac. If linux then everything is fine. If mac then select the right driver from this current folder in the line below\n\n");
                String driver = "chromedriver_linux64";
                System.setProperty("webdriver.chrome.driver", "src/test/java/org/hbrs/se2/project/selenium/" + driver);
            }*/
        String os = System.getProperty("os.name").toUpperCase();
            if(os.contains("WIN")){
            System.setProperty("webdriver.chrome.driver", "src/test/java/org/hbrs/se2/project/selenium/chromedriver.exe");
            } else if (os.contains("MAC")) {
                if(os.contains("ARM")) {
                    System.setProperty("webdriver.chrome.driver", "src/test/java/org/hbrs/se2/project/selenium/chromedriver_arm64");
                }else {
                    System.setProperty("webdriver.chrome.driver", "src/test/java/org/hbrs/se2/project/selenium/chromedriver_mac64");
                }
            }else {
                System.setProperty("webdriver.chrome.driver", "src/test/java/org/hbrs/se2/project/selenium/chromedriver_linux64");
            }
    }

    @BeforeEach
    void setUp(){
        companyDTO = h.registerTestCompany();
        userDTO = h.getUserDTOForCompany();
        webDriver = new ChromeDriver();
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        webDriver.manage().window().maximize();
        webDriver.get("http://localhost:8080");
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
        assertEquals("http://localhost:8080/", webDriver.getCurrentUrl());
    }
    @Test
    @Description("Tests Login as Testuser")
    public void login() throws InterruptedException {
        textfieldUsername.sendKeys(userDTO.getUsername());
        textfieldPassword.sendKeys(userDTO.getPassword());
        buttonLogin.click();
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
        assertEquals("Meine Stellen",webDriver.getTitle());
        assertEquals("http://localhost:8080/myads", webDriver.getCurrentUrl());
    }
    @Test
    @Description("Tests website navigation with registerAsStudent button")
    public void testButtonRegisterAsStudent(){
        buttonRegisterAsStudent.click();
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
        assertEquals("Als Student registrieren", webDriver.getTitle());
        assertEquals("http://localhost:8080/register-student", webDriver.getCurrentUrl());
    }
    @Test
    @Description("Tests website naviagtion with registerAsCompany button")
    public void testButtonRegisterAsCompany(){
        buttonRegisterAsCompany.click();
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
        assertEquals("Als Unternehmen registrieren", webDriver.getTitle());
        assertEquals("http://localhost:8080/register-company", webDriver.getCurrentUrl());
    }
}
