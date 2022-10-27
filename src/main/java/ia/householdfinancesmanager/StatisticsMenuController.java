/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ia.householdfinancesmanager;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.ListSpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;


/**
 * FXML Controller class
 *
 * @author wirel
 */

public class StatisticsMenuController implements Initializable {

    @FXML
    private ComboBox<String> valueCBox;
    @FXML
    private ComboBox<String> choiceCBox;
    @FXML
    private ComboBox<String> timeCBox;
    @FXML
    private Pane statisticsPane;
    @FXML
    private Pane tablePane;
    @FXML
    private Pane timePane;
    @FXML
    private Pane yearPane;
    @FXML
    private Pane monthYearPane;
    @FXML
    private Pane prevPeriodPane;
    @FXML
    private Spinner<Integer> yearSpinner;
    @FXML
    private Spinner<String> monthSpinner;
    @FXML
    private Spinner<Integer> monthYearSpinner;
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
    private ToggleButton tableTButton;
    @FXML
    private Text amountText;
    @FXML
    private Text numberText;
    @FXML
    private Text prevPeriodText;
    @FXML
    private StackedBarChart<String,Number> yearChart;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initValueCBox();
        try {
            initTimeBoxes();
            resetTimePanes();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        yearChart.setAnimated(false);
        tablePane.setVisible(false);
        statisticsPane.setVisible(false);
        tableTButton.setDisable(true);            
    }    
    
