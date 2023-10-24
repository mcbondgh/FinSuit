package app.errorLogger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ErrorLogger {
    private static Logger logger = Logger.getLogger("error-logs");

    public static void LogError() throws IOException {
        String filePath = "src/main/resources/app/logs/error_logs.txt";
        FileHandler fileHandler = new FileHandler(filePath);

        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);

        logger.addHandler(fileHandler);
        logger.setLevel(Level.ALL);

        logger.setUseParentHandlers(false);
    }

    public void logError(String errorMessage) {

    }
}
