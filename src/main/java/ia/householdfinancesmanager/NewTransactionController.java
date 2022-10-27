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
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author wirel
 */
public class NewTransactionController implements Initializable {

    boolean correctOnce;
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
    private ComboBox presetCBox;
    @FXML
    private ComboBox intervalCBox;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button finishButton;
    @FXML
    private Text inputWarning;
    @FXML
    private RadioButton shopRadioButton;
    @FXML
    private RadioButton presetRadioButton;
    @FXML
    private Pane presetPane;
    @FXML
    private Pane amountPane;
    @FXML
    private Pane datePane;
    @FXML
    private Pane descriptionPane;
    @FXML
    private Pane shopPane;
    @FXML
    private Pane typePane;
    @FXML
    private Pane personPane;
    @FXML
    private Pane intervalPane;
    @FXML
    private Text dateText;
            
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(App.getLastScene().equals("settingsmenu")){
            presetPane.setVisible(false);
            amountPane.setVisible(false);
            datePane.setVisible(false);
            presetRadioButton.setSelected(true);
            amountField.setText("1");
            descriptionPane.setLayoutX(95);
            descriptionPane.setLayoutY(50);
            typePane.setLayoutX(305);
            typePane.setLayoutY(50);
            shopPane.setLayoutX(95);
            shopPane.setLayoutY(160);
            personPane.setLayoutX(305);
            personPane.setLayoutY(160);
        }
        else if(App.getTypePass().equals("recurring")){
            intervalPane.setVisible(true);
            dateText.setText("First date (can be past or future)");
            dateText.setFont(Font.font("system", FontPosture.REGULAR, 12));
        }
        correctOnce = false;
        try {
            App.sv = FileHandler.loadSavedValues();
            initCBoxes();
        } 
        catch (IOException ex) {}        
        inputWarning.setVisible(false);
    }    
    
    private void initCBoxes() throws IOException{
        shopCBox.getItems().addAll(sv.getShops());
        typeCBox.getItems().addAll(sv.getTypes());
        personCBox.getItems().addAll(sv.getPersons());
        intervalCBox.getItems().addAll("A year", "Half a year","A month", "Two weeks", "A week");
        personCBox.getSelectionModel().selectFirst();
        intervalCBox.getSelectionModel().select("A month");
        
        LinkedList<Preset> presets = FileHandler.loadPresets();
        for(int i = 0; i < presets.size(); i++){
            presetCBox.getItems().add(presets.get(i).getName());
        }
        
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
        datePicker.setValue(LocalDate.parse(DateHandler.formatDate(new Date()), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
    
    @FXML
    private void back() throws IOException {
        popup.close();
        App.resetScene();
    }
    @FXML
    private void finish() throws IOException{
        if(checkInput()){
            if(App.getLastScene().equals("settingsmenu")){
                saveValues();
                back();
                return;
            }
            else if(App.getTypePass().equals("recurring")){
                System.out.println("recurring");
                LinkedList<RecurringTransaction> transactions = FileHandler.loadRecurring();
                transactions.add(new RecurringTransaction(descriptionField.getText(), 
                    personCBox.getValue().toString(), 
                    Date.from(datePicker.getValue().atStartOfDay().toInstant(ZoneOffset.UTC)), 
                    Double.parseDouble(amountField.getText()), 
                    new Shop(String.valueOf(shopCBox.getValue())), 
                    typeCBox.getValue().toString(),
                    intervalCBox.getValue().toString()));
                FileHandler.writeRecurring(transactions);
                App.resetRecurringPopup();
            }
            else{
                FileHandler.addTransaction(new Transaction(descriptionField.getText(), 
                    personCBox.getValue().toString(), 
                    Date.from(datePicker.getValue().atStartOfDay().toInstant(ZoneOffset.UTC)), 
                    Double.parseDouble(amountField.getText()), 
                    new Shop(String.valueOf(shopCBox.getValue())), 
                    typeCBox.getValue().toString()));
            }
            if(checkPresetUse()){
                Preset preset = FileHandler.loadPresets().get(presetCBox.getSelectionModel().getSelectedIndex());
                LinkedList<Preset> presets= FileHandler.loadPresets();
                presets.remove(presetCBox.getSelectionModel().getSelectedIndex());
                preset.setTimesUsed(preset.getTimesUsed()+1);
                presets.add(preset);
                FileHandler.writePresets(presets);
            }
            saveValues();
            back();
        }
    }
    @FXML
    private void loadPreset() throws IOException{
        Preset preset = FileHandler.loadPresets().get(presetCBox.getSelectionModel().getSelectedIndex());
        descriptionField.setText(preset.getName());
        amountField.setText(String.format("%.2f",preset.getAmount()));
        typeCBox.getSelectionModel().select(preset.getType());
        shopCBox.getSelectionModel().select(preset.getShop());
        personCBox.getSelectionModel().select(preset.getPerson());
    }
    @FXML
    private boolean checkInput(){
        boolean correct = true;
        if(descriptionField.getText().isBlank() || 
                String.valueOf(shopCBox.getValue()).isBlank() || 
                String.valueOf(typeCBox.getValue()).isBlank() || 
                String.valueOf(amountField.getText()).isBlank()){
            correct = false;
        }
        if(!amountField.getText().isBlank()){
            try{
                double value = Double.parseDouble(amountField.getText());    
            }
            catch (Exception e){
                correct = false;
            }           
        }
        if(correct){
            correctOnce = true;
        }
        if(correctOnce){
            finishButton.setDisable(!correct); 
            inputWarning.setVisible(!correct);
        }
        return correct;
    }
    
    private boolean checkPresetUse() throws IOException{
        if(presetCBox.getSelectionModel().isEmpty()){
            return false;
        }
        Preset preset = FileHandler.loadPresets().get(presetCBox.getSelectionModel().getSelectedIndex());
        if(descriptionField.getText().equals(preset.getName()) &&
                typeCBox.getSelectionModel().getSelectedItem().toString().equals(preset.getType()) &&
                personCBox.getSelectionModel().getSelectedItem().toString().equals(preset.getPerson()) &&
                shopCBox.getSelectionModel().getSelectedItem().toString().equals(preset.getShop().getName()) &&
                Double.parseDouble(amountField.getText()) == preset.getAmount()
            ){
            return true;
        }
        else{
            return false;
        }
    }
    private void saveValues() throws IOException{
        ArrayList shops = sv.getShops();
        if(shopRadioButton.isSelected() && !shops.contains(shopCBox.getValue())){
            sv.addShop(String.valueOf(shopCBox.getValue()));
        }          
        if(presetRadioButton.isSelected()){
            LinkedList<Preset> presets = FileHandler.loadPresets();
            presets.add(new Preset(descriptionField.getText(),  
                    personCBox.getSelectionModel().getSelectedItem().toString(), 
                    Double.parseDouble(amountField.getText()),
                    new Shop(shopCBox.getSelectionModel().getSelectedItem().toString()),
                    typeCBox.getSelectionModel().getSelectedItem().toString(),
                    0)
            );
            FileHandler.writePresets(presets);
        }
    }
}
