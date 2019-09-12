package prashna.model;

import com.google.gson.JsonObject;

public class Quiz {

    private String name;
    private String branch;
    private String semester;

    public Quiz(JsonObject questions) {
        this.name = questions.get("quizName").getAsString();
        this.branch = questions.get("branch").getAsString();
        this.semester = questions.get("semester").getAsString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return "";
    }
}
