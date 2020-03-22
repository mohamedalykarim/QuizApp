package mohalim.app.quizapp.core.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SessionItem implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    int id;
    int currentQuestion;
    String quizId;
    long startTime;

    public SessionItem() {
    }

    protected SessionItem(Parcel in) {
        id = in.readInt();
        currentQuestion = in.readInt();
        quizId = in.readString();
        startTime = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(currentQuestion);
        dest.writeString(quizId);
        dest.writeLong(startTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SessionItem> CREATOR = new Creator<SessionItem>() {
        @Override
        public SessionItem createFromParcel(Parcel in) {
            return new SessionItem(in);
        }

        @Override
        public SessionItem[] newArray(int size) {
            return new SessionItem[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(int currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
