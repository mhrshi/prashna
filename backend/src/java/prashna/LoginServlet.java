package prashna;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginServlet extends HttpServlet {
    
    Connection conn = Connector.connect();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String table = username.startsWith("IU") ? "STUDENT"
                                                 : "FACULTY";
        try {
            ResultSet set =
                conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
                    .executeQuery("SELECT * FROM " + table + " WHERE " +
                                  "USERNAME='" + username + "' AND " +
                                  "PASSWORD='" + password + "'");
            out.println(set.next() ? "valid" : "invalid");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }
}
