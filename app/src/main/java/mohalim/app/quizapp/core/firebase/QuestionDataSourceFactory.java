package mohalim.app.quizapp.core.firebase;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import mohalim.app.quizapp.core.repositories.QuizRepository;

public class QuestionDataSourceFactory extends DataSource.Factory {
    private final QuizRepository quizRepository;
    private final String quizId;
    private MutableLiveData<QuestionDataSource> questionDataSourceMutableLiveData;
    private QuestionDataSource questionDataSource;

    public QuestionDataSourceFactory(QuizRepository quizRepository, String quizId) {
        this.quizRepository = quizRepository;
        this.quizId = quizId;
        this.questionDataSourceMutableLiveData = new MutableLiveData<>();
    }

    @NonNull
    @Override
    public DataSource create() {
        questionDataSource = new QuestionDataSource(quizId);
        questionDataSourceMutableLiveData.postValue(questionDataSource);
        return questionDataSource;
    }

    public MutableLiveData<QuestionDataSource> getQuestionDataSourceMutableLiveData() {
        return questionDataSourceMutableLiveData;
    }

    public void invalidate(){
        questionDataSource.invalidate();
    }

}
