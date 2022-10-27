/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ia.householdfinancesmanager;

import java.util.Date;

/**
 *
 * @author wirel
 */
public class RecurringTransaction extends Transaction{
    private String interval;

    public RecurringTransaction(String name, String person, Date date, double amount, Shop shop, String type, String interval) {
        super(name, person, date, amount, shop, type);
        this.interval = interval;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }
    
    @Override
    public String toString() {
        return "RecurringTransaction("+super.toString()+") interval=" + interval ;
    }
}


