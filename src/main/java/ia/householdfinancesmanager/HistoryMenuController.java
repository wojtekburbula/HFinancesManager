/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ia.householdfinancesmanager;

import static ia.householdfinancesmanager.App.loadFXML;
import static ia.householdfinancesmanager.App.popup;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author wirel
 */
public class HistoryMenuController implements Initializable {

    @FXML
    private TableView transactionTable;
    @FXML
    private TableColumn descriptionColumn;
    @FXML
    private TableColumn amountColumn;
    @FXML
    private TableColumn shopColumn;
    @FXML
    private TableColumn dateColumn;
    @FXML
    private TableColumn typeColumn;
    @FXML
    private TableColumn personColumn;
    @FXML 
    private Text noSelectionWarningEdit;
    @FXML 
    private Text noSelectionWarningRemove;
    @FXML
    private Text pageNumText;
    @FXML
    private Spinner<Integer> pageSpinner;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            initSpinner();
            initTable(App.getPagePass());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        noSelectionWarningEdit.setVisible(false);
        noSelectionWarningRemove.setVisible(false);       
    }    
      
    private void initTable(int page) throws IOException{      
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amountString"));
        shopColumn.setCellValueFactory(new PropertyValueFactory<>("shop"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateString"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        personColumn.setCellValueFactory(new PropertyValueFactory<>("person"));
        
        App.setPagePass(page);
        
        LinkedList transactions = FileHandler.loadTransactions();
        int i = (page-1)*25;
        int max = i+25;
        while(i < max && i<transactions.size()){
            transactionTable.getItems().add(transactions.get(i));
            i++;
        }
    }
    private void initSpinner() throws IOException{
        int maxValue = (int)(FileHandler.loadTransactions().size()/25)+1;
        pageSpinner.getStyleClass().add("split-arrows-horizontal");
        IntegerSpinnerValueFactory factory = new IntegerSpinnerValueFactory(1, maxValue);
        factory.setValue(App.getPagePass());
        pageSpinner.setValueFactory(factory);
        pageNumText.setText("of "+maxValue);
    }
    @FXML
    private void backToMain() throws IOException {
        App.setPagePass(1);
        App.setRoot("mainmenu");
    }
    @FXML
    private void newTransaction() throws IOException {
        popup = new Stage();
        Scene scene = new Scene(loadFXML("newtransaction"), 600, 400);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setScene(scene);
        popup.show();
        popup.setTitle("Add new transaction");
    }
    @FXML
    public void resetTable() throws IOException{
        transactionTable.getItems().clear();
        initTable(pageSpinner.getValue());
        transactionTable.scrollTo(transactionTable.getItems().get(0));
    }
    
    @FXML
    private void edit() throws IOException{
        if(!transactionTable.getSelectionModel().isEmpty()){
            App.setPass((Transaction)transactionTable.getSelectionModel().getSelectedItem(), (transactionTable.getSelectionModel().getSelectedIndex()+((pageSpinner.getValue()-1)*25)));
            popup = new Stage();
            Scene scene = new Scene(loadFXML("editmenu"), 600, 400);
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setScene(scene);
            popup.show(); 
            popup.setTitle("Edit transaction");
            noSelectionWarningEdit.setVisible(false);
        }
        else{
            noSelectionWarningEdit.setVisible(true);
        }
    }
    @FXML
    private void remove() throws IOException{
        if(!transactionTable.getSelectionModel().isEmpty()){
            noSelectionWarningRemove.setVisible(false);
            App.setPassIndex(transactionTable.getSelectionModel().getSelectedIndex());
            popup = new Stage();
            Scene scene = new Scene(loadFXML("removeconfirmation"), 300, 200);
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setScene(scene);
            popup.show(); 
            popup.setTitle("Remove transaction");
        }
        else{
            noSelectionWarningRemove.setVisible(true);
        }
    }
}
