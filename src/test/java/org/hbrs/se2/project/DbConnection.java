package org.hbrs.se2.project;

import org.hbrs.se2.project.control.LoginControl;
import org.junit.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DbConnection {

    @Autowired
    private LoginControl loginControl;

    // Tests UserDAO Class which makes a query with jdbc
    // But we want to use JPA
    // ToDo Tests for JPA

    /*@Test
    public void connectionToDbTabUserSelectAllWhereIDAndPasswdIsTestWorks() throws SQLException {
        UserDAO testDAO = new UserDAO();
        ResultSet testset = null;
        try {
            testDAO.connection(2, "bobobob");
            testset = testDAO.getSet();
        } catch (Exception e) {
            System.out.println(e.getMessage()+" | "+e.getCause()+" | "+e);
        }
        // print result set
        if(testset.next()) {
            System.out.println(
                    "userid: " + testset.getInt(1) + " " +
                            "username: " + testset.getString(2) + " " +
                            "password: " + testset.getString(3) + " " +
                            "email: " + testset.getString(4));
        } else {
            System.out.println("User with given ID and Passsword not found.");
            Assert.assertNotNull(null);
        }
    }

     */
}
