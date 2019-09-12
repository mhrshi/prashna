package prashna.core;

import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Iterator;

public class ExcelHandler {

    private static final String[] XL_COLS = {"question", "alpha",
                                             "beta", "gamma", "delta"};
    private static final String[] XL_PROPS = {"quizName", "topic", "course", "branch"};

    private File file;
    private JsonObject json = new JsonObject();
    private JsonObject keyJson = new JsonObject();

    public ExcelHandler(File file) {
        this.file = file;
    }

    public void toJson() throws Exception {
        int ques = 0, col = 0;
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);
        getMetadata(workbook);

        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next();
        while (rowIterator.hasNext()) {
            ++ques;
            Row row = rowIterator.next();
            JsonObject jsonRow = new JsonObject();
            for (col = 0; col < XL_COLS.length; col++) {
                jsonRow.addProperty(XL_COLS[col],
                                    row.getCell(col).getStringCellValue());
            }
            json.add(String.valueOf(ques), jsonRow);
            keyJson.addProperty(String.valueOf(ques),
                                row.getCell(5).getStringCellValue());
        }
        pushToServer();
    }

    private void pushToServer() throws UnirestException {
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String quizId = json.get("quizName").getAsString();
        json.addProperty("quizId", quizId);
        HttpResponse<String> res = Unirest.post("http://localhost:8080/Prashna/addQuiz")
               .field("quizId", quizId)
               .field("questions", json.toString())
               .field("keys", keyJson.toString())
               .asString();
    }

    private void getMetadata(XSSFWorkbook workbook) {
        POIXMLProperties.CustomProperties customProps = workbook.getProperties()
                                                                .getCustomProperties();
        for (String str : XL_PROPS) {
            json.addProperty(str, getStringProp(customProps, str));
        }
        json.addProperty("semester", getIntProp(customProps, "semester"));
        json.addProperty("questions", getIntProp(customProps, "questions"));
        CTProperty randomizeProp = customProps.getProperty("randomize");
        if (randomizeProp.getBool()) {
            json.addProperty("randomize", true);
            json.addProperty("randomNumber",
                             getIntProp(customProps, "randomNumber"));
        }
    }

    private int getIntProp(POIXMLProperties.CustomProperties customProps, String propName) {
        return customProps.getProperty(propName).getI4();
    }

    private String getStringProp(POIXMLProperties.CustomProperties customProps, String propName) {
        return customProps.getProperty(propName).getLpwstr();
    }
}
