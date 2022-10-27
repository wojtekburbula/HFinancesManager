/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ia.householdfinancesmanager;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author wirel
 */
public class SavedValues {
    private ArrayList types;
    private ArrayList shops;
    private ArrayList<String> persons;

    public SavedValues(ArrayList types, ArrayList shops, ArrayList persons){
        this.types = types;
        this.shops = shops;
        this.persons = persons;
        sort(types);
        sort(shops);
        sort(persons);
    }

    public ArrayList getTypes(){
        return types;
    }
    public ArrayList getShops(){
        return shops;
    }
    public ArrayList getPersons(){
        return persons;
    }

    public void setTypes(ArrayList types) {
        this.types = types;
    }
    public void setShops(ArrayList shops) {
        this.shops = shops;
    }
    public void setPersons(ArrayList persons) {
        this.persons = persons;
    }
    
    public void addType(String type) throws IOException{
        if(!types.contains(type))
            types.add(type);
        sort(types);
        FileHandler.writeSavedValues();
        
    }
    public void addShop(String shop) throws IOException{
        if(!shops.contains(shop))
            shops.add(shop);
        sort(shops);
        FileHandler.writeSavedValues();
    }
    public void addPerson(String name) throws IOException{
        if(!persons.contains(name))
            persons.add(name);
        sort(persons);
        FileHandler.writeSavedValues();
    }
    
    public void removeType(String type){
        types.remove(type);
    }
    public void removeShop(String shop){
        shops.remove(shop);
    }
    public void removePerson(String person){
        persons.remove(person);
    }
    
    final public void sort(ArrayList list){
        if(list.isEmpty()){
            return;
        }
        for(int i = 0; i < list.size()-1; i++){
            for(int j = 0; j < list.size()-1; j++){
                String first = (String)list.get(j);
                String second = (String)list.get(j+1);
                if(first.compareTo(second)>0){
                    list.set(j, second);
                    list.set(j+1, first);
                }
            }
        }
    } 

    @Override
    public String toString() {
        return "SavedValues{" + "\ntypes=" + types + ",\n shops=" + shops + ",\n names=" + persons + '}';
    }
    
}
