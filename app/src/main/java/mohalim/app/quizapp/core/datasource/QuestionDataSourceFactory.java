package mohalim.app.quizapp.core.datasource;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import mohalim.app.quizapp.core.repositories.QuizRepository;

public class QuestionDataSourceFactory extends DataSource.Factory {
    private final QuizRepository quizRepository;
    private final String quizId;
    private MutableLiveData<QuestionDataSource> questionDataSourceMutableLiveData;
    private QuestionDataSource questionDataSource;
    private String questionSearch = "";

    public QuestionDataSourceFactory(QuizRepository quizRepository, String quizId) {
        this.quizRepository = quizRepository;
        this.quizId = quizId;
        this.questionDataSourceMutableLiveData = new MutableLiveData<>();
    }

    @NonNull
    @Override
    public DataSource create() {
        questionDataSource = new QuestionDataSource(quizId);
        questionDataSource.setQuestionSearch(questionSearch);
        questionDataSourceMutableLiveData.postValue(questionDataSource);
        return questionDataSource;
    }

    public MutableLiveData<QuestionDataSource> getQuestionDataSourceMutableLiveData() {
        return questionDataSourceMutableLiveData;
    }

    public void invalidate(){
        if (questionDataSource != null)
        questionDataSource.invalidate();
    }

    public void upDateQuestionSearch(String questionSearch) {
        this.questionSearch = questionSearch;
        invalidate();
    }
}
