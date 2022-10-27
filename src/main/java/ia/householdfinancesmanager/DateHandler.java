/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ia.householdfinancesmanager;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 *
 * @author wirel
 */
public class DateHandler {
    
    public static Date parseDate(String string){
        String[] s = string.split("/");
        Date date = new Date(Integer.valueOf(s[2])-1900, Integer.valueOf(s[1])-1, Integer.valueOf(s[0]), 0, 0 );
        return date;
    }
    
    public static String formatDate(Date date){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");        
        return df.format(date);
    }
    
    public static int getFirstYear() throws IOException{
        LinkedList<Transaction> l = FileHandler.loadTransactions();
        return l.get(l.size()-1).getDate().getYear()+1900;
    }
   
    public static Date getThirtyDays(Date current){
        return new Date(current.getYear(), current.getMonth(), current.getDate()-30);
    }
    
    public static int parseMonthString(String s){
        switch(s){
            case "January":
                return 0;
            case "February":
                return 1;
            case "March":
                return 2;
            case "April":
                return 3;
            case "May":
                return 4;
            case "June":
                return 5;
            case "July":
                return 6;
            case "August":
                return 7;
            case "September":
                return 8;
            case "October":
                return 9;
            case "November":
                return 10;
            case "December":
                return 11;
        }
        return 1;
    }
    public static String getMonthString(int i){
        switch(i){
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
        }
        return "none";
    }
}


