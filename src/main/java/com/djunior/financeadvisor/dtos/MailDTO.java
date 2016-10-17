/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.djunior.financeadvisor.dtos;

/**
 *
 * @author davidbrittojr
 */
public class MailDTO {
    
    private String subject;
    private String content;
    
    public MailDTO(String s, String c) {
        subject = s;
        content = c;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public String getContent() {
        return content;
    }
}
