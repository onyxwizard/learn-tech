import java.sql.*;

/**
 * @author onyxwizard
 * Date : 15-01-2026
 */

public class CreateTable {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // Load the Driver Class

        String url = "jdbc:mysql://localhost:3306/test";
        String user = "juser";
        String cred = "root";
        Connection con = DriverManager.getConnection(url,user,cred);
        System.out.println("Connection Established");

        // Create Statement
        Statement s = con.createStatement();

        // Create Table
        String query = "create table IF NOT EXISTS user(cid int,cname varchar(30))";

        //Execute [Update]
        s.executeUpdate(query);


        // Check If table exist
        query = "show tables";
        ResultSet rs = s.executeQuery(query);
        while (rs.next()){
            System.out.println(rs.getString(1));
        }
        System.out.println();
        System.out.println("Table create Successfully");

        // Close the Connection
        con.close();
    }
}
