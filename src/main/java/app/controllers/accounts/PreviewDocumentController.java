package app.controllers.accounts;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PreviewDocumentController {

    @FXML
    private static WebView webView;
    @FXML private static StackPane stackPane;

    public static void previewPdfFile(String fileName) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Preview Document");
        stage.centerOnScreen();
        webView = new WebView();
        Desktop.getDesktop().browse(new File(fileName).toURI());
        String replaceSlash = fileName.replace("\\", "/");
        webView.getEngine().load("file:///"+replaceSlash);
        stackPane = new StackPane(webView);
        Scene scene = new Scene(stackPane);
        stage.setScene(scene);
        stage.show();


    }


}
