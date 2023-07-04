package app.config.db;
import java.sql.*;
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
