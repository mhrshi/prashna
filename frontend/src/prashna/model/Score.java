package prashna.model;

public class Score {

    private String studentId;
    private String score;

    public Score(String studentId, String score) {
        this.studentId = studentId;
        this.score = score;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
