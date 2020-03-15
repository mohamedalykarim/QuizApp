package mohalim.app.quizapp.core.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.Exclude;

@Entity
public class AnswerItem implements Parcelable {

    @Exclude
    @NonNull
    @PrimaryKey(autoGenerate = true)
    int id;

    String answerText;
    boolean isCorrect;
    int arrange;

    @Exclude
    String questionId;

    @Exclude
    String quizId;

    public AnswerItem() {
    }

    protected AnswerItem(Parcel in) {
        id = in.readInt();
        answerText = in.readString();
        isCorrect = in.readByte() != 0;
        arrange = in.readInt();
        questionId = in.readString();
        quizId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(answerText);
        dest.writeByte((byte) (isCorrect ? 1 : 0));
        dest.writeInt(arrange);
        dest.writeString(questionId);
        dest.writeString(quizId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AnswerItem> CREATOR = new Creator<AnswerItem>() {
        @Override
        public AnswerItem createFromParcel(Parcel in) {
            return new AnswerItem(in);
        }

        @Override
        public AnswerItem[] newArray(int size) {
            return new AnswerItem[size];
        }
    };

    @Exclude
    public int getId() {
        return id;
    }

    @Exclude
    public void setId(int id) {
        this.id = id;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public int getArrange() {
        return arrange;
    }

    public void setArrange(int arrange) {
        this.arrange = arrange;
    }

    @Exclude
    public String getQuestionId() {
        return questionId;
    }

    @Exclude
    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    @Exclude
    public String getQuizId() {
        return quizId;
    }

    @Exclude
    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }
}
