/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.djunior.financeadvisor.connectors;

import com.djunior.financeadvisor.dtos.TransactionDTO;
import com.djunior.financeadvisor.dtos.VendorDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author davidbrittojr
 */
public class DBConnector extends DBBase {
    
    public DBConnector() {
        super();
    }
    
    public int addTag(String name) {
        
        System.out.println("DBConnector.addTag(" + name + ")");
        Connection connection = null;
        PreparedStatement pstmtInsert = null;
        PreparedStatement pstmtCheck = null;
        ResultSet rs = null;
        int ret = 0;
        
        String statementInsert = "INSERT IGNORE INTO tag (tag_name) VALUES (?);";
        String statementCheck =  "SELECT idtag FROM tag WHERE tag_name=?;";
        
        try {
            connection = getConnection();
            
            pstmtInsert = connection.prepareStatement(statementInsert);
            pstmtInsert.setString(1, name);
            pstmtInsert.execute();
            
            pstmtCheck = connection.prepareStatement(statementCheck);
            pstmtCheck.setString(1,name);
            rs = pstmtCheck.executeQuery();
            
            while(rs.next()) {

                ret = rs.getInt("idtag");
                
                System.out.println("-------------------------- Got idtag! " + ret);
            }
        } 
        catch(SQLException e) { e.printStackTrace(); }
        finally {
            try {
                if (rs != null) rs.close();
                if (pstmtInsert !=null) pstmtInsert.close();
                if (pstmtCheck != null) pstmtCheck.close();
                if (connection != null) connection.close();
            } catch(SQLException e) {}
        }
        return ret;
    }
    
