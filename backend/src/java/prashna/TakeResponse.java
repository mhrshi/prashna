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

public class TakeResponse extends HttpServlet {

    Connection conn = Connector.connect();
    JsonParser parser = new JsonParser();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        
        JsonObject response = parser.parse(req.getParameter("answers")).getAsJsonObject();
        String quizId = response.get("quizId").getAsString();
        String userId = response.get("userId").getAsString();
        ResultSet set;
        try {
            set = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
                    .executeQuery("SELECT KEYS, SCORES FROM QUIZZES WHERE id='" + quizId + "'");
            set.next();
            JsonObject scores;
            if (set.getObject("SCORES") == null) {
                scores = new JsonObject();
            } else {
                scores = parser.parse(set.getString("scores")).getAsJsonObject();
            }
            JsonObject keys = parser.parse(set.getString("keys")).getAsJsonObject();
            int score = 0;
            for (int i = 1; i <= keys.size(); i++) {
                if (response.get(String.valueOf(i)).getAsString()
                        .equals(keys.get(String.valueOf(i)).getAsString())) {
                    score++;
                }
            }
            scores.addProperty(userId, score);
            conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
                .executeUpdate("UPDATE QUIZZES SET SCORES='" + scores.toString() + "' WHERE id='" + quizId + "'");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
