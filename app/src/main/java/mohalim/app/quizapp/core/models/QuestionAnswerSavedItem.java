package mohalim.app.quizapp.core.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class QuestionAnswerSavedItem {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String quizId;
    private String questionId;
    private int chosenAnswer;
    private boolean isChosenAnswerCorrect;

    public QuestionAnswerSavedItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public int getChosenAnswer() {
        return chosenAnswer;
    }

    public void setChosenAnswer(int chosenAnswer) {
        this.chosenAnswer = chosenAnswer;
    }

    public boolean isChosenAnswerCorrect() {
        return isChosenAnswerCorrect;
    }

    public void setChosenAnswerCorrect(boolean chosenAnswerCorrect) {
        isChosenAnswerCorrect = chosenAnswerCorrect;
    }
}
