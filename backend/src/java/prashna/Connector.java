package prashna;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connector {
    public static Connection connect() {
        Connection conn = null;
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/prashna", "root", "root");
        } catch (Exception e) {
            System.out.println(e);
        }
        return conn;
    }
}
