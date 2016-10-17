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
public class LocalConnector implements Connector{
    
    @Override
    public ArrayList<MailDTO> getMail(int numberOfTransactions) {
        ArrayList<MailDTO> ret = new ArrayList<>();
        
        MailDTO m1 = new MailDTO("Compra com cartão débito aprovada",
                                "<http://www.itau.com.br/_arquivosestaticos/Itaumail/servicos/canais/comunicacao-digital/varejo/IMG_01.jpg> 	\n" +
                                "Compra com cartão de débito aprovada\n" +
                                "\n" +
                                "DAVID, \n" +
                                "\n" +
                                "Você realizou uma compra no valor de R$ 266,00, no dia 04/08/2016, às\n" +
                                "09:20:40h, local: LAMINA ICARA0408, com o cartão final: **** **** ****\n" +
                                "7578. A compra foi aprovada e debitada da sua conta.\n" +
                                "\n" +
                                "Pague no débito com seu cartão Itaú. Sem custo adicional e você ainda\n" +
                                "tem 50% de desconto em cinemas. \n" +
                                "\n" +
                                " \n" +
                                "<http://www.itau.com.br/_arquivosestaticos/Itaumail/servicos/canais/comunicacao-digital/varejo/IMG_02.jpg> 	\n" +
                                "E-mail nº 397419822366077.");
        
        ret.add(m1);
        
        return ret;
    } 
}
