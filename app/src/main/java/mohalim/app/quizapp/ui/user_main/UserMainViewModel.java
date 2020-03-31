package mohalim.app.quizapp.ui.user_main;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.google.android.gms.common.util.ArrayUtils;

import java.util.Calendar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.Reusable;
import mohalim.app.quizapp.core.datasource.AdminQuizDataSourceFactory;
import mohalim.app.quizapp.core.datasource.UserQuizDataSourceFactory;
import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.models.SessionItem;
import mohalim.app.quizapp.core.models.UserItem;
import mohalim.app.quizapp.core.repositories.QuizRepository;
import mohalim.app.quizapp.core.utils.AppExecutor;

@Reusable
public class UserMainViewModel extends ViewModel {
    @Inject
    QuizRepository quizRepository;
    @Inject
    AppExecutor appExecutor;

    private LiveData<PagedList<QuizItem>> quizLiveData;
    public QuizItem initedQuiz;


    UserItem currentUser;
    private Executor executor;
    private UserQuizDataSourceFactory userQuizDataSourceFactory;

    @Inject
    public UserMainViewModel() {
    }

    void initForUser(EditText searchET){
        executor = Executors.newFixedThreadPool(5);

        userQuizDataSourceFactory = new UserQuizDataSourceFactory(quizRepository, currentUser);
        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(5)
                .setPageSize(3)
                .build();

        quizLiveData= new LivePagedListBuilder(userQuizDataSourceFactory, config).build();

        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int [] counts = {0,2,5,8,11,15};
                if (ArrayUtils.contains(counts, count)){
                    userQuizDataSourceFactory.upDateQuizSearch(s.toString());
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
                    Calendar calendar = Calendar.getInstance();
                    sessionItem.setStartTime(calendar.getTimeInMillis());
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


    public UserItem getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserItem currentUser) {
        this.currentUser = currentUser;
    }

    public void refresh() {
        userQuizDataSourceFactory.invalidate();
    }

    public LiveData<PagedList<QuizItem>> getQuizLiveData() {
        return quizLiveData;
    }


}
