package mohalim.app.quizapp.core.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ResultQuestionItem implements Parcelable {
    String questionId;
    String questionText;
    List<AnswerItem> questionAnswers;
    int chosenAnswer;

    public ResultQuestionItem() {
    }

    protected ResultQuestionItem(Parcel in) {
        questionId = in.readString();
        questionText = in.readString();
        chosenAnswer = in.readInt();
        questionAnswers = new ArrayList<>();
        in.readList(questionAnswers, AnswerItem.class.getClassLoader());

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(questionId);
        dest.writeString(questionText);
        dest.writeInt(chosenAnswer);
        dest.writeList(questionAnswers);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ResultQuestionItem> CREATOR = new Creator<ResultQuestionItem>() {
        @Override
        public ResultQuestionItem createFromParcel(Parcel in) {
            return new ResultQuestionItem(in);
        }

        @Override
        public ResultQuestionItem[] newArray(int size) {
            return new ResultQuestionItem[size];
        }
    };

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<AnswerItem> getQuestionAnswers() {
        return questionAnswers;
    }

    public void setQuestionAnswers(List<AnswerItem> questionAnswers) {
        this.questionAnswers = questionAnswers;
    }

    public int getChosenAnswer() {
        return chosenAnswer;
    }

    public void setChosenAnswer(int chosenAnswer) {
        this.chosenAnswer = chosenAnswer;
    }
}
