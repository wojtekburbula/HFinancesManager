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
public class Preset extends Transaction{
    private int timesUsed;

    public Preset(String name, String person, double amount, Shop shop, String type, int timesUsed){
        super(name, person, new Date(), amount, shop, type);
        this.timesUsed = timesUsed;
    }

    public int getTimesUsed() {
        return timesUsed;
    }
    public String getName() {
        return super.getName();
    }
    public String getPerson() {
        return super.getPerson();
    }
    public Shop getShop() {
        return super.getShop();
    }
    public String getType() {
        return super.getType();
    }

    public void setTimesUsed(int timesUsed) {
        this.timesUsed = timesUsed;
    }
    public void setName(String name) {
        super.setName(name);
    }
    public void setPerson(String person) {
        super.setPerson(person);
    }
    public void setShop(Shop shop) {
        super.setShop(shop);
    }
    public void setType(String type) {
        super.setType(type);
    } 

    @Override
    public String toString() {
        return super.toString() + "Times used: "+timesUsed;
    }
    
}
