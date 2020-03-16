package mohalim.app.quizapp.core.firebase;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import javax.inject.Inject;

import mohalim.app.quizapp.core.repositories.QuizRepository;

public class QuizDataSourceFactory extends DataSource.Factory {
    private final QuizRepository quizRepository;
    private MutableLiveData<QuizDataSource> quizDataSourceMutableLiveData;
    QuizDataSource quizDataSource;
    private String quizSearch = "";

    public QuizDataSourceFactory(QuizRepository quizRepository) {
        this.quizDataSourceMutableLiveData = new MutableLiveData<>();
        this.quizRepository = quizRepository;
    }

    @NonNull
    @Override
    public DataSource create() {
        quizDataSource = new QuizDataSource();
        quizDataSource.uodateQuizName(quizSearch);
        quizDataSourceMutableLiveData.postValue(quizDataSource);
        return quizDataSource;
    }


    public MutableLiveData<QuizDataSource> getQuizDataSourceMutableLiveData() {
        return quizDataSourceMutableLiveData;
    }



    public void upDateQuizSearch(String quizSearch){
        this.quizSearch = quizSearch;
        invalidate();
    }

    public void invalidate(){
        quizDataSource.invalidate();
    }
}
