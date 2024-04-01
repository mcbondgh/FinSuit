package app.controllers.transactions;

import app.controllers.homepage.AppController;
import app.models.finance.FinanceModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class TellerDashboardController extends FinanceModel implements Initializable{

    final int loggedInUser = getUserIdByName(AppController.activeUserPlaceHolder);
    /*******************************************************************************************************************
     *********************************************** FXML FILE EJECTIONS
     ********************************************************************************************************************/
    @FXML
    private Label currentBalaceLabel, loadedBalanceLabel, tellerAccountStatus;
    @FXML private VBox tellerBalanceContainer;


    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/



    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS
     ********************************************************************************************************************/

    public void initialize(URL url, ResourceBundle bundle){
        String host = url.getHost();
        System.out.println(host);
        setCashierDashboardParameters();
    }

    void setCashierDashboardParameters() {
        // <=49% balance color -> #ffe3e3, <=74% color -> #b28446 >=75% color -> #4591b5
        String userName = AppController.activeUserPlaceHolder;
        NumberFormat currency = NumberFormat.getInstance();
        getCashierCurrentAndLoadedBalance().forEach((key, value) -> {
            double loadedAmount = value.get(0);
            double currentAmount = value.get(1);
            double lessThan50 = (loadedAmount * 50) / 100;
            double lessThan75 = (loadedAmount * 75) / 100;

            if(currentAmount < lessThan50) {
                System.out.println(lessThan50);
                currentBalaceLabel.setStyle("-fx-text-fill:#ff0000");
                tellerBalanceContainer.setStyle("-fx-background-color:#ffe3e3; -fx-border-color:#ddd; -fx-border-radius:5px; -fx-background-radius:5px;");
            } else if(currentAmount >= lessThan50 && currentAmount < lessThan75) {
                System.out.println(lessThan75);
                currentBalaceLabel.setStyle("-fx-text-fill:#b28446");
                tellerBalanceContainer.setStyle("-fx-background-color:#fff; -fx-border-color:#ddd; -fx-border-radius:5px; -fx-background-radius:5px;");
            } else {
                currentBalaceLabel.setStyle("-fx-text-fill:#4591b5");
                tellerBalanceContainer.setStyle("-fx-background-color:#fff; -fx-border-color:#ddd; -fx-border-radius:5px; -fx-background-radius:5px;");
            }
            if (key.equals(userName)){
                loadedBalanceLabel.setText(currency.format(loadedAmount));
                currentBalaceLabel.setText(currency.format(currentAmount));
            }

        });
    }

    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS
     ********************************************************************************************************************/




}
