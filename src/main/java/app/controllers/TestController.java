package app.controllers;


import app.alerts.UserNotification;
import app.models.MainModel;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.controlsfx.control.spreadsheet.Grid;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TestController extends MainModel implements Initializable {
    public TestController() {
//        printItemIndex();
    }

    @FXML
    ImageView imageView;
    @FXML Button upload;
    @FXML
    ComboBox<Integer> showImage;
    @FXML
    Label fileName;
//    @FXML
//    VBox vBox;
//    @FXML
//    Pane pane;
//
//    public MFXScrollPane scrollPane;
//    @FXML
//    Button button;
//    @FXML
//    Label counter;
//
//    public void buttonClicked() {
//        vBox.maxHeight(1000.0);
//        vBox.setPadding(new Insets(10));
//        pane = new Pane();
//        counter = new Label();
//        int count = vBox.getChildren().size();
//        pane.setStyle(
//                "-fx-background-color:#eee; -fx-pref-width: 80px; -fx-pref-height: 90; " +
//                "-fx-border-color:#ddd; -fx-border-radius: 10px; -fx-padding:50px; " +
//                "-fx-background-radius: 10px"
//        );
//
//        counter.setStyle("-fx-alignment: center; -fx-font-family:poppins; -fx-font-size:20px; -fx-font-weight: bold; -fx-padding: 5px 10px");
//        counter.setText(String.valueOf(count));
//        pane.getChildren().add(counter);
//        vBox.getChildren().add(pane);
//    }
//
//
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        printItemIndex();
    }

//    private void printItemIndex() {
//       vBox.setOnMouseMoved(mouseEvent -> {
//           if (!vBox.getChildren().isEmpty()) {
//               for (int x =0; x < vBox.getChildren().size(); x++) {
//                   int finalX = x;
//                   vBox.getChildren().get(x).setOnMouseClicked(mouseEvent1 -> {
//                       System.out.println(finalX);
//                   });
//               }
//           }
//       });
//    }






}
