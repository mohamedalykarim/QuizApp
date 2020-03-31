package mohalim.app.quizapp.core.datasource;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import mohalim.app.quizapp.core.repositories.QuizRepository;

public class AdminQuizDataSourceFactory extends DataSource.Factory {
    private final QuizRepository quizRepository;
    private MutableLiveData<AdminQuizDataSource> quizDataSourceMutableLiveData;
    AdminQuizDataSource adminQuizDataSource;
    private String quizSearch = "";

    public AdminQuizDataSourceFactory(QuizRepository quizRepository) {
        this.quizDataSourceMutableLiveData = new MutableLiveData<>();
        this.quizRepository = quizRepository;
    }

    @NonNull
    @Override
    public DataSource create() {
        adminQuizDataSource = new AdminQuizDataSource();
        adminQuizDataSource.uodateQuizName(quizSearch);
        quizDataSourceMutableLiveData.postValue(adminQuizDataSource);
        return adminQuizDataSource;
    }


    public MutableLiveData<AdminQuizDataSource> getQuizDataSourceMutableLiveData() {
        return quizDataSourceMutableLiveData;
    }



    public void upDateQuizSearch(String quizSearch){
        this.quizSearch = quizSearch;
        invalidate();
    }

    public void invalidate(){
        if (adminQuizDataSource != null)
        adminQuizDataSource.invalidate();
    }
}
