/*
 * To change this license header, choose License Headers in Project Properties.  
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ia.householdfinancesmanager;

import static ia.householdfinancesmanager.App.popup;
import static ia.householdfinancesmanager.App.sv;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author wirel
 */
public class RemoveConfirmationController implements Initializable {
    
    @FXML
    Text mainText;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(App.getLastScene().equals("historymenu")){
            mainText.setText("Are you sure you want to delete this transaction?");
        }
        else if(App.getLastScene().equals("settingsmenu")){
            switch (App.getTypePass()) {
                case "transactions":
                    mainText.setText("Are you sure you want to delete ALL transactions?");
                    break;
                case "savedvalues":
                    mainText.setText("Are you sure you want to delete all saved names, shops and types?");
                    break;
                case "presets":
                    mainText.setText("Are you sure you want to delete all presets?");            
                    break;             
                case "preset":
                    mainText.setText("Are you sure you want to delete this preset?");            
                    break;                         
            }
        }
        else if(App.getTypePass().equals("recurring")){
             mainText.setText("Are you sure you want to delete this recurring transaction?");
        }
    } 
    
    @FXML
    private void back(){
        popup.close();
    }
    @FXML
    private void confirm() throws IOException{
        if(App.getLastScene().equals("historymenu")){
            FileHandler.removeTransaction(App.getPassIndex());
        }
        else if(App.getLastScene().equals("settingsmenu")){
            switch (App.getTypePass()) {
                case "transactions":
                    LinkedList<Transaction> transactions = new LinkedList();
                    FileHandler.writeTransactions(transactions);
                    break;
                case "savedvalues":
                    ArrayList list = new ArrayList();
                    sv.setPersons(list);
                    sv.setTypes(list);
                    sv.setShops(list);
                    FileHandler.writeSavedValues();
                    sv = FileHandler.loadSavedValues();
                    break;                   
                case "presets":
                    LinkedList<Preset> presets = new LinkedList();
                    FileHandler.writePresets(presets);        
                    break;                     
                case "preset":                   
                    LinkedList<Preset> preset = FileHandler.loadPresets();
                    preset.remove(App.getPassIndex());
                    FileHandler.writePresets(preset);        
                    break;                                        
            }
        }  
        else if(App.getTypePass().equals("recurring")){
            LinkedList<RecurringTransaction> recurring = FileHandler.loadRecurring();
            recurring.remove(App.getPassIndex());
            FileHandler.writeRecurring(recurring);   
            App.resetRecurringPopup();
            popup.close();
        }
        popup.close();
        App.resetScene();
    }
                
}
