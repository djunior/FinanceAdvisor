/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.djunior.financeadvisor.parsers;

import com.djunior.financeadvisor.dtos.TransactionDTO;
import com.djunior.financeadvisor.exceptions.InvalidContentException;

/**
 *
 * @author davidbrittojr
 */
public interface Parser {
    
    public TransactionDTO parse(String c) throws InvalidContentException;
    
}
