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
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author wirel
 */
public class EditMenuController implements Initializable {

    private Transaction transaction;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField amountField;
    @FXML
    private ComboBox shopCBox;
    @FXML
    private ComboBox personCBox;
    @FXML
    private ComboBox typeCBox;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button finishButton;
    @FXML
    private Text noChangeWarning;
    @FXML
    private Text inputWarning;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        transaction = App.getPass();
        initFields();
        initCBoxes();
        noChangeWarning.setVisible(false);
        inputWarning.setVisible(false);
    }    
    
    private void initFields(){
        descriptionField.setText(transaction.getName());
        amountField.setText(String.valueOf(transaction.getAmount()));
    }
    private void initCBoxes(){
        shopCBox.getItems().addAll(sv.getShops());
        typeCBox.getItems().addAll(sv.getTypes());
        personCBox.getItems().addAll(sv.getPersons());
        
        shopCBox.getSelectionModel().select(transaction.getShop());
        personCBox.getSelectionModel().select(transaction.getPerson());
        typeCBox.getSelectionModel().select(transaction.getType());
        
        datePicker.setConverter(new StringConverter<LocalDate>(){
            private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate localDate){
                if(localDate==null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }
            public LocalDate fromString(String dateString){
                if(dateString==null || dateString.trim().isEmpty()){
                    return null;
                }
                return LocalDate.parse(dateString,dateTimeFormatter);
            }
        });
        datePicker.setValue(LocalDate.parse(DateHandler.formatDate(transaction.getDate()), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
    @FXML
    private void back() throws IOException {
        App.resetScene();
        popup.close();
    }
    @FXML
    private void finish() throws IOException{
        if(check()){
            FileHandler.switchTransaction(new Transaction(descriptionField.getText(), 
                String.valueOf(personCBox.getValue()), 
                Date.from(datePicker.getValue().atStartOfDay().toInstant(ZoneOffset.UTC)), 
                Double.parseDouble(amountField.getText()), 
                new Shop(String.valueOf(shopCBox.getValue())), 
                String.valueOf(typeCBox.getValue())), 
                App.getPassIndex());
            App.setPass(null, -1);
            back();          
        }
    }    
    @FXML
    private boolean check(){
        if(checkChanged() && checkInput()){
            return true;
        }
        else{
            return false;
        }
    }
            
    private boolean checkInput(){
        boolean correct = true;
        if(descriptionField.getText().isBlank() || String.valueOf(shopCBox.getValue()).isBlank() || String.valueOf(typeCBox.getValue()).isBlank() || String.valueOf(amountField.getText()).isBlank()){
            correct = false;
        }
        if(!amountField.getText().isBlank()){
            boolean amountWrong =  false;
            try{
                double value = Double.parseDouble(amountField.getText());    
            }
            catch (Exception e){
                correct = false;
            }           
        }
        finishButton.setDisable(!correct); 
        inputWarning.setVisible(!correct);
        if(noChangeWarning.isVisible()){
            noChangeWarning.setVisible(false);
        }
        return correct;
    }
    private boolean checkChanged(){
        boolean changed = true;
        if(descriptionField.getText().equals(transaction.getName()) && 
            String.valueOf(shopCBox.getValue()).equals(transaction.getShop().getName()) && 
            String.valueOf(typeCBox.getValue()).equals(transaction.getType()) && 
            Double.parseDouble(amountField.getText())==transaction.getAmount() && 
            String.valueOf(personCBox.getValue()).equals(transaction.getPerson()) &&
            Date.from(datePicker.getValue().atStartOfDay().toInstant(ZoneOffset.UTC)).equals(transaction.getDate())
            ){
            changed = false;
        }
        finishButton.setDisable(!changed);
        noChangeWarning.setVisible(!changed);
        
        if(inputWarning.isVisible() && !changed){
            inputWarning.setVisible(false);
        }
        return changed;
    }
}
