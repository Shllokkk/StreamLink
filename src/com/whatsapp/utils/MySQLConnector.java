package com.whatsapp.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class MySQLConnector {
    private static Connection connection = null;
    
    public static Connection getConnection() {
        if(connection == null) {
            String connectionString = "jdbc:mysql://localhost:3306/whatsapp";
            try {
                connection = DriverManager.getConnection(connectionString, "root", "Superfly23#");
            } catch (SQLException ex) {
                String msg = "Could not connect to DB" + ex.getMessage();
                JOptionPane.showMessageDialog(null, msg, "ERROR!", JOptionPane.ERROR_MESSAGE);
            }
        }
        return connection;
    }
}
