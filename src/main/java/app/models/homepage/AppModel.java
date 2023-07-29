package app.models.homepage;

import app.fetchedData.BusinessInfoObject;
import app.models.MainModel;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AppModel extends MainModel {
    protected void setVariables(Label appName, ImageView logo) {
        for (BusinessInfoObject items : getBusinessInfo()) {
            appName.setText(items.getName());
            String getImageSource = "G:\\My Drive\\FINAL YEAR PROJECT\\FinSuit\\src\\main\\resources\\app\\uploads\\" + items.getLogo();
            logo.setImage(new Image(getImageSource));
        }
    }

}//end of class...
