module ia.householdfinancesmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens ia.householdfinancesmanager to javafx.fxml;
    
    exports ia.householdfinancesmanager;
}
