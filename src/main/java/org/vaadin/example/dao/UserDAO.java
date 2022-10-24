package org.vaadin.example.dao;

import org.vaadin.example.util.Globals;
import org.vaadin.example.services.db.exception.DatabaseLayerException;
import org.vaadin.example.services.db.JDBCConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {
    // Set ResultSet to null;
    ResultSet set = null;

    public ResultSet getSet(){
        return set;
    }

    public void connection(int id, String password) throws DatabaseLayerException{
        // database query for getting a user by id and password
        // question marks "?" are placeholders for values
        String query = "SELECT * FROM mid9db.user WHERE userid = ? AND password = ?";
        // Set try-clause
        try {
            PreparedStatement preparedStatement = null;
            try {
                // prepare a statement with a query
                preparedStatement = JDBCConnection.getInstance().getPreparedStatement(query);
            } catch (DatabaseLayerException e) {
                e.printStackTrace();
            }

            // insert query values into prepared statement
            // parameter Index i is question mark ? at position i
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, password);

            // execute query and save resultset
            this.set = preparedStatement.executeQuery();

            JDBCConnection.getInstance().closeConnection();

        } catch (SQLException ex) {
            System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
            DatabaseLayerException e = new DatabaseLayerException("Fehler im SQL-Befehl!");
            e.setReason(Globals.Errors.SQLERROR);
            throw e;
        } catch (NullPointerException ex) {
            System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
            DatabaseLayerException e = new DatabaseLayerException("Fehler bei Datenbankverbindung!");
            e.setReason(Globals.Errors.DATABASE);
            throw e;
        }
    }
}
