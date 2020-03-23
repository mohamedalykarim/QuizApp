package mohalim.app.quizapp.core.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.List;

@Entity
public class QuestionItem implements Parcelable {
    @NonNull
    @PrimaryKey
    String id;
    String questionText;

    @Ignore
    List<AnswerItem> questionAnswers;
    @Exclude
    String quizId;
    @Exclude
    boolean answered;

    @Exclude
    int chosenAnswer;

    @Exclude
    boolean chosenAnswerCorrect;

    public QuestionItem() {
    }

    protected QuestionItem(Parcel in) {
        id = in.readString();
        questionText = in.readString();
        questionAnswers = new ArrayList<>();
        in.readList(questionAnswers, AnswerItem.class.getClassLoader());
        quizId = in.readString();
        answered = in.readByte() != 0;
        chosenAnswer = in.readInt();
        chosenAnswerCorrect = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(questionText);
        dest.writeList(questionAnswers);
        dest.writeString(quizId);
        dest.writeByte((byte) (answered ? 1 : 0));
        dest.writeInt(chosenAnswer);
        dest.writeByte((byte) (chosenAnswerCorrect ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuestionItem> CREATOR = new Creator<QuestionItem>() {
        @Override
        public QuestionItem createFromParcel(Parcel in) {
            return new QuestionItem(in);
        }

        @Override
        public QuestionItem[] newArray(int size) {
            return new QuestionItem[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    @Ignore
    public List<AnswerItem> getQuestionAnswers() {
        return questionAnswers;
    }

    @Ignore
    public void setQuestionAnswers(List<AnswerItem> questionAnswers) {
        this.questionAnswers = questionAnswers;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    @Exclude
    public boolean isAnswered() {
        return answered;
    }

    @Exclude
    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    @Exclude
    public int getChosenAnswer() {
        return chosenAnswer;
    }

    @Exclude
    public void setChosenAnswer(int chosenAnswer) {
        this.chosenAnswer = chosenAnswer;
    }

    @Exclude
    public boolean isChosenAnswerCorrect() {
        return chosenAnswerCorrect;
    }


    @Exclude
    public void setChosenAnswerCorrect(boolean chosenAnswerCorrect) {
        this.chosenAnswerCorrect = chosenAnswerCorrect;
    }

    public static DiffUtil.ItemCallback<QuestionItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<QuestionItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull QuestionItem oldItem, @NonNull QuestionItem newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull QuestionItem oldItem, @NonNull QuestionItem newItem) {
            return oldItem.getId().equals(newItem.getId());
        }
    };
}
