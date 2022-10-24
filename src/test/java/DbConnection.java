import org.junit.Assert;

import org.junit.Test;
import org.vaadin.example.dao.UserDAO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DbConnection {
    @Test
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
}
