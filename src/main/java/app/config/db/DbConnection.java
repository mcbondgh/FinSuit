package app.config.db;
import app.stages.AppStages;

import java.sql.*;
import java.util.Properties;

public class DbConnection extends Variables{
    public DbConnection() {}
    public Connection getConnection()  {
        Connection connection = null;
        try {
            String LINK = loadProperties().getProperty("connection_path");
            String DB_USERNAME = loadProperties().getProperty("db_username");
            String DB_PASSWORD = loadProperties().getProperty("db_password");
            connection = DriverManager.getConnection(LINK, DB_USERNAME, DB_PASSWORD);
        }catch (SQLException e) {
            e.printStackTrace();
            AppStages.databaseFailedStage();
        }
        return connection;
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
