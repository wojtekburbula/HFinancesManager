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
public class Transaction {
    private String name;
    private String person;
    private Date date;
    private String dateString;
    private String amountString;
    private double amount;
    private Shop shop;
    private String type;

    public Transaction(String name, String person, Date date, double amount, Shop shop, String type) {
        this.name = name;
        this.person = person;
        this.date = date;
        this.amount = amount;
        this.shop = shop;
        this.type = type;
        dateString = DateHandler.formatDate(date);
        amountString = String.format("%.2f", amount);
    }

    public String getName() {
        return name;
    }
    public String getPerson() {
        return person;
    }
    public Date getDate() {
        return date;
    }
    public double getAmount() {
        return amount;
    }
    public Shop getShop() {
        return shop;
    }
    public String getType() {
        return type;
    }  
    public String getDateString(){
        return dateString;
    }
    public String getAmountString(){
        return amountString;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setPerson(String person) {
        this.person = person;
    }
    public void setDate(Date date) {
        this.date = date;
        setDateString(DateHandler.formatDate(date));
    }
    public void setAmount(double amount) {
        this.amount = amount;
        setAmountString(String.format("%.2f", amount));
    }
    public void setShop(Shop shop) {
        this.shop = shop;
    }
    public void setType(String type) {
        this.type = type;
    }
    private void setDateString(String ds){
        this.dateString = ds;
    }
    private void setAmountString(String as){
        this.amountString = as;
    }
    
    public int compare(Transaction t1, Transaction t2){
        if(t1.getDate().after(t2.getDate()))
            return 1;
        else if(t2.getDate().after(t1.getDate()))
            return -1;
        else
            return 0;
    }

    @Override
    public String toString() {
        return "name=" + name + ", person=" + person + ", date=" + date + ", amount=" + amount + ", shop=" + shop + ", type=" + type + '}';
    }   
}

