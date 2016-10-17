/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.djunior.financeadvisor.dtos;

import java.util.ArrayList;

/**
 *
 * @author davidbrittojr
 */
public class VendorDTO {
    
    private int id;
    private String name;
    private ArrayList<String> tags;
    private String tag;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    
    public void setName(String n) {
        name = n;
    }
    
    public String getName() {
        return name;
    }
    
    public void addTag(String t) {
        tags.add(t);
    }
    
    public ArrayList<String> getTags() {
        return tags;
    }
    
    public void setTag(String t) {
        tag = t;
    }
    
    public String getTag() {
        return tag;
    }
    
}
