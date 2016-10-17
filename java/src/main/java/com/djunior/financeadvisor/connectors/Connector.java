/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.djunior.financeadvisor.connectors;

import com.djunior.financeadvisor.dtos.MailDTO;
import java.util.ArrayList;

/**
 *
 * @author davidbrittojr
 */
public interface Connector {
    public ArrayList<MailDTO> getMail(int numberOfTransactions);
}
