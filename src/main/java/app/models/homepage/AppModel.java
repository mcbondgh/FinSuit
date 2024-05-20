package app.models.homepage;

import app.documents.ImageReadWriter;
import app.models.MainModel;
import app.repositories.business.BusinessInfoEntity;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;

public class AppModel extends MainModel {
    protected void setVariables(Label appName, ImageView logo) {
        for (BusinessInfoEntity items : getBusinessInfo()) {
            appName.setText(items.getName());
            logo.setImage(new Image(new ByteArrayInputStream(items.getLogo())));
        }
    }

}//end of class...
