import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author onyxwizard
 * Date : 15-01-2026
 */

public class UpdateRowData {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // Load the Driver Class

        String url = "jdbc:mysql://localhost:3306/test";
        String user = "juser";
        String cred = "root";
        Connection con = DriverManager.getConnection(url, user, cred);
        System.out.println("Connection Established");

        // Create Statement
        Statement s = con.createStatement();

        // Create Table
        String query = "Update user set cname='pk' WHERE cid=1";

        //Execute [Update]
        int val = s.executeUpdate(query);

        System.out.printf("Affected rows %d",val);

        // Close Conn
        con.close();
    }
}
