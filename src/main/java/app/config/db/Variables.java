package app.config.db;

import java.io.FileInputStream;
import java.util.Properties;

public class Variables {
      static Properties loadProperties() {
            String path = "src/main/resources/app/config.properties";
            Properties properties = new Properties();
            try(FileInputStream stream = new FileInputStream(path)) {
                  properties.load(stream);
            }catch (Exception e) {
                  e.printStackTrace();
            }
            return properties;
      }


}
