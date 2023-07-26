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

public class DbConnection extends Variables{
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, DB_USERNAME, DB_PASSWORD);
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
