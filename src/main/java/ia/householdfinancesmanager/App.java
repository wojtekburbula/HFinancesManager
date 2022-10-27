package ia.householdfinancesmanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import javafx.stage.Modality;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    public static Stage popup;
    public static Stage recurringPopup;
    public static SavedValues sv;
    private static String lastScene;
    private static String typePass;
    private static int pagePass;
    private static Transaction pass;
    private static int passIndex;

    @Override
    public void start(Stage stage) throws IOException {
        sv = FileHandler.loadSavedValues();
        scene = new Scene(loadFXML("mainmenu"), 600, 400);
        lastScene = "mainmenu";
        typePass = "";
        pass = null;
        passIndex = -1;
        recordRecurring();
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        stage.setTitle("Household Finances Manager");
        pagePass = 1;        
    }
    
    private void recordRecurring() throws IOException {
        LinkedList<RecurringTransaction> transactions = FileHandler.loadRecurring();
        RecurringTransaction current;
        Date today = new Date();
        for(int i = 0; i < transactions.size(); i++){
            current = transactions.get(i);
            switch(current.getInterval()){
                case "A year":
                    while(current.getDate().before(today)){
                        FileHandler.addTransaction(new Transaction(current.getName(),
                                current.getPerson(),
                                current.getDate(),
                                current.getAmount(),
                                current.getShop(),
                                current.getType()));
                        current.setDate(new Date(current.getDate().getYear()+1, current.getDate().getMonth(), current.getDate().getDate(), 12, 0));
                    }
                    break;
                case "Half a year":
                    while(current.getDate().before(today)){
                        FileHandler.addTransaction(new Transaction(current.getName(),
                                current.getPerson(),
                                current.getDate(),
                                current.getAmount(),
                                current.getShop(),
                                current.getType()));
                        current.setDate(new Date(current.getDate().getYear(), current.getDate().getMonth()+6, current.getDate().getDate(), 12, 0));
                    }
                    break;
                case "A month":
                    while(current.getDate().before(today)){
                        FileHandler.addTransaction(new Transaction(current.getName(),
                                current.getPerson(),
                                current.getDate(),
                                current.getAmount(),
                                current.getShop(),
                                current.getType()));
                        current.setDate(new Date(current.getDate().getYear(), current.getDate().getMonth()+1, current.getDate().getDate(), 12, 0));
                    }
                    break;
                case "Two weeks":
                    while(current.getDate().before(today)){
                        FileHandler.addTransaction(new Transaction(current.getName(),
                                current.getPerson(),
                                current.getDate(),
                                current.getAmount(),
                                current.getShop(),
                                current.getType()));
                        current.setDate(new Date(current.getDate().getYear(), current.getDate().getMonth(), current.getDate().getDate()+14, 12, 0));
                    }
                    break;
                case "A week":
                    while(current.getDate().before(today)){
                        FileHandler.addTransaction(new Transaction(current.getName(),
                                current.getPerson(),
                                current.getDate(),
                                current.getAmount(),
                                current.getShop(),
                                current.getType()));
                        current.setDate(new Date(current.getDate().getYear(), current.getDate().getMonth(), current.getDate().getDate()+7, 12, 0));
                    }
                    break;
            }
            FileHandler.writeRecurring(transactions);
        }       
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
        lastScene = fxml;
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml+".fxml"));
        return fxmlLoader.load();
    }
    
    public static FXMLLoader getFXMLLoader(String fxml){
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml+".fxml"));
        return fxmlLoader;
    }

    public static int getPagePass() {
        return pagePass;
    }

    public static void setPagePass(int pagePass) {
        App.pagePass = pagePass;
    }
    
    public static String getTypePass() {
        return typePass;
    }

    public static void setTypePass(String typePass) {
        App.typePass = typePass;
    }
    
    public static String getLastScene() {
        return lastScene;
    }

    public static void setLastScene(String lastScene) {
        App.lastScene = lastScene;
    }
    
    public static void setPass(Transaction t, int i){
        pass = t;      
        setPassIndex(i);
    }
    public static void setPassIndex(int i){     
        passIndex = i;
    }
    public static Transaction getPass(){
        return pass;
    }
    public static int getPassIndex(){
        return passIndex;
    }

    public static void main(String[] args) throws IOException {    
        launch();
    }
    
    public static void resetScene() throws IOException{
        setRoot(lastScene);
    }
    public static void resetRecurringPopup() throws IOException{
        Scene recurringScene = new Scene(loadFXML("recurringmenu"), 300, 200);
        recurringPopup.setScene(recurringScene);
    }
}