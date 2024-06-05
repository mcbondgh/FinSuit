package app.documents;

import app.alerts.UserNotification;
import app.errorLogger.ErrorLogger;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ImageReadWriter {
    static ErrorLogger log = new ErrorLogger();
    public static final String absolutePath = "/src/main/resources/app/uploads";
    public static final File defaultImageName = new File("src/main/resources/app/images/profile.png");
    static final long STANDARD_IMAGE_SIZE = (300 * 1024);

    public static void saveImageToDestination(String imageName, @NotNull ImageView uploadedImage ) throws FileNotFoundException {
        Image selectedImage = uploadedImage.getImage();
        File destinationFolder = new File(absolutePath);
        if (selectedImage != null) {
            if (!destinationFolder.exists()) {destinationFolder.mkdirs();}
            try {
                File filePath = new File(destinationFolder , imageName);
                ImageIO.write(SwingFXUtils.fromFXImage(selectedImage, null), ".png", filePath);
            }catch (Exception ignored){

            }
        }
    }

    @NotNull
    @Contract(pure = true)
    public static String displayImage(String imageName){
        return absolutePath + File.separator + imageName;
    }

    public static void uploadImageFile(MFXButton uploadButton, ImageView imageView ) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Preferred Image");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg",  "*.jpeg", "*.svg"));
            File imageFile = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());
            Image image = new Image(imageFile.getPath());
            long imageSize = imageFile.length();
            //CHECK IF THE IMAGE SIZE IS GREATER THAN THE ACTUAL PERMITTED SIZE ie 300kb.
            if (imageSize > STANDARD_IMAGE_SIZE) {
                new UserNotification().informationNotification("FILE TOO LARGE", "Selected file size should be less or equal to 300kb");
            } else  {
                imageView.setImage(image);
            }
        }catch (Exception ignore) {

        }
    }

    public static byte[] readImageStream(Image image) throws IOException {
        byte[] data = null;
        String path = "src/main/resources/app/images/profile.png";
        try {
            data = new FileInputStream(image.getUrl()).readAllBytes();
        }catch (Exception ex){
            data = new FileInputStream(path).readAllBytes();
            new ErrorLogger().logMessage(ex.getLocalizedMessage(),"readImageStream", ImageReadWriter.class.getName());
        }
        return data;
    }

    public static byte[] readImageStream(Image image, byte[] data) throws IOException {
        try {
            data = new FileInputStream(image.getUrl()).readAllBytes();
        }catch (Exception ex){
           return data;
        }
        return data;
    }

}
