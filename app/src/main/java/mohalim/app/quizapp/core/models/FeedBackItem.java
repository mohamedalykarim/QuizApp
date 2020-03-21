package mohalim.app.quizapp.core.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import mohalim.app.quizapp.BR;

public class FeedBackItem extends BaseObservable implements Parcelable {
    String id;
    String name;
    String userId;
    String feedBackText;
    String userName;
    String random;
    boolean reviewed;

    public FeedBackItem() {
    }

    protected FeedBackItem(Parcel in) {
        id = in.readString();
        name = in.readString();
        userId = in.readString();
        feedBackText = in.readString();
        userName = in.readString();
        random = in.readString();
        reviewed = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(userId);
        dest.writeString(feedBackText);
        dest.writeString(userName);
        dest.writeString(random);
        dest.writeByte((byte) (reviewed ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FeedBackItem> CREATOR = new Creator<FeedBackItem>() {
        @Override
        public FeedBackItem createFromParcel(Parcel in) {
            return new FeedBackItem(in);
        }

        @Override
        public FeedBackItem[] newArray(int size) {
            return new FeedBackItem[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Bindable
    public String getFeedBackText() {
        return feedBackText;
    }

    public void setFeedBackText(String feedBackText) {
        this.feedBackText = feedBackText;
        notifyPropertyChanged(BR.feedBackText);

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }
}
