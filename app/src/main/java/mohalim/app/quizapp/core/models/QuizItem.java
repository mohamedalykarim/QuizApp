package mohalim.app.quizapp.core.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.recyclerview.widget.DiffUtil;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.List;

import mohalim.app.quizapp.BR;


public class QuizItem extends BaseObservable implements Parcelable {

    private String id;
    private String quizName;
    private String quizDescription;
    private int questionCountWanted;
    private String owner;
    private int quizResult;
    private boolean isCheckAnswerWorking;
    private boolean showResults;
    private boolean saveResults;
    private String quizSwipeDirection;
    private String quizNavigationDirection;
    private List<UserItem> peopleCanAccess;
    int timeInMinutes;
    long startTimeInMilliscond;




    public QuizItem() {
    }

    protected QuizItem(Parcel in) {
        id = in.readString();
        quizName = in.readString();
        quizDescription = in.readString();
        questionCountWanted = in.readInt();
        owner = in.readString();
        quizResult = in.readInt();
        isCheckAnswerWorking = in.readByte() != 0;
        showResults = in.readByte() != 0;
        saveResults = in.readByte() != 0;
        quizSwipeDirection = in.readString();
        quizNavigationDirection = in.readString();
        peopleCanAccess = new ArrayList<>();
        in.readList(peopleCanAccess, AnswerItem.class.getClassLoader());
        timeInMinutes = in.readInt();
        startTimeInMilliscond = in.readLong();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(quizName);
        dest.writeString(quizDescription);
        dest.writeInt(questionCountWanted);
        dest.writeString(owner);
        dest.writeInt(quizResult);
        dest.writeByte((byte) (isCheckAnswerWorking ? 1 : 0));
        dest.writeByte((byte) (showResults ? 1 : 0));
        dest.writeByte((byte) (saveResults ? 1 : 0));
        dest.writeString(quizSwipeDirection);
        dest.writeString(quizNavigationDirection);
        dest.writeList(peopleCanAccess);
        dest.writeInt(timeInMinutes);
        dest.writeLong(startTimeInMilliscond);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuizItem> CREATOR = new Creator<QuizItem>() {
        @Override
        public QuizItem createFromParcel(Parcel in) {
            return new QuizItem(in);
        }

        @Override
        public QuizItem[] newArray(int size) {
            return new QuizItem[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Bindable
    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
        notifyPropertyChanged(mohalim.app.quizapp.BR.quizName);
    }

    @Bindable
    public String getQuizDescription() {
        return quizDescription;
    }

    public void setQuizDescription(String quizDescription) {
        this.quizDescription = quizDescription;
        notifyPropertyChanged(mohalim.app.quizapp.BR.quizDescription);
    }

    public int getQuestionCountWanted() {
        return questionCountWanted;
    }

    public void setQuestionCountWanted(int questionCountWanted) {
        this.questionCountWanted = questionCountWanted;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getQuizResult() {
        return quizResult;
    }

    public void setQuizResult(int quizResult) {
        this.quizResult = quizResult;
    }

    public boolean isCheckAnswerWorking() {
        return isCheckAnswerWorking;
    }

    public void setCheckAnswerWorking(boolean checkAnswerWorking) {
        isCheckAnswerWorking = checkAnswerWorking;
    }

    public String getQuizSwipeDirection() {
        return quizSwipeDirection;
    }

    public void setQuizSwipeDirection(String quizSwipeDirection) {
        this.quizSwipeDirection = quizSwipeDirection;
    }

    public String getQuizNavigationDirection() {
        return quizNavigationDirection;
    }

    public void setQuizNavigationDirection(String quizNavigationDirection) {
        this.quizNavigationDirection = quizNavigationDirection;
    }

    public List<UserItem> getPeopleCanAccess() {
        return peopleCanAccess;
    }

    public void setPeopleCanAccess(List<UserItem> peopleCanAccessQuiz) {
        this.peopleCanAccess = peopleCanAccessQuiz;
    }

    public int getTimeInMinutes() {
        return timeInMinutes;
    }

    public void setTimeInMinutes(int timeInMinutes) {
        this.timeInMinutes = timeInMinutes;
    }

    public boolean isShowResults() {
        return showResults;
    }

    public void setShowResults(boolean showResults) {
        this.showResults = showResults;
    }

    public boolean isSaveResults() {
        return saveResults;
    }

    public void setSaveResults(boolean saveResults) {
        this.saveResults = saveResults;
    }

    @Exclude
    public long getStartTimeInMilliscond() {
        return startTimeInMilliscond;
    }

    @Exclude
    public void setStartTimeInMilliscond(long startTimeInMilliscond) {
        this.startTimeInMilliscond = startTimeInMilliscond;
    }

    public static DiffUtil.ItemCallback<QuizItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<QuizItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull QuizItem oldItem, @NonNull QuizItem newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull QuizItem oldItem, @NonNull QuizItem newItem) {
            return oldItem.getId().equals(newItem.getId());
        }
    };



}
