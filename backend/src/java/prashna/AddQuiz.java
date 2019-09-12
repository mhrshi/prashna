package prashna;

import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddQuiz extends HttpServlet {
    
    Connection conn = Connector.connect();
    JsonParser parser = new JsonParser();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        
        String id = req.getParameter("quizId");
        String questions = req.getParameter("questions");
        String keys = req.getParameter("keys");
        
        try {
            int v = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
                .executeUpdate("INSERT INTO QUIZZES VALUES ('" +
                               id + "', '" +
                               questions + "', '" +
                               keys + "', null)");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
