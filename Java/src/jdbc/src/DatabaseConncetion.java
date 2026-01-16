import com.mysql.jdbc.Driver;

import java.sql.*;

/**
 * @author onyxwizard
 * Date : 15-01-2026
 */

public class DatabaseConncetion {
    /**
     * Seven Steps to follow:
     * [1]: Import Package
     * [2]: Load and Register the driver
     * [3]: Establish conn
     * [4]:  create statement
     * [5]: Execute the sql statements/query
     * [6]: Process result
     * [7]: Close Conncetion
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // Load the Driver Class
        //Static class
        // Class.forName("com.mysql.cj.jdbc.Driver");  //[optional]
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "juser";
        String cred = "root";
        Connection con = DriverManager.getConnection(url,user,cred);
        System.out.println("Connection Established");

        // Create Statement
        Statement s = con.createStatement();

        // Query
        String query = "Select * from javadb";

        //Store in ResultSet
        ResultSet rt = s.executeQuery(query);

        rt.next(); // Must To skip Header

        int id = rt.getInt(1);
        String name = rt.getString(2);
        int age = rt.getInt(3);

        System.out.printf("The User id : %d | name : %s | age :%d",id,name,age);

        // Close the Connection
        con.close();
    }
}
