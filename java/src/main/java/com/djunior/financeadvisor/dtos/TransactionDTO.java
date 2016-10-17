/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.djunior.financeadvisor.dtos;

import java.util.Date;

/**
 *
 * @author davidbrittojr
 */
public class TransactionDTO {
    
    public static enum TransactionType {
        TRANSACTION_TYPE_DEBIT,
        TRANSACTION_TYPE_CREDIT
    }
    
    private Float value;
    private VendorDTO vendor;
    private Date date;
    private String card;
    private TransactionType type;
    
    public TransactionDTO() {
        this.vendor = new VendorDTO();
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public TransactionType getType() {
        return type;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public String getLocation() {
        return this.vendor.getName();
    }

    public void setLocation(String location) {
        this.vendor.setName(location);
    }
    
    public void setLocation(int id, String location) {
        this.vendor.setId(id);
        this.vendor.setName(location);
    }
    
    public void setLocation(int id, String location, String tag) {
        this.vendor.setId(id);
        this.vendor.setName(location);
        this.vendor.setTag(tag);
    }
    
    public VendorDTO getVendor() {
        return vendor;
    }
    
    public void setVendor(VendorDTO v) {
        this.vendor = v;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date){
        this.date = date;
    }
}
