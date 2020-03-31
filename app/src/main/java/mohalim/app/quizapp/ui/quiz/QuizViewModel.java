package mohalim.app.quizapp.ui.quiz;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import mohalim.app.quizapp.core.models.AnswerItem;
import mohalim.app.quizapp.core.models.QuestionAnswerSavedItem;
import mohalim.app.quizapp.core.models.QuestionItem;
import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.models.ResultItem;
import mohalim.app.quizapp.core.models.ResultQuestionItem;
import mohalim.app.quizapp.core.models.SessionItem;
import mohalim.app.quizapp.core.repositories.QuizRepository;
import mohalim.app.quizapp.core.utils.AppExecutor;

public class QuizViewModel extends ViewModel {
    @Inject
    QuizRepository quizRepository;

    @Inject
    AppExecutor appExecutor;

    @Inject
    FirebaseAuth mAuth;


    public QuizItem quizItem;

    SessionItem currentSession;
    List<QuestionItem> questionItemList;

    public static final String TAG = "Quiz_app_tag";


    @Inject
    public QuizViewModel() {
            questionItemList = new ArrayList<>();

    }

    public void initSession(final QuizItem quizItem){
        appExecutor.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                SessionItem session = quizRepository.getCurrentSession(quizItem.getId());
                currentSession = session;
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

    public SessionItem getSessionFromInternal(String quizId) {
        return  quizRepository.getCurrentSession(quizId);
    }

    public void removeSessionForQuiz(String quizId) {
        quizRepository.removeSessionForQuiz(quizId);
    }

    public void resetQuiz(final QuizItem quizItem) {

        appExecutor.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<QuestionItem> questionItems = quizRepository.getQuestionsFromInternal(quizItem.getId());
                for (QuestionItem questionItem: questionItems){
                    quizRepository.deleteAnswerFromInternal(questionItem.getId(), quizItem.getId());
                    quizRepository.deleteQuestionFromInternal(questionItem.getId(), quizItem.getId());
                }
                quizRepository.deleteSessionFromInternal(quizItem.getId());
                quizRepository.deleteSavedAnswersForQuiz(quizItem.getId());
            }
        });
    }

    public void saveAnswer(final QuestionAnswerSavedItem questionAnswerSavedItem) {
        appExecutor.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                quizRepository.saveAnswer(questionAnswerSavedItem);
            }
        });
    }

    public QuestionAnswerSavedItem getSavedAnswer(String quizId, String questionId) {
        return quizRepository.getSavedAnswer(quizId, questionId);
    }


    public void startSaveResults(final List<QuestionItem> questionItems) {
        appExecutor.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                ResultItem resultItem = new ResultItem();
                resultItem.setQuizId(quizItem.getId());
                resultItem.setUserId(mAuth.getUid());
                resultItem.setUsername(mAuth.getCurrentUser().getEmail().replace("@gmail.com",""));
                resultItem.setDisplayname(mAuth.getCurrentUser().getDisplayName());

                double questionCount = questionItems.size();
                double correctAnswersCount = 0;

                List<ResultQuestionItem> resultQuestionItems = new ArrayList<>();

                for (QuestionItem questionItem : questionItems){
                    if (questionItem.isChosenAnswerCorrect()){
                        correctAnswersCount++;
                        Log.d(TAG, "run: correctAnswersCount "+ correctAnswersCount);
                    }

                    ResultQuestionItem resultQuestionItem = new ResultQuestionItem();
                    resultQuestionItem.setQuestionId(questionItem.getId());
                    resultQuestionItem.setQuestionText(questionItem.getQuestionText());
                    resultQuestionItem.setChosenAnswer(questionItem.getChosenAnswer());
                    resultQuestionItems.add(resultQuestionItem);
                    List<AnswerItem> answerItems = quizRepository.getAnswersForQuestionFromInternal(questionItem.getId(), quizItem.getId());

                    resultQuestionItem.setQuestionAnswers(answerItems);
                }
                resultItem.setResultQuestion(resultQuestionItems);

                double result = ((correctAnswersCount / questionCount) * 100);

                int resultInteger = (int) Math.round(result);

                resultItem.setResultScore(resultInteger);

                quizRepository.startSaveResults(resultItem);
            }
        });

    }

    public void resetAnswerSaved() {
        appExecutor.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                quizRepository.deleteSavedAnswersForQuiz(quizItem.getId());
            }
        });
    }
}
