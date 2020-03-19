package mohalim.app.quizapp.ui.main;

import android.app.Application;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.google.android.gms.common.util.ArrayUtils;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.Reusable;
import mohalim.app.quizapp.core.datasource.QuizDataSourceFactory;
import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.models.SessionItem;
import mohalim.app.quizapp.core.models.UserItem;
import mohalim.app.quizapp.core.repositories.QuizRepository;
import mohalim.app.quizapp.core.utils.AppExecutor;

@Reusable
public class MainViewModel extends ViewModel {
    private static final String TAG = "MainViewModel";

    private Executor executor;
    private LiveData<PagedList<QuizItem>> quizLiveData;
    public QuizItem initedQuiz;
    QuizDataSourceFactory quizDataSourceFactory;

    @Inject
    QuizRepository quizRepository;

    @Inject
    AppExecutor appExecutor;

    @Inject
    Application application;

    @Inject
    public MainViewModel() {

    }



    void init(EditText searchET){
        executor = Executors.newFixedThreadPool(5);

        quizDataSourceFactory = new QuizDataSourceFactory(quizRepository);
        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(5)
                .setPageSize(3)
                .build();

        quizLiveData= new LivePagedListBuilder(quizDataSourceFactory, config).build();

        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int [] counts = {0,2,5,8,11,15};
                if (ArrayUtils.contains(counts, count)){
                    quizDataSourceFactory.upDateQuizSearch(s.toString());
                }
            }



            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }


    public void initSession(final QuizItem quizItem){
        if (quizItem == null)return;
        this.initedQuiz = quizItem;

        appExecutor.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                SessionItem session = quizRepository.getCurrentSession(quizItem.getId());
                if (session == null){
                    // create session
                    SessionItem sessionItem = new SessionItem();
                    sessionItem.setCurrentQuestion(1);
                    sessionItem.setQuizId(quizItem.getId());
                    quizRepository.insertSession(sessionItem);
                    session = quizRepository.getCurrentSession(quizItem.getId());

                    quizRepository.startChooseQuestionToSession(quizItem, session);

                }else {
                    setQuizInitiatingNow(false);
                }

            }
        });


    }

    public MutableLiveData<Boolean> getQuizInitiatingNow() {
        return quizRepository.getQuizInitiatingNow();
    }

    public void setQuizInitiatingNow(boolean initiatingNow) {
        this.quizRepository.setQuizInitiatingNow(initiatingNow);
    }


    public void startAddQuiz(QuizItem quizItem) {
        quizRepository.startAddQuiz(quizItem);
    }

    public LiveData<PagedList<QuizItem>> getQuizLiveData() {
        return quizLiveData;
    }

    public void refresh() {
        quizDataSourceFactory.invalidate();
    }

    public void startUpdateQuiz(QuizItem quizItem) {
        quizRepository.startUpdateQuiz(quizItem);
    }

    public void startAccessQuiz(String quizId) {
        quizRepository.startAccessQuiz(quizId);
    }

    public MutableLiveData<QuizItem> getAccessedQuiz() {
        return quizRepository.getAccessedQuiz();
    }

    public void setAccessedQuiz(QuizItem quiz) {
        this.quizRepository.setAccessedQuiz(quiz);
    }

    public void startGetPersonByUsername(String username) {
        quizRepository.startGetPersonByUsername(username);
    }

    public MutableLiveData<List<UserItem>> getUsersSearchObservation() {
        return quizRepository.getUsersSearchObservation();
    }

    public void setUsersSearchObservation(List<UserItem> userItems) {
        this.quizRepository.setUsersSearchObservation(userItems);
    }

    public void startAddUserAccessToQuiz(QuizItem quizItem, String userName) {
        this.quizRepository.startAddUserAccessToQuiz(quizItem, userName);
    }

    public void startGetQuiz(String quizId) {
        this.quizRepository.startGetQuiz(quizId);
    }

    public MutableLiveData<QuizItem> getQuizitemObservation() {
        return this.quizRepository.getQuizitemObservation();
    }

    public void setQuizitemObservation(QuizItem quizItem) {
        this.quizRepository.setQuizitemObservation(quizItem);
    }
}
