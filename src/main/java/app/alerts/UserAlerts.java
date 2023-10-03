package app.alerts;

import javafx.beans.NamedArg;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class UserAlerts {

    private String title, header, content;

    public UserAlerts(@NamedArg("title") String title, @NamedArg("header") String header, @NamedArg("body") String content) {
        this.title = title;
        this.header = header;
        this.content = content;
    }

    public UserAlerts(String alertTitle, String alertHeader) {
        this.title = alertTitle;
        this.header = alertHeader;
    }

    public boolean confirmationAlert() {
        boolean flag = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, content);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.getButtonTypes().add(ButtonType.YES);
        alert.getButtonTypes().remove(ButtonType.OK);
        if (alert.showAndWait().get().equals(ButtonType.YES)) {
            flag = true;
        }
        return flag;
    }
    public String saveToDraft() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, content);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.getButtonTypes().add(ButtonType.YES);
        alert.getButtonTypes().add(ButtonType.NO);
        alert.getButtonTypes().remove(ButtonType.OK);
        return alert.showAndWait().get() == ButtonType.YES ? "YES" : alert.showAndWait().get().equals(ButtonType.NO) ? "NO" : "CANCEL";
    }

    public void warningAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public void informationAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public void errorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public void successAlert() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


}//end of class...
