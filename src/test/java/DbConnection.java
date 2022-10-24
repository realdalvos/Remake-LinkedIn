import org.junit.Assert;

import org.junit.Test;
import org.vaadin.example.dao.UserDAO;

import java.sql.ResultSet;

public class DbConnection {
    @Test
    public void connectionToDbTabUserSelectAllWhereIDAndPasswdIsTestWorks() {
        UserDAO testDAO = new UserDAO();
        ResultSet testset = null;
        try {
            testDAO.connection("test", "test");
            testset = testDAO.getSet();
        } catch (Exception e) {
            System.out.println(e.getMessage()+" | "+e.getCause()+" | "+e);
        }
        Assert.assertNotNull(testset);
    }
}
