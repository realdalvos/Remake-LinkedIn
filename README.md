# Coll@HBRS by Team mid9++
## The vision
Finally, there should be a software that enables everyone to find contacts from companies for future permanent positions and internships. Students from the 3rd semester get the opportunity to get jobs this way.

## Features
- Web application
- Modern Framework
- Simple registration process
- Clean design
- Individual user profiles
- Create job ads and delete them
- Search for students and job ads
- Rating system
- Report function
- Integrated Messanger
- High security standards
- Option to delete the account

## TBD
- Usability Improvements
- Multilanguage Support
- ...

## Requirements
A modern Browser in a recent Version. 
Tested with Chromium based browsers and Firefox.

## Running the Application
The project is a standard Maven project. To run it from the command line, type `mvn` and open http://localhost:8080 in your browser. The application can also be deployed on a compatible web server like Tomcat8.

You can also import the project to your IDE of choice as you would with any Maven project. Read more on [how to set up a development environment for Vaadin projects](https://vaadin.com/docs/latest/guide/install) (Windows, Linux, macOS).

## Project structure
The project follow Maven's [standard directory layout structure](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html):
- Under `srs/main/java` are the Application sources located
    - `Application.java` is a runnable Java application class and the app's starting point
- Under `srs/test/java` are the JUnit and Selenium test files located
- `src/main/resources` contains configuration files and static resources like the language files
- The `frontend` directory in the root folder contains client-side 
  dependencies and resource files.
  - The custom Theme is located under `frontend/themes/mytheme`
- `db` holds the database sql script
- In `design` is the class diagram located

## Useful links
- Deployed application: [Tomcat](http://sepp-test.inf.h-brs.de:8080/mid9/)
- Continuous Integration: [Jenkins](https://sepp-jenkins.inf.h-brs.de/view/SE2%20WiSE%202022/job/mid-9/)
- Static analysis: [SonarQube](https://sepp-sonar.inf.h-brs.de/dashboard?id=mid-9)


- Vaadin documentation: [vaadin.com/docs](https://vaadin.com/docs)
- Spring boot documentation: [docs.spring.io](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)