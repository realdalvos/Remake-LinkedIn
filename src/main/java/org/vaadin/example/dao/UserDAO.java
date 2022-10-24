package org.vaadin.example.dao;

import org.vaadin.example.util.Globals;
import org.vaadin.example.services.db.exception.DatabaseLayerException;
import org.vaadin.example.services.db.JDBCConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {
    // Set ResultSet to null;
    ResultSet set = null;

    public ResultSet getSet(){
        return set;
    }

    public void connection(String id, String password) throws DatabaseLayerException{
        // Set try-clause
        try {
            Statement statement = null;
            try {
                statement = JDBCConnection.getInstance().getStatement();
            } catch (DatabaseLayerException e) {
                e.printStackTrace();
            }
            set = statement.executeQuery(
                    "SELECT * "
                            + "FROM mid9db.user ");
                         //   + "WHERE mid9db.user.userid = \'" + id + "\'"
                         //   + " AND mid9db.user.password = \'" + password + "\'");

            JDBCConnection.getInstance().closeConnection();

        } catch (
                SQLException ex) {
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