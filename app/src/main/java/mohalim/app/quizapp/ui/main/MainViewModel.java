package mohalim.app.quizapp.ui.main;

import android.app.Application;
import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.Reusable;
import mohalim.app.quizapp.core.firebase.QuizDataSourceFactory;
import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.models.SessionItem;
import mohalim.app.quizapp.core.repositories.QuizRepository;
import mohalim.app.quizapp.core.utils.AppExecutor;
import mohalim.app.quizapp.core.utils.Constants;
import mohalim.app.quizapp.ui.quiz.QuizActivity;

@Reusable
public class MainViewModel extends ViewModel {
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
        init();
    }



    void init(){
        executor = Executors.newFixedThreadPool(5);

        quizDataSourceFactory = new QuizDataSourceFactory(quizRepository);
        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(5)
                .setPageSize(3)
                .build();

        quizLiveData= new LivePagedListBuilder(quizDataSourceFactory, config).build();
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
}
