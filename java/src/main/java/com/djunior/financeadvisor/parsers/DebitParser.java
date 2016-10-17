/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.djunior.financeadvisor.parsers;

import com.djunior.financeadvisor.connectors.DBConnector;
import com.djunior.financeadvisor.dtos.TransactionDTO;
import com.djunior.financeadvisor.dtos.VendorDTO;
import com.djunior.financeadvisor.exceptions.InvalidContentException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author davidbrittojr
 */
public class DebitParser implements Parser {
    
    public DebitParser() {
        
    }
    
    @Override
    public TransactionDTO parse(String c) throws InvalidContentException{
        TransactionDTO t = new TransactionDTO();
        String clean = c.replaceAll("\n","").replaceAll("\r","");
        String str = clean.replaceAll("<http://[a-zA-Z\\._\\-/0-9]+>","");
        
        Pattern price = Pattern.compile("[0-9]+(,[0-9]+)?");
        Matcher price_m = price.matcher(str);
        if (price_m.find())
        {
            t.setValue(Float.parseFloat(price_m.group(0).replace(",",".")));
        } else {
            System.out.println(str);
            throw new InvalidContentException("Could not parse price");
        }
        
        String dateStr = "";
        Pattern date = Pattern.compile("[0-9]+/[0-9]+/[0-9]+");
        Matcher date_m = date.matcher(str);
        if (date_m.find())
        {
            dateStr = date_m.group(0);
        }
        
        Pattern time = Pattern.compile("[0-9]+:[0-9]+:[0-9]+");
        Matcher time_m = time.matcher(str);
        if (time_m.find())
        {
            
            try {
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss"); 
                Date startDate = (Date)formatter.parse(dateStr + " " + time_m.group(0)); 
                t.setDate(startDate);
            } catch(ParseException e) {

            }
        }

        Pattern location = Pattern.compile("local: [a-zA-Z0-9 ]+[a-zA-Z ]+");
        Matcher location_m = location.matcher(str);
        if (location_m.find())
        {
            String l = location_m.group(0).replace("local: ","");
            
            DBConnector db = new DBConnector();

            db.setDbName("FinanceAdvisorDB");
            db.setServerLocation("localhost:3306");
            db.setUsername("root");
            db.setPassword("");
            
            int vendorId = db.addVendor(l);
            
            t.setLocation(vendorId, l);
            
        } else {
            System.out.println(str);
            throw new InvalidContentException("Could not parse location");
        }
        
        Pattern card = Pattern.compile("\\*\\*\\*\\*[0-9]+");
        Matcher card_m = card.matcher(str);
        if (card_m.find())
        {
            t.setCard(card_m.group(0).replace("\\*\\*\\*\\*",""));
        } else {

        }
        
        return t;
    }
    
}
