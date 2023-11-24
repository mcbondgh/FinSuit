package app.documents;

import app.errorLogger.ErrorLogger;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.io.File;

public class ImageReadWriter {

    static ErrorLogger log = new ErrorLogger();
    public static final String absolutePath = "C:\\Users\\Druglord\\Documents\\FinSuit\\src\\main\\resources\\app\\uploads";
    public static final File defaultImageName = new File("C:\\Users\\Druglord\\Documents\\FinSuit\\src\\main\\resources\\app\\images\\profile.png");
    public static void saveImageToDestination(String imageName, @NotNull ImageView uploadedImage ){
        Image selectedImage = uploadedImage.getImage();
        File destinationFolder = new File(absolutePath);
        if (selectedImage != null) {
            if (!destinationFolder.exists()) {destinationFolder.mkdirs();}
            try {
                File filePath = new File(destinationFolder , imageName);
                ImageIO.write(SwingFXUtils.fromFXImage(selectedImage, null), ".png", filePath);
            }catch (Exception e){
                    log.log(e.getLocalizedMessage());
            }
        }
    }

    @NotNull
    @Contract(pure = true)
    public static String displayImage(String imageName){
        return absolutePath + File.separator + imageName;
    }
}
