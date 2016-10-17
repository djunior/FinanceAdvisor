/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.djunior.financeadvisor;

import com.djunior.financeadvisor.connectors.Connector;
import com.djunior.financeadvisor.connectors.DBConnector;
import com.djunior.financeadvisor.connectors.LocalConnector;
import com.djunior.financeadvisor.connectors.MailConnector;
import com.djunior.financeadvisor.dtos.MailDTO;
import com.djunior.financeadvisor.dtos.TransactionDTO;
import com.djunior.financeadvisor.dtos.VendorDTO;
import com.djunior.financeadvisor.exceptions.InvalidContentException;
import com.djunior.financeadvisor.parsers.DebitParser;
import com.djunior.financeadvisor.parsers.Parser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author davidbrittojr
 */
public class Main {
    
    public static DBConnector createDBConnector() {
        DBConnector db = new DBConnector();

//        db.setDbName("FinanceAdvisorDB");
//        db.setServerLocation("localhost:3306");
//        db.setUsername("root");
//        db.setPassword("");
        
        return db;
    }
    
    public static String askForVendorTag(VendorDTO v) throws IOException{
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        
        System.out.println("Digite a tag para o vendor: " + v.getName());
        return reader.readLine();   
    }
    
    public static void addTagToVendors() {
        DBConnector db = createDBConnector();
        ArrayList<VendorDTO> vendorList = db.getAllVendors();
        
        for (VendorDTO v : vendorList) {
            
            if (v.getTag() != null && ! v.getTag().equals(""))
                continue;
            
            try {
                String t = askForVendorTag(v);
                if (! t.equals("")) {
                    System.out.println("Adicionando tag " + t);
                    db.addTagToVendor(v.getId(),t);
                }
            } catch(IOException e) {
                e.printStackTrace();
            }


        }
    }
    
    public static void discriminateExpanses(ArrayList<TransactionDTO> transactions) {
        
        HashMap<String,Float> spentPerLocation = new HashMap<>();
        HashMap<String,Float> spentPerDay = new HashMap<>();
        HashMap<String,Float> spentPerTag = new HashMap<>();
        Float total = 0f;
        
        for (TransactionDTO t : transactions) {
            if (spentPerLocation.containsKey(t.getLocation())) {
                Float currentPerLocation = spentPerLocation.get(t.getLocation());
                spentPerLocation.put(t.getLocation(),currentPerLocation + t.getValue());
            } else {
                spentPerLocation.put(t.getLocation(), t.getValue());
            }
            
            SimpleDateFormat dfDay = new SimpleDateFormat("YYYY/MM/dd");
            String day = dfDay.format(t.getDate());
            
            if (spentPerDay.containsKey(day)) {
                Float current = spentPerDay.get(day);
                spentPerDay.put(day, current + t.getValue());
            } else {
                spentPerDay.put(day, t.getValue());
            }
            
            String tag = t.getVendor().getTag();
            if (tag == null || tag.equals(""))
                tag = "unknown";
            if (spentPerTag.containsKey(tag)) {
                Float current = spentPerTag.get(tag);
                spentPerTag.put(tag,current + t.getValue());
            } else {
                spentPerTag.put(tag, t.getValue());
            }
            
            total += t.getValue();
        }
        
//        System.out.println("Gastos por localidade:");
//        for (Map.Entry<String, Float> entry : spentPerLocation.entrySet()) {
//            String key = entry.getKey();
//            Object value = entry.getValue();
//            System.out.println(key + ": " + value);
//        }
//
//        System.out.println("Gastos por dia:");
//        for (Map.Entry<String, Float> entry : spentPerDay.entrySet()) {
//            String key = entry.getKey();
//            Object value = entry.getValue();
//            System.out.println(key + ": " + value);
//        }
        
        System.out.println("Gastos por tag:");
        for (Map.Entry<String, Float> entry : spentPerTag.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(key + ": " + value);
        }
        
        System.out.println("Total: " + total);
    }
    
    public static void discriminateTransactionsPerMonth(ArrayList<TransactionDTO> transactions) {
        
        HashMap< String, ArrayList<TransactionDTO> > transactionsPerMonth = new HashMap<>(); 
        
        for (TransactionDTO t : transactions) {
            
            SimpleDateFormat dfDay = new SimpleDateFormat("YYYY/MM");
            String month = dfDay.format(t.getDate());
            
            ArrayList<TransactionDTO> tList = transactionsPerMonth.get(month);
            if (tList == null)
                tList = new ArrayList<>();
            
            tList.add(t);
            
            transactionsPerMonth.put(month,tList);
        }
        
        for (Map.Entry<String, ArrayList<TransactionDTO> > entry : transactionsPerMonth.entrySet()) {
            System.out.println("\n\n\n\nMes " + entry.getKey());
            discriminateExpanses(entry.getValue());
        }
        
    }
    
    public static void retrieveEmailTransactions() {
                
        DBConnector db = createDBConnector();
        Connector c = new MailConnector();
        
        int n = 0;
        try {
            n = db.getNumberOfTransactions();
        }catch(SQLException e) {
            e.printStackTrace();
        }
        
        ArrayList<MailDTO> mails = c.getMail(n);
        
        int mailCount = 0;
        int transactionCount = 0;
        int failCount = 0;
        
        for (int i = 0; i < mails.size(); i++) {
            MailDTO mail = mails.get(i);

            Parser p = new DebitParser();
            
            mailCount++;            
            
            try {
                TransactionDTO t = p.parse(mail.getContent());
                
                if (t.getLocation() != null) {

                    try {

                        db.addTransaction(t);
                        transactionCount++;
                        
                    } catch(SQLException e) {
                        e.printStackTrace();
                        failCount++;
                    }
                } else {
                    failCount++;
                }
            } catch(InvalidContentException e) {
                e.printStackTrace();
                failCount++;
            }
        }
        
        System.out.println("Mail count: " + mailCount);
        System.out.println("Transaction count: " + transactionCount);
        System.out.println("Fail count: " + failCount);
    }
    
    public static void main(String[] args) {
        
        System.out.println("Main executed!");        
        
        DBConnector db = createDBConnector();
//
        retrieveEmailTransactions();
        ArrayList<TransactionDTO> transactions = db.getAllTransactions();
        discriminateTransactionsPerMonth(transactions);
        
    }
}
