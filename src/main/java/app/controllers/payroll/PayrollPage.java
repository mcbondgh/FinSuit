package app.controllers.payroll;

import app.specialmethods.SpecialMethods;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PayrollPage implements Initializable {

    @FXML
    protected CheckBox checkMonth;

    @FXML
    protected ListView<String> monthview;
    @FXML
    protected Label yearLabel ;
    @FXML
    private Label statusLabel;
    @FXML
    protected Button generatePayButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpecialMethods.loadMonth(monthview);
        currentYear();
    }

    private void currentYear(){
        LocalDate localDate = LocalDate.now();
        String year = String.valueOf(localDate.getYear());
        yearLabel.setText(year);
        selectedMonth();
    }

    void selectedMonth() {
        monthview.setOnMouseClicked(mouseEvent -> {
            String selectedMonth = monthview.getSelectionModel().getSelectedItem();
            if (selectedMonth != null) {
//                System.out.println( selectedMonth);
                statusLabel.setText("Generated");
                statusLabel.setStyle("-fx-background-color : Red");
                generatePayButton.setText("Paid");

            }

//            else if (selectedMonth=="APRIL"){
//                statusLabel.setText("UNPAID");
//                generatePayButton.setText("GERERATE");
//            }


        });
    }

}
