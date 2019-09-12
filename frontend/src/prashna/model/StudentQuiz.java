package prashna.model;

import com.google.gson.JsonObject;

import java.util.ArrayList;

public class StudentQuiz {

    private String name;
    private String topic;
    private String course;
    private JsonObject questions;
    private ArrayList<Question> quesList = null;

    public StudentQuiz(JsonObject questions) {
        this.questions = questions;
        this.name = questions.get("quizName").getAsString();
        this.topic = questions.get("topic").getAsString();
        this.course = questions.get("course").getAsString();
    }

    public ArrayList<Question> getQuestions() {
        if (quesList != null) {
            return quesList;
        }
        quesList = new ArrayList<>();
        int limit = questions.get("questions").getAsInt();
        for (int i = 1; i <= limit; i++) {
            JsonObject ques = questions.get(String.valueOf(i)).getAsJsonObject();
            quesList.add(new Question(
                    ques.get("question").getAsString(),
                    ques.get("alpha").getAsString(),
                    ques.get("beta").getAsString(),
                    ques.get("gamma").getAsString(),
                    ques.get("delta").getAsString()
            ));
        }
        return quesList;
    }

    public String getQuizId() {
        return questions.get("quizId").getAsString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "";
    }
}
