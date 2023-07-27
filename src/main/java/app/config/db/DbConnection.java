package app.config.db;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.moshi.Json;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class DbConnection extends Variables{
    public Connection getConnection() throws SQLException {
        String URL = loadConfiguration().getProperty("connection_path");
        String USERNAME = loadConfiguration().getProperty("db_username");
        String PASSWORD = loadConfiguration().getProperty("db_password");
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        //London Billionaire marketing Association certificate
    }

    protected ResultSet resultSet;
    protected PreparedStatement preparedStatement;
    protected Statement statement;

//    public static void main(String[] args) {
//        DbConnection conn = new DbConnection();
//        try {
//            System.out.println("Connected to server..");
//            conn.getConnection();
//        }catch (Exception e) {
//            System.err.println(e);
//        }
//    }

}//end of class...
