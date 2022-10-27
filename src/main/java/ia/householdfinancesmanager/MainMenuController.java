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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author wirel
 */
public class MainMenuController implements Initializable {

    @FXML
    private TableView dashTransactionTable;
    @FXML
    private TableColumn dashDescriptionColumn;
    @FXML
    private TableColumn dashAmountColumn;
    @FXML
    private TableColumn dashShopColumn;
    @FXML
    private TableColumn dashDateColumn;
    @FXML
    private Text amountText;
    @FXML
    private Text recurringText;
    @FXML
    private Button addTransactionButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            initTable();
            initText();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        App.setTypePass("");
    }    
    @FXML
    private void switchToHistoryMenu() throws IOException{
        App.setRoot("historymenu");
    }
    @FXML
    private void newTransaction() throws IOException{
        popup = new Stage();
        Scene scene = new Scene(loadFXML("newtransaction"), 600, 400);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setScene(scene);
        popup.show();
        popup.setTitle("Add new transaction");
    }
    @FXML
    private void switchToStatisticsMenu() throws IOException{
        App.setRoot("statisticsmenu");
    }
    @FXML
    private void switchToSettingsMenu() throws IOException{
        App.setRoot("settingsmenu");
    }
    @FXML
    private void switchToRecurringMenu() throws IOException{
        App.setTypePass("recurring");
        recurringPopup = new Stage();
        Scene scene = new Scene(loadFXML("recurringmenu"), 300, 200);
        recurringPopup.initModality(Modality.APPLICATION_MODAL);
        recurringPopup.setScene(scene);
        recurringPopup.show();
        recurringPopup.setTitle("Manage recurring transactions");
    }
    
    private void initTable() throws IOException{      
        dashDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        dashAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amountString"));
        dashShopColumn.setCellValueFactory(new PropertyValueFactory<>("shop"));
        dashDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateString"));

        dashDateColumn.setReorderable(false);
        dashAmountColumn.setReorderable(false);
        dashDescriptionColumn.setReorderable(false);
        dashShopColumn.setReorderable(false);
        
        
        LinkedList transactions = FileHandler.loadTransactions();
        if(transactions.size()>10){
            for(int i = 0; i < 10; i++){
                dashTransactionTable.getItems().add(transactions.get(i));
            }
        }
        else{
            for(int i = 0; i < transactions.size(); i++){
                dashTransactionTable.getItems().add(transactions.get(i));
            }
        }
    }
    
    private void initText() throws IOException{
        amountText.setText(String.format("%.2f", StatisticsHandler.getLastMonthSum())+" PLN");
        LinkedList<RecurringTransaction> recurring = FileHandler.loadRecurring();
        if(!recurring.isEmpty()){
            RecurringTransaction next = recurring.get(recurring.size()-1);
            recurringText.setText(next.getName()+", "+next.getAmountString()+" PLN"+", on "+next.getDateString());
        }       
    }
    
}
