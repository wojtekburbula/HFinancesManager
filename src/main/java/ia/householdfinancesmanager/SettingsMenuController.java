/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ia.householdfinancesmanager;

import static ia.householdfinancesmanager.App.loadFXML;
import static ia.householdfinancesmanager.App.popup;
import static ia.householdfinancesmanager.App.sv;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author wirel
 */
public class SettingsMenuController implements Initializable {

    @FXML
    private ComboBox typeCBox;
    @FXML
    private ComboBox valuesCBox;
    @FXML
    private ComboBox presetCBox;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            initPresetCBox();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        typeCBox.getItems().addAll("Types","People","Shops");
        valuesCBox.setPromptText("Select values to edit");
    }    
    
    @FXML
    private void initValuesCBox(){
        valuesCBox.getItems().clear();
        if(typeCBox.getSelectionModel().getSelectedItem().equals("Types")){
            valuesCBox.getItems().addAll(sv.getTypes());
        }
        else if(typeCBox.getSelectionModel().getSelectedItem().equals("People")){
            valuesCBox.getItems().addAll(sv.getPersons());
        }
        else if(typeCBox.getSelectionModel().getSelectedItem().equals("Shops")){
            valuesCBox.getItems().addAll(sv.getShops());
        }  
        valuesCBox.setPromptText("");
    }
    @FXML
    private void initPresetCBox() throws IOException{
        LinkedList<Preset> presets = FileHandler.loadPresets();
        for(int i = 0; i < presets.size(); i++){
            presetCBox.getItems().add(presets.get(i).getName());
        }
    }
    
    @FXML
    private void add() throws IOException{
        if(valuesCBox.getSelectionModel().getSelectedItem().toString().isBlank()){
            return;
        }   
        
        if(typeCBox.getSelectionModel().getSelectedItem().equals("Types")){
            sv.addType(valuesCBox.getSelectionModel().getSelectedItem().toString());
        }
        else if(typeCBox.getSelectionModel().getSelectedItem().equals("People")){
            sv.addPerson(valuesCBox.getSelectionModel().getSelectedItem().toString());
        }
        else if(typeCBox.getSelectionModel().getSelectedItem().equals("Shops")){
            sv.addShop(valuesCBox.getSelectionModel().getSelectedItem().toString());
        }       
        initValuesCBox();
    }  
    @FXML
    private void remove() throws IOException{
        if(typeCBox.getSelectionModel().getSelectedItem().equals("Types")){
            sv.removeType(valuesCBox.getSelectionModel().getSelectedItem().toString());
        }
        else if(typeCBox.getSelectionModel().getSelectedItem().equals("People")){
            sv.removePerson(valuesCBox.getSelectionModel().getSelectedItem().toString());
        }
        else if(typeCBox.getSelectionModel().getSelectedItem().equals("Shops")){
            sv.removeShop(valuesCBox.getSelectionModel().getSelectedItem().toString());
        }
        valuesCBox.getSelectionModel().clearSelection();
        initValuesCBox();
    }
    @FXML
    private void removePreset() throws IOException{
        App.setPassIndex(presetCBox.getSelectionModel().getSelectedIndex());
        App.setTypePass("preset");
        popup = new Stage();
        Scene scene = new Scene(loadFXML("removeconfirmation"), 300, 200);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setScene(scene);
        popup.show(); 
        popup.setTitle("Remove preset");
    }
    @FXML
    private void addPreset() throws IOException{
        popup = new Stage();
        Scene scene = new Scene(loadFXML("newtransaction"), 600, 400);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setScene(scene);
        popup.show();
        popup.setTitle("Add new preset");
    }
    
    @FXML
    private void resetTransactions() throws IOException{
        App.setTypePass("transactions");
        popup = new Stage();
        Scene scene = new Scene(loadFXML("removeconfirmation"), 300, 200);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setScene(scene);
        popup.show(); 
        popup.setTitle("Reset transactions");
    }
    @FXML
    private void resetSavedValues() throws IOException{
        App.setTypePass("savedvalues");
        popup = new Stage();
        Scene scene = new Scene(loadFXML("removeconfirmation"), 300, 200);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setScene(scene);
        popup.show(); 
        popup.setTitle("Reset saved values");   
    }
    @FXML
    private void resetPresets() throws IOException{
        App.setTypePass("presets");
        popup = new Stage();
        Scene scene = new Scene(loadFXML("removeconfirmation"), 300, 200);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setScene(scene);
        popup.show(); 
        popup.setTitle("Reset presets");      
    }
    
    @FXML
    private void backToMain() throws IOException {
        App.setRoot("mainmenu");
    }
}
