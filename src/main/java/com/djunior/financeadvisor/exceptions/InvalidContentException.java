/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.djunior.financeadvisor.exceptions;

/**
 *
 * @author davidbrittojr
 */
public class InvalidContentException extends Exception{
    public InvalidContentException() {
        super();
    }
    
    public InvalidContentException(String reason) {
        super(reason);
    }
}
