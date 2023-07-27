package app.config.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class Variables {
   private final Properties variables = new Properties();
   protected Properties loadConfiguration() {
      String PATH = "config.properties";
      try(InputStream inputStream = Variables.class.getClassLoader().getResourceAsStream(PATH)) {
         variables.load(inputStream);
      }catch (Exception e) {e.printStackTrace();}
      return variables;
   }


//  public static Properties loadConfiguration() {
//   Properties config = new Properties();
//   protected String URL = config.getProperty();
//   try (InputStream inputStream = Variables.class.getClassLoader().getResourceAsStream(PATH)) {
//    config.load(inputStream);
//   } catch (IOException e) {
//    e.printStackTrace();
//   }
//   return config;
//  }

}
