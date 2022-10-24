package org.vaadin.example.services.db;

import org.vaadin.example.services.db.exception.DatabaseLayerException;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author sascha
 * @editor Julian
 */
public class JDBCConnection {
    private static JDBCConnection connection = null;
    private String url = "jdbc:postgresql://dumbo.inf.h-brs.de/gwolni2s";
    private Connection conn;
    private String login = "gwolni2s";
    private String password = "gwolni2s";

    public static JDBCConnection getInstance() throws DatabaseLayerException {
        if ( connection == null ) {
            connection = new JDBCConnection();
        }
        return connection;
    }

    private JDBCConnection() throws DatabaseLayerException {
        this.initConnection();
    }

    public void initConnection() throws DatabaseLayerException {
        try {
            DriverManager.registerDriver( new org.postgresql.Driver() ); 
        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.openConnection();
    }

    public void openConnection() throws DatabaseLayerException {
        try {
            Properties props = new Properties();
            props.setProperty("user", "gwolni2s" );
            props.setProperty("password", "gwolni2s" );

            this.conn = DriverManager.getConnection(this.url, props);

        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            throw new DatabaseLayerException( "Fehler bei Zugriff auf die DB! Sichere Verbindung vorhanden!?" );
        }
    }

    public Statement getStatement() throws DatabaseLayerException {
        try {
            if ( this.conn.isClosed() ) {
                this.openConnection();
            }
            return this.conn.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
            return null;
        }
    }

    public PreparedStatement getPreparedStatement( String sql  ) throws DatabaseLayerException {
        try {
            if ( this.conn.isClosed() ) {
                this.openConnection();
            }
            return this.conn.prepareStatement(sql);
        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void closeConnection(){
        try {
            this.conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

