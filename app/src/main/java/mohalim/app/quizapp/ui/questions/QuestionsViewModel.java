package mohalim.app.quizapp.ui.questions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import mohalim.app.quizapp.core.firebase.QuestionDataSourceFactory;
import mohalim.app.quizapp.core.models.QuestionItem;
import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.repositories.QuizRepository;

public class QuestionsViewModel extends ViewModel {
    Executor executor;
    QuestionDataSourceFactory questionDataSourceFactory;
    private LiveData<PagedList<QuestionItem>> questionsLiveData;

    @Inject
    QuizRepository quizRepository;

    @Inject
    public QuestionsViewModel() {
    }

    public void init(String quizId){
        executor = Executors.newFixedThreadPool(5);
        questionDataSourceFactory = new QuestionDataSourceFactory(quizRepository, quizId);
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10)
                .setPageSize(5)
                .build();

        questionsLiveData = new LivePagedListBuilder(questionDataSourceFactory, config).build();

    }

    public void startInsertQuestion(QuizItem quizItem, QuestionItem questionItem) {
        quizRepository.startInsertQuestion(quizItem, questionItem);
    }

    public LiveData<PagedList<QuestionItem>> getQuestionsLiveData() {
        return questionsLiveData;
    }

    public void refresh() {
        questionDataSourceFactory.invalidate();
    }
}
