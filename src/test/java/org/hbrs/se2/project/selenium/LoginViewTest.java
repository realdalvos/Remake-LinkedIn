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

    @BeforeAll
    static void start(){

        if(System.getenv().get("OS").matches("Windows")){
            System.setProperty("webdriver.chrome.driver","src/test/java/org/hbrs/se2/project/selenium/chromedriver.exe");
        }else{
            System.out.println("\n\nYou might use linux or mac. If linux then everything is fine. If mac then select rigth driver in line below\n\n");
            String driver= "chromedriver_linux64";
            System.setProperty("webdriver.chrome.driver","src/test/java/org/hbrs/se2/project/selenium/" + driver);
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
    }
    @AfterEach
    void tearDown(){
        h.deleteTestUsers();
        //webDriver.close();
        webDriver.quit();
    }

    @Test
    @Description("Tests Login as Testuser")
    public void login() throws InterruptedException {
        WebElement textfieldUsername = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-login-form/vaadin-login-form-wrapper/form/vaadin-text-field/input"));
        textfieldUsername.sendKeys(userDTO.getUsername());
        WebElement textfieldPassword = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-login-form/vaadin-login-form-wrapper/form/vaadin-password-field/input"));
        textfieldPassword.sendKeys(userDTO.getPassword());
        WebElement buttonLogin = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-login-form/vaadin-login-form-wrapper/form/vaadin-button"));
        buttonLogin.click();
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
        assertEquals("Meine Stellen",webDriver.getTitle());
    }
    @Test
    @Description("Tests Login with wrong password")
    public void wronglogin(){
        WebElement textfieldUsername = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-login-form/vaadin-login-form-wrapper/form/vaadin-text-field/input"));
        textfieldUsername.sendKeys(userDTO.getUsername());
        WebElement textfieldPassword = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-login-form/vaadin-login-form-wrapper/form/vaadin-password-field/input"));
        textfieldPassword.sendKeys(userDTO.getUsername()+"X");
        WebElement buttonLogin = webDriver.findElement(By.xpath("/html/body/vaadin-vertical-layout/vaadin-login-form/vaadin-login-form-wrapper/form/vaadin-button"));
        buttonLogin.click();
        h.synchronizedwait(webDriver,MAXIMUM_PAGE_LOADINGTIME);
        assertEquals("Login", webDriver.getTitle());
    }
}
