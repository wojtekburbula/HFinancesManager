/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ia.householdfinancesmanager;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;


/**
 *
 * @author wirel
 */
public class StatisticsHandler {   
    
    public static LinkedList<Transaction> getLastMonth() throws IOException{
        LinkedList<Transaction> transactions = FileHandler.loadTransactions();
        LinkedList<Transaction> last = new LinkedList();
        Date date = DateHandler.getThirtyDays(new Date());
        for(int i = 0; i < transactions.size(); i++){
            if(transactions.get(i).getDate().after(date)){
                last.add(transactions.get(i));
            }
            else{
                break;
            }
        }
        return last;
    }
    public static LinkedList<Transaction> getSecondLastMonth() throws IOException{
        LinkedList<Transaction> transactions = FileHandler.loadTransactions();
        LinkedList<Transaction> last = new LinkedList();
        Date end = DateHandler.getThirtyDays(new Date());
        Date start = DateHandler.getThirtyDays(end);
        for(int i = 0; i < transactions.size(); i++){
            if(transactions.get(i).getDate().after(start) && transactions.get(i).getDate().before(end)){
                last.add(transactions.get(i));
            }
        }             
        return last;
    }
    
    public static LinkedList<Transaction> getMonth(Date d) throws IOException{
        LinkedList<Transaction> transactions = FileHandler.loadTransactions();
        LinkedList<Transaction> month = new LinkedList();
        for(int i = 0; i < transactions.size(); i++){
            if(transactions.get(i).getDate().getMonth() == d.getMonth() && transactions.get(i).getDate().getYear() == d.getYear()){
                month.add(transactions.get(i));
            }
        }
        return month;
    }
    
    public static LinkedList<Transaction> getYear(Date d) throws IOException{
        LinkedList<Transaction> transactions = FileHandler.loadTransactions();
        LinkedList<Transaction> year = new LinkedList();
        for(int i = 0; i < transactions.size(); i++){
            if(transactions.get(i).getDate().getYear() == d.getYear()){
                year.add(transactions.get(i));
            }
        }
        return year;
    }
    
    public static LinkedList<Transaction> findValue(LinkedList<Transaction> l, String value, String choice){
        LinkedList<Transaction> transactions = new LinkedList<Transaction>();
        if(value == "Person"){
            for(int i = 0; i < l.size(); i++){
                if(l.get(i).getPerson().equals(choice)){
                    transactions.add(l.get(i));
                }
            }
        }
        else if(value == "Type"){
            for(int i = 0; i < l.size(); i++){
                if(l.get(i).getType().equals(choice)){
                    transactions.add(l.get(i));
                }
            }
        }
        else if(value == "Shop"){
            for(int i = 0; i < l.size(); i++){
                if(l.get(i).getShop().getName().equals(choice)){
                    transactions.add(l.get(i));
                }
            }
        }
        return transactions;
    }
    
    public static double getLastMonthSum() throws IOException{
        LinkedList<Transaction> transactions = getLastMonth();
        double sum = 0;
        for(int i = 0; i < transactions.size(); i++){
            sum = sum + transactions.get(i).getAmount();
        }
        return sum;
    }
    
    public static double getSum(LinkedList<Transaction> transactions) throws IOException{
        double sum = 0;
        for(int i = 0; i < transactions.size(); i++){
            sum = sum + transactions.get(i).getAmount();
        }
        return sum;
    }
    
    public static String getPeriodComparison(LinkedList<Transaction> current, LinkedList<Transaction> previous) throws IOException{
        String result = "";
        double cSum = getSum(current);
        double pSum = getSum(previous);
        double percent = 0;
        if(cSum != 0 && pSum != 0){
            percent = (cSum/pSum - 1)*100;
            if(percent > 0){
                result=result+"+"; 
            }
            else{
                result = result+"-";
            }
            result= result + String.format("%.2f", Math.abs(percent))+"%";
        }
        else{
            result = "one is 0";
        }

        return result;
    }
}


