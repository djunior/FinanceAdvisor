/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.djunior.financeadvisor.connectors;

import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;

import com.djunior.financeadvisor.dtos.MailDTO;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author davidbrittojr
 */
public class MailConnector implements Connector{
    
    private String username;
    private String password;
    private String server;
    
    public MailConnector() {
        Properties prop = new Properties();
        InputStream input = getClass().getClassLoader().getResourceAsStream("mail.properties");
        if (input != null){
            try {
             prop.load(input);
            } catch (IOException e){
             return;
            }
        } else {
            System.out.println("InputStream is null!");
        }
        
        username = prop.getProperty("USER");
        password = prop.getProperty("PASSWORD");
        server = prop.getProperty("SERVER");
        
        System.out.println("------------ USERNAME: " + username);
        System.out.println("------------ PASSWORD: " + password);
        System.out.println("-------------- SERVER: " + server);
    }
    
    public MailConnector(String s, String u, String p) {
        server = s;
        username = u;
        password = p;
    }
    
    public void setUsername(String u) {
        username = u;
    }
    
    public void setPassword(String p) {
        password = p;
    }
     
    @Override
    public ArrayList<MailDTO> getMail(int numberOfTransactions) {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        props.put("mail.imap.starttls.enable","true");
        props.put("mail.imap.auth", "true");  // If you need to authenticate
        // Use the following if you need SSL
        props.put("mail.imap.socketFactory.port", 993);
        props.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.imap.socketFactory.fallback", "false");
        
        ArrayList<MailDTO> ret = new ArrayList<>();
        
        System.out.println("numberOfTransactions = " + numberOfTransactions);
        
        try {
            Session session = Session.getInstance(props, null);
            Store store = session.getStore();
            store.connect(server, username, password);
            Folder inbox = store.getFolder("Itau");
            inbox.open(Folder.READ_ONLY);
            System.out.println("inbox.getMessageCount()"+ inbox.getMessageCount());
            int diff = inbox.getMessageCount() - numberOfTransactions;
            System.out.println("diff = "+ diff);
            for (Integer i = 0; i < diff; i++) {
                Message msg = inbox.getMessage(i+1);

                Multipart mp = (Multipart) msg.getContent();
                BodyPart bp = mp.getBodyPart(0);
                
                ret.add(new MailDTO(msg.getSubject(), bp.getContent().toString()));
            }
        } catch (Exception mex) {
            mex.printStackTrace();
        }
        
        return ret;
    }
}
