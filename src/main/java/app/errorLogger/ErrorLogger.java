package app.errorLogger;

import java.time.LocalDateTime;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ErrorLogger {
    private final Logger logger =  Logger.getLogger(getClass().getName());
        public ErrorLogger() {
            try {String errorName = "src/main/resources/app/logs/error_logger.txt";
                FileHandler fileHandler = new FileHandler(errorName, true);
                SimpleFormatter formatter = new SimpleFormatter();
                fileHandler.setFormatter(formatter);
                logger.addHandler(fileHandler);
           }catch (Exception ignored) {}
            }

            public void logMessage(String message,String sourceClass) {
                logger.logp(Level.SEVERE,sourceClass, "", message);
            }



}