    private void initTable(LinkedList<Transaction> transactions) throws IOException{ 
        if(!transactionTable.getItems().isEmpty()){
            transactionTable.getItems().clear();
        }
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        shopColumn.setCellValueFactory(new PropertyValueFactory<>("shop"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        personColumn.setCellValueFactory(new PropertyValueFactory<>("person"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateString"));

        dateColumn.setReorderable(false);
        amountColumn.setReorderable(false);
        descriptionColumn.setReorderable(false);
        shopColumn.setReorderable(false);
        personColumn.setReorderable(false);
        typeColumn.setReorderable(false);     
        
        for(int i = 0; i < transactions.size(); i++){
            transactionTable.getItems().add(transactions.get(i));
        }
    }
    private void initStats(LinkedList<Transaction> transactions) throws IOException{
        if(choiceCBox.getItems().isEmpty()){
            return;
        }
        
        amountText.setText(String.format("%.2f", StatisticsHandler.getSum(transactions))+" PLN");
        numberText.setText(String.valueOf(transactions.size()));
        
        if(timeCBox.getValue().equals("A year") || valueCBox.getValue().equals("A year")){
            initChartData();
            yearChart.setVisible(true);
        }
        else{
            yearChart.setVisible(false);
        }
        
        if(!choiceCBox.getValue().equals("All time")){
            if(!timeCBox.getValue().equals("All time")){
                String prevPeriod = "";
                if(timeCBox.getValue()=="Last 30 days"){     
                    prevPeriod = StatisticsHandler.getPeriodComparison(transactions, StatisticsHandler.findValue(StatisticsHandler.getSecondLastMonth(), valueCBox.getValue(), choiceCBox.getValue()));
                }
                else if(timeCBox.getValue()=="A month"){
                    prevPeriod = StatisticsHandler.getPeriodComparison(transactions, StatisticsHandler.findValue(StatisticsHandler.getMonth(new Date(monthYearSpinner.getValue()-1900, DateHandler.parseMonthString(monthSpinner.getValue())-1, 1)), valueCBox.getValue(), choiceCBox.getValue()));
                }
                else if(timeCBox.getValue()=="A year"){
                    prevPeriod = StatisticsHandler.getPeriodComparison(transactions, StatisticsHandler.findValue(StatisticsHandler.getYear(new Date(yearSpinner.getValue()-1901, 0, 1)), valueCBox.getValue(), choiceCBox.getValue()));
                }
                
                if(!prevPeriod.equals("one is 0")){
                    prevPeriodPane.setVisible(true);
                    prevPeriodText.setText(prevPeriod);
                }
                else{
                    prevPeriodPane.setVisible(false);
                }
            }
            else{
                prevPeriodPane.setVisible(false);
            }
        }
        else{
            prevPeriodPane.setVisible(false);
        }
    }
    private void initChartData() throws IOException{
        yearChart.getData().clear();  
        XYChart.Series series = new XYChart.Series();
        for(int i = 0; i < 12; i++){           
            series.getData().add(new XYChart.Data(DateHandler.getMonthString(i), StatisticsHandler.getSum(StatisticsHandler.findValue(StatisticsHandler.getMonth(new Date(yearSpinner.getValue()-1900, i, 1)), valueCBox.getValue(), choiceCBox.getValue()))));
        }
        yearChart.getData().addAll(series);
    }
    
    private LinkedList<Transaction> getList() throws IOException{
        LinkedList<Transaction> transactions = new LinkedList<Transaction>();
        if(valueCBox.getValue() == "Time period"){
            if(choiceCBox.getValue()=="All time"){
                transactions = FileHandler.loadTransactions();
            }
            else if(choiceCBox.getValue()=="Last 30 days"){        
                transactions = StatisticsHandler.getLastMonth();
            }
            else if(choiceCBox.getValue()=="A month"){
                transactions = StatisticsHandler.getMonth(new Date(monthYearSpinner.getValue()-1900, DateHandler.parseMonthString(monthSpinner.getValue()), 1));
            }
            else if(choiceCBox.getValue()=="A year"){
                transactions = StatisticsHandler.getYear(new Date(yearSpinner.getValue()-1900, 0, 1));
            }   
        }
        else{ 
            if(timeCBox.getValue()=="All time"){
                transactions = FileHandler.loadTransactions();
            }
            else if(timeCBox.getValue()=="Last 30 days"){        
                transactions = StatisticsHandler.getLastMonth();
            }
            else if(timeCBox.getValue()=="A month"){
                transactions = StatisticsHandler.getMonth(new Date(monthYearSpinner.getValue()-1900, DateHandler.parseMonthString(monthSpinner.getValue()), 1));
            }
            else if(timeCBox.getValue()=="A year"){
                transactions = StatisticsHandler.getYear(new Date(yearSpinner.getValue()-1900, 0, 1));
            }
            transactions = StatisticsHandler.findValue(transactions, valueCBox.getValue(), choiceCBox.getValue());    
        }
        return transactions;
    }
       
    @FXML
    private void changeValue() throws IOException{
        initChoiceCBox();
        timePane.setVisible(false);
        resetTimePanes();
        tablePane.setVisible(false);
        statisticsPane.setVisible(false);
        tableTButton.setDisable(true);
    }
    @FXML
    private void changeChoice() throws IOException{       
            if(tableTButton.isSelected()){
                tablePane.setVisible(true); 
            }
            else{
                statisticsPane.setVisible(true);
            }
            if(!valueCBox.getValue().equals("Time period")){
                timePane.setVisible(true);
            }
            try{
            changeTimePeriod(); 
            }
            catch(Exception e){}           
            tableTButton.setDisable(false);      
    }
    @FXML
    private void changeTimePeriod() throws IOException{ 
        if(valueCBox.getValue() == "Time period"){
            switch(choiceCBox.getValue()){
                case "All time":
                    yearPane.setVisible(false);
                    monthYearPane.setVisible(false);
                    break;
                case "Last 30 days":
                    yearPane.setVisible(false);
                    monthYearPane.setVisible(false);
                    break;
                case "A month":
                    yearPane.setVisible(false);
                    monthYearPane.setVisible(true);
                    break;
                case "A year":
                    monthYearPane.setVisible(false);
                    yearPane.setVisible(true);
                    break;
            }
        }
        else{
            switch(timeCBox.getValue()){
                case "All time":
                    yearPane.setVisible(false);
                    monthYearPane.setVisible(false);
                    break;
                case "Last 30 days":
                    yearPane.setVisible(false);
                    monthYearPane.setVisible(false);
                    break;
                case "A month":
                    yearPane.setVisible(false);
                    monthYearPane.setVisible(true);
                    break;
                case "A year":
                    monthYearPane.setVisible(false);
                    yearPane.setVisible(true);
                    break;
            }
        }
        changeTime();
        if(tableTButton.isSelected()){
           tablePane.setVisible(true); 
        }
        else{
            statisticsPane.setVisible(true);
        }
    }
    @FXML
    private void changeTime() throws IOException{
        if(tableTButton.isSelected()){
           tablePane.setVisible(true); 
        }
        else{
            statisticsPane.setVisible(true);
        }
        LinkedList<Transaction> transactions = getList();  
        initTable(transactions);
        try{
            initStats(transactions);
        }
        catch(Exception e){          
        }
    }
    @FXML
    private void resetTimePanes() throws IOException{
        timePane.setVisible(false);
        yearPane.setVisible(false);
        monthYearPane.setVisible(false);
        timeCBox.getSelectionModel().selectFirst();
    }
    
    private void initChoiceCBox() throws IOException{
        choiceCBox.getItems().clear();
        switch(String.valueOf(valueCBox.getValue())){
            case "Type":
                choiceCBox.getItems().addAll(FileHandler.loadSavedValues().getTypes());
                break;
            case "Person":
                choiceCBox.getItems().addAll(FileHandler.loadSavedValues().getPersons());
                break;
            case "Shop":
                choiceCBox.getItems().addAll(FileHandler.loadSavedValues().getShops());
                break;
            case "Time period":
                choiceCBox.getItems().addAll("All time","Last 30 days", "A month","A year");
                break;
        }
    }    
    private void initValueCBox(){
        valueCBox.getItems().addAll("Type", "Person", "Shop", "Time period");      
    }
    private void initTimeBoxes() throws IOException{
        yearPane.setVisible(true);
        timeCBox.getItems().addAll("All time","Last 30 days", "A month","A year");
        timeCBox.getSelectionModel().selectFirst();
        
        System.out.println(new Date().getYear()+1900);
        IntegerSpinnerValueFactory yearVF = new IntegerSpinnerValueFactory(DateHandler.getFirstYear(), new Date().getYear()+1900);
        IntegerSpinnerValueFactory monthYearVF = new IntegerSpinnerValueFactory(DateHandler.getFirstYear(), new Date().getYear()+1900);
        yearSpinner.setValueFactory(yearVF);
        monthYearSpinner.setValueFactory(monthYearVF);
        
        ObservableList<String> months = FXCollections.observableArrayList(
               "January", "February", "March", "April", 
               "May", "June", "July", "August", 
               "September", "October", "November", "December");
        ListSpinnerValueFactory<String> monthVF = new ListSpinnerValueFactory<String>(months);
        monthSpinner.setValueFactory(monthVF);
    }
  
    @FXML 
    private void backToMain() throws IOException{
        App.setRoot("mainmenu");
    }
    
    @FXML
    private void toggleTable(){
            if(tableTButton.isSelected()){
            tableTButton.setText("Hide table");
            tablePane.setVisible(true);
            }
            else{
            tableTButton.setText("Show table");
            tablePane.setVisible(false);
            }     
    }
}
