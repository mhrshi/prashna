package prashna;

import com.google.gson.JsonObject;
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

public class GetFacultyQuizzes extends HttpServlet {

    Connection conn = Connector.connect();
    JsonParser parser = new JsonParser();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        ResultSet resultSet;
        try {
            resultSet = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
                      .executeQuery("SELECT * FROM QUIZZES");
            if (!resultSet.next()) {
                out.print("none");
                return;
            }
            resultSet.beforeFirst();
            resultSet.next();
            JsonObject masterJson = new JsonObject();
            JsonObject questions = parser.parse(resultSet.getString("questions")).getAsJsonObject();
            masterJson.add("content", questions);
            if (resultSet.getObject("SCORES") == null) {
                masterJson.addProperty("scores", "none");
            } else {
                masterJson.add("scores",
                        parser.parse(resultSet.getString("scores")).getAsJsonObject());
            }
            out.print(masterJson.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
            out.print("none");
        }        
    }
}
