package app.config.db;

import app.stages.AppStages;

import java.sql.*;

public class DbConnection extends Variables{
    public DbConnection() {}
    Connection connection;
    public Connection getConnection(){
        try {
            String LINK = loadProperties().getProperty("connection_path");
            String DB_USERNAME = loadProperties().getProperty("db_username");
            String DB_PASSWORD = loadProperties().getProperty("db_password");
            connection = DriverManager.getConnection(LINK, DB_USERNAME, DB_PASSWORD);
//            connection.setAutoCommit(false);
        }catch (SQLException e) {
            AppStages.databaseFailedStage();
        }
        return connection;
        // London Billionaire marketing Association certificate
    }

    public void commitTransaction() throws SQLException {
        connection.commit();
    }
    public void rollBack() {
        try {
            connection.rollback();
        }catch (Exception ignore){}
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
