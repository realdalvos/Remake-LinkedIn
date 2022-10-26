package org.hbrs.se2.project.util;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class Globals {
    public static String CURRENT_USER = "current_User";

    public static class Pages {
        public static final String MAIN_VIEW = "main";
        public static final String LOGIN_VIEW = "login";
        public static final String JOBS_VIEW = "jobs";
        public static final String MYADS_VIEW = "myads";
        public static final String REGISTER_COMPANY_VIEW = "register-company";
        public static final String REGISTER_STUDENT_VIEW = "register-student";
    }

    public static class Roles {
        public static final String student = "student";
        public static final String company = "company";

    }

    public static class Errors {
        public static final String NOUSERFOUND = "nouser";
        public static final String SQLERROR = "sql";
        public static final String DATABASE = "database";
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
