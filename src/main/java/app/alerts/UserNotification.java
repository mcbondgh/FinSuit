package app.alerts;

import javafx.beans.NamedArg;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;


public class UserNotification {
   Notifications notifications = Notifications.create();
    public void  successNotification(String title, String text) {
        Image logo = new Image("G:\\My Drive\\FINAL YEAR PROJECT\\FinSuit\\src\\main\\resources\\app\\images\\successIcon.gif");
        notifications.graphic(new ImageView(logo));
        notifications.title(title);
        notifications.text(text);
        notifications.position(Pos.TOP_CENTER);
        this.notifications.show();
        notifications.hideAfter(Duration.seconds(3));
    }
    public void errorNotification(String title, String text) {
        notifications.title(title);
        notifications.text(text);
        notifications.position(Pos.TOP_CENTER);
        notifications.showError();
        notifications.hideAfter(Duration.seconds(3));
    }
    public void informationNotification(@NamedArg("Title")String title, @NamedArg("Text")String text) {
        notifications.title(title);
        notifications.text(text);
        notifications.position(Pos.TOP_CENTER);
        notifications.showInformation();
        notifications.hideAfter(Duration.seconds(3));
    }










}//END OF CLASS...