package Challenge;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author onyxwizard
 * Date : 16-01-2026
 */

public class DbConnection {
    public static Connection connect(String url,String user,String pass) throws SQLException {
        return DriverManager.getConnection(url,user,pass);
    }
}
