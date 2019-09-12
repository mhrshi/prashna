package prashna.core;

import com.google.gson.JsonObject;

public class Store {

    private static Store ourInstance = new Store();
    private static JsonObject store = new JsonObject();

    public static Store get() {
        return ourInstance;
    }

    public void saveAnswer(int questionNumber, String option) {
        store.addProperty(String.valueOf(questionNumber),
                          option);
    }

    public void setQuizId(String id) {
        store.addProperty("quizId", id);
    }

    public void setUserId(String id) {
        store.addProperty("userId", id);
    }

    public String getJsonString() {
        return store.toString();
    }

    private Store() {}
}
