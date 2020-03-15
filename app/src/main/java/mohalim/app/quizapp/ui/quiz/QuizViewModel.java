package mohalim.app.quizapp.ui.quiz;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import mohalim.app.quizapp.core.models.AnswerItem;
import mohalim.app.quizapp.core.models.QuestionItem;
import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.models.SessionItem;
import mohalim.app.quizapp.core.repositories.QuizRepository;
import mohalim.app.quizapp.core.utils.AppExecutor;

public class QuizViewModel extends ViewModel {
    @Inject
    QuizRepository quizRepository;

    @Inject
    AppExecutor appExecutor;

    public QuizItem quizItem;

    SessionItem currentSession;
    List<QuestionItem> questionItemList;

    public static final String TAG = "Quiz_app_tag";


    @Inject
    public QuizViewModel() {
    }

    public void initSession(final QuizItem quizItem){

        if (questionItemList == null){
            questionItemList = new ArrayList<>();
        }

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

                    currentSession = session;
                    quizRepository.startChooseQuestionToSession(quizItem, currentSession);

                }else {
                    currentSession = session;
                }



            }
        });


    }

    public void setQuestionItemList(List<QuestionItem> questionItemList) {
        this.questionItemList = questionItemList;
    }

    public LiveData<List<QuestionItem>> getQuestionsFromInternalObserved(String quizId) {
        return quizRepository.getQuestionsFromInternalObserved(quizId);
    }

    public LiveData<List<AnswerItem>> getAnswersForQuestionFromInternalObserved(String questionId, String quizId) {
        return quizRepository.getAnswersForQuestionFromInternalObserved(questionId, quizId);
    }


}
