package mohalim.app.quizapp.core.models;

public class StatisticsStudentsArrangeItem {
    String userId;
    String name;
    double grade;
    boolean isFinished;

    public StatisticsStudentsArrangeItem() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
}
