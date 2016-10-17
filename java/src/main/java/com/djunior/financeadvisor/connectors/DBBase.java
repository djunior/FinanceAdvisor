package com.djunior.financeadvisor.connectors;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author djunior
 */
public class DBBase {
    public String status;
    private String dbName;
    private String serverLocation;
    private String username;
    private String password;
    
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public void setServerLocation(String serverLocation) {
        this.serverLocation = serverLocation;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public DBBase() {
        status = "Not connected to DB";
        
        Properties prop = new Properties();
        InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties");
        if (input != null){
            try {
             prop.load(input);
            } catch (IOException e){
             return;
            }
        } else {
            System.out.println("InputStream is null!");
        }
       
       dbName = prop.getProperty("DATABASE");
       serverLocation = prop.getProperty("SERVER") + ":" + prop.getProperty("PORT");
       username = prop.getProperty("USER");
       password = prop.getProperty("PASSWORD");
       
       System.out.println("DB NAME:" + dbName);
       System.out.println("serverLocation: " +serverLocation);
       System.out.println("username: " +username);
       System.out.println("password: " +password);
    }
    
    public Connection getConnection() throws SQLException{
        
        System.out.println("[DBBase] getSQLConnection() started");
        
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + serverLocation + "/" + dbName;
            connection = DriverManager.getConnection(url,username,password);
            status = "Connected to DB!";
            return connection;
        } catch (ClassNotFoundException e) {
            System.out.println("[DBBase] ClassNotFoundException");
            System.out.println(e.getMessage());
            status = "Could not connect to DB, ClassNotFoundException\n" + e.getMessage();
            return connection;
        }
    }
    
    public String getStatusMessage(){
        return status;
    }
    
    public boolean closeConnection(){
//        try {
            status = "Not connected to DB";
            return true;
//        } catch (SQLException e){
//            status = "Could not disconnect to DB\n" + e.getMessage();
//            return false;
//        }
    }
    
    public Connection restartConnection() throws SQLException{
        closeConnection();
        return getConnection();
    }
    
    
}
