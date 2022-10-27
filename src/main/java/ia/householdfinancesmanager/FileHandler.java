/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ia.householdfinancesmanager;

import static ia.householdfinancesmanager.App.sv;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 *
 * @author wirel
 */
public class FileHandler {
    
    static private BufferedReader getReader(String file) throws FileNotFoundException, IOException{
        FileReader fr = new FileReader(file+".csv"); 
        BufferedReader br = new BufferedReader(fr);
        return br;
    }
    static private FileWriter getWriter(String file) throws IOException{
        File f = new File(file);
        if (!f.exists()){ 
            f.createNewFile();
        }
        FileWriter writer = new FileWriter(file,false);
        return writer;
    }
    
    static public LinkedList<Transaction> loadTransactions() throws IOException{
        BufferedReader br = getReader("transactions");        
        String line="";
        LinkedList<Transaction> transactions = new LinkedList();
        
        while((line = br.readLine()) != null){
            String[] t = line.split(",");
            Transaction newTransaction = new Transaction(t[0], t[1], DateHandler.parseDate(t[2]), Double.parseDouble(t[3]), new Shop(t[4]), t[5]);
            transactions.add(newTransaction);      
        }
        return transactions;
    }
    static public LinkedList<RecurringTransaction> loadRecurring() throws IOException{
        BufferedReader br = getReader("recurring");        
        String line="";
        LinkedList<RecurringTransaction> transactions = new LinkedList();
        
        while((line = br.readLine()) != null){
            String[] t = line.split(",");
            RecurringTransaction newTransaction = new RecurringTransaction(t[0], t[1], DateHandler.parseDate(t[2]), Double.parseDouble(t[3]), new Shop(t[4]), t[5], t[6]);
            transactions.add(newTransaction);      
        }
        return transactions;
    }
    static public LinkedList<Preset> loadPresets() throws IOException{
        BufferedReader br = getReader("presets");        
        String line="";
        LinkedList<Preset> presets = new LinkedList();
        
        while((line = br.readLine()) != null){
            String[] t = line.split(",");
            Preset newPreset = new Preset(t[0], 
                    t[1], 
                    Double.valueOf(t[2]),
                    new Shop(t[3]),
                    t[4],
                    Integer.parseInt(t[5]));
            presets.add(newPreset);        
        }
        return presets;
    } 
    static public SavedValues loadSavedValues() throws IOException{
        BufferedReader br = getReader("savedvalues");        
        String line="";
        ArrayList[] ar = {new ArrayList(), new ArrayList(), new ArrayList()};
        int j = 0;
        
        while((line = br.readLine()) != null && j<3){            
            ArrayList list = new ArrayList();
            String[] t = line.split(",");          
            for(int i = 0; i < t.length; i++){ 
                list.add(t[i]);
            }
            ar[j] = list;
            j++;
        }
        SavedValues savedvalues = new SavedValues(ar[0], ar[1], ar[2]);
        return savedvalues;
    }
    
    public static void writeTransactions(LinkedList<Transaction> transactions) throws FileNotFoundException, IOException{
        FileWriter writer = getWriter("transactions.csv");
        transactions = sortTransactionsList(transactions);
        for (int i = 0; i < transactions.size(); i++) {
            writer.write(transactions.get(i).getName()
                    +","+transactions.get(i).getPerson()
                    +","+DateHandler.formatDate(transactions.get(i).getDate())
                    +","+transactions.get(i).getAmount()
                    +","+transactions.get(i).getShop()
                    +","+transactions.get(i).getType()+"\n");
        }
        writer.flush();
        writer.close();  
    }
    public static void writeRecurring(LinkedList<RecurringTransaction> transactions) throws FileNotFoundException, IOException{
        FileWriter writer = getWriter("recurring.csv");
        transactions = sortRecurringList(transactions);
        for (int i = 0; i < transactions.size(); i++) {
            writer.write(transactions.get(i).getName()+","+transactions.get(i).getPerson()+","+DateHandler.formatDate(transactions.get(i).getDate())+","+transactions.get(i).getAmount()+","+transactions.get(i).getShop()+","+transactions.get(i).getType()+","+transactions.get(i).getInterval()+"\n");
        }
        writer.flush();
        writer.close();  
    }
    public static void writePresets(LinkedList<Preset> presets) throws FileNotFoundException, IOException{
        FileWriter writer = getWriter("presets.csv");
        for (int i = 0; i < presets.size(); i++) {
            writer.write(presets.get(i).getName()+","+presets.get(i).getPerson()+","+presets.get(i).getAmount()+","+presets.get(i).getShop()+","+presets.get(i).getType()+","+presets.get(i).getTimesUsed()+"\n");
        }
        writer.flush();
        writer.close();  
    }
    
    public static void writeSavedValues() throws FileNotFoundException, IOException{
        FileWriter writer = getWriter("savedvalues.csv");
        ArrayList[] lists = {sv.getTypes(), sv.getShops(), sv.getPersons()};
        for(int i = 0; i < 3; i++){
            String s = "";
            if(!lists[i].isEmpty()){
                for(int j = 0; j < lists[i].size()-1; j++){
                    s = s+(lists[i].get(j)+",");
                }
                s = s+(lists[i].get(lists[i].size()-1)+"\n");
            }
            writer.write(s);
        }
        writer.flush();
        writer.close();  
    }
    
    private static LinkedList<Transaction> sortTransactionsList(LinkedList<Transaction> transactions){
        Collections.sort(transactions,new Comparator<Transaction>(){
            @Override
            public int compare(Transaction lhs,Transaction rhs) {
                if(lhs.getDate().after(rhs.getDate()))
                    return -1;
                else if(rhs.getDate().after(lhs.getDate()))
                    return 1;
                else
                    return 0;
                }
        });
        return transactions;  
    }
    private static LinkedList<RecurringTransaction> sortRecurringList(LinkedList<RecurringTransaction> transactions){
        Collections.sort(transactions, (Transaction lhs, Transaction rhs) -> {
            if(lhs.getDate().after(rhs.getDate()))
                return -1;
            else if(rhs.getDate().after(lhs.getDate()))
                return 1;
            else
                return 0;
        });
        return transactions;  
    }

    public static void addTransaction(Transaction t) throws IOException{
        LinkedList<Transaction> transactions = loadTransactions();
        transactions.add(t);
        writeTransactions(transactions);
    }
    public static void removeTransaction(int i) throws IOException{
        LinkedList<Transaction> transactions = loadTransactions();
        transactions.remove(i);
        writeTransactions(transactions);
    }
    public static void switchTransaction(Transaction t, int i) throws IOException{
        LinkedList<Transaction> transactions = loadTransactions();
        transactions.remove(i);
        transactions.add(t);
        writeTransactions(transactions);
    }
}