    public void addTagToVendor(int idvendor, int idtag) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        
        try {
            connection = getConnection();
            String statement = "UPDATE vendor SET idtag=? WHERE idvendor=?;";
            pstmt = connection.prepareStatement(statement);
            pstmt.setInt(1, idtag);
            pstmt.setInt(2, idvendor);
            pstmt.execute();
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (connection  != null) connection.close();
            } catch(Exception e) { e.printStackTrace(); }
        }
    }

    public int addVendor(String name) {
        
        System.out.println("DBConnector.addVendor(" + name + ")");
        Connection connection = null;
        PreparedStatement pstmtInsert = null;
        PreparedStatement pstmtCheck = null;
        ResultSet rs = null;
        int ret = 0;
        
        String statementInsert = "INSERT IGNORE INTO vendor (vendor_name) VALUES (?);";
        String statementCheck =  "SELECT idvendor FROM vendor WHERE vendor_name=?;";
        
        try {
            connection = getConnection();
            
            pstmtInsert = connection.prepareStatement(statementInsert);
            pstmtInsert.setString(1, name);
            pstmtInsert.execute();
            
            pstmtCheck = connection.prepareStatement(statementCheck);
            pstmtCheck.setString(1,name);
            rs = pstmtCheck.executeQuery();
            
            while(rs.next()) {

                ret = rs.getInt("idvendor");
                
                System.out.println("-------------------------- Got idvendor! " + ret);
            }
        } 
        catch(SQLException e) { e.printStackTrace(); }
        finally {
            try {
                if (rs != null) rs.close();
                if (pstmtInsert !=null) pstmtInsert.close();
                if (pstmtCheck != null) pstmtCheck.close();
                if (connection != null) connection.close();
            } catch(SQLException e) {}
        }
        return ret;
    }
    
    public void addTransaction(TransactionDTO t) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;
        
        try {
            connection = getConnection(); 
            System.out.println("Status: " + getStatusMessage());
            String statement = "INSERT INTO transaction(value,idvendor,card,date) VALUES(?,?,?,?);";
           
            pstmt = connection.prepareStatement(statement);
            pstmt.setFloat(1, t.getValue());
            pstmt.setInt(2, t.getVendor().getId());
            pstmt.setString(3, t.getCard());
            pstmt.setDate(4,new java.sql.Date(t.getDate().getTime()));

            pstmt.execute();
        } finally {
           try { if(pstmt != null) pstmt.close(); } catch(Exception e) {e.printStackTrace(); }
           try { if(connection != null) connection.close(); } catch(Exception e) {e.printStackTrace(); }
        }
        
    }
    
    public ArrayList<TransactionDTO> getAllTransactions() {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<TransactionDTO> result = new ArrayList<>();
        
        try {
           connection = getConnection(); 
           System.out.println("Status: " + getStatusMessage());
           String statement =   "SELECT idtransaction,value,date,card,transaction.idvendor,vendor_name,tag_name FROM transaction "
                              + "INNER JOIN vendor ON transaction.idvendor=vendor.idvendor "
                              + "LEFT JOIN tag ON vendor.idtag=tag.idtag;";
           
           pstmt = connection.prepareStatement(statement);
           rs = pstmt.executeQuery();
           
           while(rs.next()) {
               TransactionDTO t = new TransactionDTO();
               
               t.setValue(rs.getFloat("value"));
               t.setCard(rs.getString("card"));
               t.setLocation(rs.getInt("idvendor"), rs.getString("vendor_name"), rs.getString("tag_name"));
               t.setDate(rs.getDate("date"));
               
               result.add(t);
           }
        } catch(SQLException e) {
        } finally {
            try { if(rs != null) rs.close(); } catch(Exception e) {e.printStackTrace(); }
            try { if(pstmt != null) pstmt.close(); } catch(Exception e) {e.printStackTrace(); }
            try { if(connection != null) connection.close(); } catch(Exception e) {e.printStackTrace(); }
        }
        return result;
    }
    
    public ArrayList<VendorDTO> getAllVendors() {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<VendorDTO> result = new ArrayList<>();
        
        try {
            connection = getConnection();
            String statement = "SELECT idvendor,vendor_name,tag_name FROM vendor "
                              + "LEFT JOIN tag ON vendor.idtag=tag.idtag;";
            pstmt = connection.prepareStatement(statement);
            rs = pstmt.executeQuery();
            while(rs.next()) {
                VendorDTO v = new VendorDTO();
                v.setId(rs.getInt("idvendor"));
                v.setName(rs.getString("vendor_name"));
                v.setTag(rs.getString("tag_name"));
                result.add(v);
            }
        } catch(SQLException e) { e.printStackTrace(); }
        finally {
            try { if (rs != null) rs.close(); } catch(Exception e) {}
            try {if (pstmt != null) pstmt.close(); } catch(Exception e) {}
            try {if (connection != null) connection.close();} catch(Exception e) {}
        }
        return result;
    }
    
    public void addTagToVendor() { }
 
    public Integer getNumberOfTransactions() throws SQLException{
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Integer result = 0;
        
        try {
           connection = getConnection(); 
           System.out.println("Status: " + getStatusMessage());
           String statement = "SELECT COUNT(*) FROM transaction;";
           
           pstmt = connection.prepareStatement(statement);
           rs = pstmt.executeQuery();

           if (rs.next()) {
               result = rs.getInt("COUNT(*)");
           }
           
        } finally {
            try { if(rs != null) rs.close(); } catch(Exception e) {e.printStackTrace(); }
            try { if(pstmt != null) pstmt.close(); } catch(Exception e) {e.printStackTrace(); }
            try { if(connection != null) connection.close(); } catch(Exception e) {e.printStackTrace(); }
        }
        
        return result;
    }
    
    public void populateVendorTable() throws SQLException {
        Connection connection = null;
        
        PreparedStatement pstmt_transaction = null;
        PreparedStatement pstmt_vendor = null;
        
        ResultSet rs_transaction = null;
        ResultSet rs_vendor = null;
        
        try {
            connection = getConnection();
            
            String statement = "SELECT * FROM transaction;";
            pstmt_transaction = connection.prepareStatement(statement);

            String statement2 = "SELECT * FROM vendor;";
            pstmt_vendor = connection.prepareStatement(statement2);
            
            rs_transaction = pstmt_transaction.executeQuery();
            rs_vendor = pstmt_vendor.executeQuery();
            
            rs_transaction.first();
            do {
                rs_vendor.first();
                do {
                    System.out.println("Setting transaction (" + rs_transaction.getInt("idtransaction") + ", " + rs_transaction.getString("idvendor") + ") to vendor (" + rs_vendor.getInt("idvendor") + ", " + rs_vendor.getString("vendor_name") + ")" );
                    if (rs_transaction.getString("idvendor").equals(rs_vendor.getString("name"))) {
                        String insertStatement = "UPDATE transaction SET vendor2=? where transactionid=?;";
                        
                        try (PreparedStatement pstmt_insert = connection.prepareStatement(insertStatement)) {
                            pstmt_insert.setInt(1, rs_vendor.getInt("idvendor"));
                            pstmt_insert.setInt(2, rs_transaction.getInt("transactionid"));
                            
                            pstmt_insert.execute();
                            
                            pstmt_insert.close();
                        }
                        
                    }
                } while(rs_vendor.next());
            } while(rs_transaction.next());
        } finally {
            try { if(rs_transaction != null) rs_transaction.close(); } catch(Exception e) {e.printStackTrace(); }
            try { if(rs_vendor != null) rs_vendor.close(); } catch(Exception e) {e.printStackTrace(); }
            try { if(pstmt_transaction != null) pstmt_transaction.close(); } catch(Exception e) {e.printStackTrace(); }
            try { if(pstmt_vendor != null) pstmt_vendor.close(); } catch(Exception e) {e.printStackTrace(); }
            try { if(connection != null) connection.close(); } catch(Exception e) {e.printStackTrace(); }
        }
    }
    
}
