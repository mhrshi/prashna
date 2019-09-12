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

public class GetStudentQuizzes extends HttpServlet {

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
                      .executeQuery("SELECT ID, QUESTIONS FROM QUIZZES");
            if (!resultSet.next()) {
                out.print("none");
                return;
            }
            resultSet.beforeFirst();
            int returned = 0;
            JsonObject masterJson = new JsonObject();
            while (resultSet.next()) {
                returned++;
                JsonObject questions = parser.parse(resultSet.getString("questions")).getAsJsonObject();
                masterJson.add(String.valueOf(returned), questions);
            }
            masterJson.addProperty("returned", returned);
            out.print(masterJson.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
            out.print("none");
        }
    }
}
