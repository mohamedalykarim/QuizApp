package mohalim.app.quizapp.core.datasource;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import mohalim.app.quizapp.core.models.UserItem;
import mohalim.app.quizapp.core.repositories.QuizRepository;

public class UserQuizDataSourceFactory extends DataSource.Factory {
    private final QuizRepository quizRepository;
    private final UserItem userItem;
    private MutableLiveData<UserQuizDataSource> quizDataSourceMutableLiveData;
    UserQuizDataSource userQuizDataSource;
    private String quizSearch = "";

    public UserQuizDataSourceFactory(QuizRepository quizRepository, UserItem userItem) {
        this.quizDataSourceMutableLiveData = new MutableLiveData<>();
        this.quizRepository = quizRepository;
        this.userItem = userItem;
    }

    @NonNull
    @Override
    public DataSource create() {
        userQuizDataSource = new UserQuizDataSource();
        userQuizDataSource.updateQuizName(quizSearch);
        userQuizDataSource.setUserItem(userItem);
        quizDataSourceMutableLiveData.postValue(userQuizDataSource);
        return userQuizDataSource;
    }


    public MutableLiveData<UserQuizDataSource> getQuizDataSourceMutableLiveData() {
        return quizDataSourceMutableLiveData;
    }



    public void upDateQuizSearch(String quizSearch){
        this.quizSearch = quizSearch;
        invalidate();
    }

    public void invalidate(){
        if (userQuizDataSource != null)
            userQuizDataSource.invalidate();
    }
}
