package app.models.homepage;

import app.documents.ImageReadWriter;
import app.models.MainModel;
import app.repositories.business.BusinessInfoEntity;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class AppModel extends MainModel {
    protected void setVariables(Label appName, ImageView logo) {
        for (BusinessInfoEntity items : getBusinessInfo()) {
            appName.setText(items.getName());
            String getImageSource = ImageReadWriter.absolutePath +"\\" + items.getLogo();
//            logo.setImage(new Image(getImageSource));
        }
    }

}//end of class...
