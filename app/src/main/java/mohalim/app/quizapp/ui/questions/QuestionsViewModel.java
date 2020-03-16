package mohalim.app.quizapp.ui.questions;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.google.android.gms.common.util.ArrayUtils;

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

    public void init(String quizId, EditText searchEt){
        executor = Executors.newFixedThreadPool(5);
        questionDataSourceFactory = new QuestionDataSourceFactory(quizRepository, quizId);
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10)
                .setPageSize(5)
                .build();

        questionsLiveData = new LivePagedListBuilder(questionDataSourceFactory, config).build();

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int [] counts = {0,2,5,8,11,15};
                if (ArrayUtils.contains(counts, count)){
                    questionDataSourceFactory.upDateQuestionSearch(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
