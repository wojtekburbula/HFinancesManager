/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ia.householdfinancesmanager;

import static ia.householdfinancesmanager.App.loadFXML;
import static ia.householdfinancesmanager.App.popup;
import static ia.householdfinancesmanager.App.recurringPopup;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author wirel
 */
public class RecurringMenuController implements Initializable {
    
    @FXML
    private Button removeButton;
    @FXML
    private ComboBox recurringCBox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        removeButton.setDisable(true);
        try {
            initCBox();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    @FXML
    private void add() throws IOException{
        App.setTypePass("recurring");
        popup = new Stage();
        Scene scene = new Scene(loadFXML("newtransaction"), 600, 400);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setScene(scene);
        popup.show();
        popup.setTitle("Add recurring transaction");
    }
    @FXML
    private void remove() throws IOException{
        App.setPassIndex(recurringCBox.getSelectionModel().getSelectedIndex());
        App.setTypePass("recurring");
        popup = new Stage();
        Scene scene = new Scene(loadFXML("removeconfirmation"), 300, 200);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setScene(scene);
        popup.show();
        popup.setTitle("Remove recurring transaction");
    }    
    
    @FXML
    private void enableRemove(){
        removeButton.setDisable(false);
    }
    
    private void initCBox() throws IOException{
        LinkedList<RecurringTransaction> recurring = FileHandler.loadRecurring();
        for(int i = 0; i < recurring.size(); i++){
            recurringCBox.getItems().add(recurring.get(i).getName());
        }
    }
    
    @FXML
    private void back() throws IOException{
        recurringPopup.close();
    }  

}
