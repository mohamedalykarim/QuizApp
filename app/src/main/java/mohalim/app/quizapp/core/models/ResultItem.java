package mohalim.app.quizapp.core.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ResultItem implements Parcelable {

    String id;
    String quizId;
    String userId;
    String Username;
    String Displayname;
    int resultScore;
    List<ResultQuestionItem> resultQuestion;


    public ResultItem() {
    }

    protected ResultItem(Parcel in) {
        id = in.readString();
        quizId = in.readString();
        userId = in.readString();
        Username = in.readString();
        Displayname = in.readString();
        resultScore = in.readInt();
        resultQuestion = new ArrayList<>();
        in.readList(resultQuestion, ResultQuestionItem.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(quizId);
        dest.writeString(userId);
        dest.writeString(Username);
        dest.writeString(Displayname);
        dest.writeInt(resultScore);
        dest.writeList(resultQuestion);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ResultItem> CREATOR = new Creator<ResultItem>() {
        @Override
        public ResultItem createFromParcel(Parcel in) {
            return new ResultItem(in);
        }

        @Override
        public ResultItem[] newArray(int size) {
            return new ResultItem[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getDisplayname() {
        return Displayname;
    }

    public void setDisplayname(String displayname) {
        Displayname = displayname;
    }

    public int getResultScore() {
        return resultScore;
    }

    public void setResultScore(int resultScore) {
        this.resultScore = resultScore;
    }

    public List<ResultQuestionItem> getResultQuestion() {
        return resultQuestion;
    }

    public void setResultQuestion(List<ResultQuestionItem> resultQuestionList) {
        this.resultQuestion = resultQuestionList;
    }


}
