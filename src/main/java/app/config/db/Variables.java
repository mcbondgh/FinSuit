package app.config.db;

import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Variables {
      @NotNull
      static Properties loadProperties() {
            String path = "src/main/resources/app/config.properties";
            Properties properties = new Properties();
            try(InputStream stream = new FileInputStream(path)) {
                  properties.load(stream);
            }catch (Exception e) {
                  e.printStackTrace();
            }
            return properties;
      }


}
