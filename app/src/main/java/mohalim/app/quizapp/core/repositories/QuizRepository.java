package mohalim.app.quizapp.core.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;

import mohalim.app.quizapp.core.database.AnswerDao;
import mohalim.app.quizapp.core.database.AppDatabase;
import mohalim.app.quizapp.core.database.QuestionDao;
import mohalim.app.quizapp.core.database.SessionDao;
import mohalim.app.quizapp.core.firebase.QuizFirebaseHandler;
import mohalim.app.quizapp.core.models.AnswerItem;
import mohalim.app.quizapp.core.models.QuestionItem;
import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.models.SessionItem;

public class QuizRepository {
    private final QuizFirebaseHandler quizFirebaseHandler;
    SessionDao sessionDao;
    QuestionDao questionDao;
    AnswerDao answerDao;

    @Inject
    public QuizRepository(QuizFirebaseHandler quizFirebaseHandler, Application application) {
        this.quizFirebaseHandler = quizFirebaseHandler;
        AppDatabase appDatabase = AppDatabase.getDatabase(application);
        sessionDao = appDatabase.sessionDao();
        questionDao = appDatabase.questionDao();
        answerDao = appDatabase.answerDao();
    }

    public void startAddQuiz(QuizItem quizItem){
        quizFirebaseHandler.startAddQuiz(quizItem);
    }


    public void startInsertQuestion(QuizItem quizItem, QuestionItem questionItem) {
        this.quizFirebaseHandler.startInsertQuestion(quizItem, questionItem);
    }

    public SessionItem getCurrentSession(String quizId) {
        return sessionDao.getSessionById(quizId);
    }

    public void insertSession(SessionItem sessionItem) {
        sessionDao.insert(sessionItem);
    }

    public void startChooseQuestionToSession(QuizItem quizItem, SessionItem currentSession){
        quizFirebaseHandler.startChooseQuestionToSession(quizItem, currentSession);
    }

    public int getSessionCount() {
        return sessionDao.getCount();
    }

    public int getQuestionCount() {
        return questionDao.getCount();
    }

    public int getAnswerCount() {
        return answerDao.getCount();
    }

    public LiveData<List<QuestionItem>> getQuestionsFromInternalObserved(String quizId) {
        return questionDao.getQuestionsFromInternalObserved(quizId);
    }

    public List<QuestionItem> getQuestionsFromInternal(String quizId) {
        return questionDao.getQuestionsFromInternal(quizId);
    }

    public LiveData<List<AnswerItem>> getAnswersForQuestionFromInternalObserved(String questionId, String quizId) {
        return answerDao.getAnswersForQuestionFromInternalObserved(questionId, quizId);
    }

    public List<AnswerItem> getAnswersForQuestionFromInternal(String questionId, String quizId) {
        return answerDao.getAnswersForQuestionFromInternal(questionId, quizId);

    }

    public void startUpdateQuiz(QuizItem quizItem) {
        quizFirebaseHandler.startUpdateQuiz(quizItem);

    }

    public void deleteAnswerFromInternal(String questionId, String quizId) {
        answerDao.deleteAnswerFromInternal(questionId, quizId);
    }

    public void deleteQuestionFromInternal(String questionId, String quizItemId) {
        questionDao.deleteQuestionFromInternal(questionId, quizItemId);
    }

    public void deleteSessionFromInternal(String quizId) {
        sessionDao.deleteSessionFromInternal(quizId);
    }

    public MutableLiveData<Boolean> getQuizInitiatingNow() {
        return quizFirebaseHandler.getQuizInitiatingNow();
    }

    public void setQuizInitiatingNow(boolean initiatingNow) {
        this.quizFirebaseHandler.setQuizInitiatingNow(initiatingNow);
    }

    public void startAccessQuiz(String quizId) {
        quizFirebaseHandler.startAccessQuiz(quizId);
    }

    public MutableLiveData<QuizItem> getAccessedQuiz() {
        return quizFirebaseHandler.getAccessedQuiz();
    }

    public void setAccessedQuiz(QuizItem quiz) {
        this.quizFirebaseHandler.setAccessedQuiz(quiz);
    }


    /***************************************************************************/
    /**                            User Data                                  **/
    /***************************************************************************/

    public void startSaveUserData() {
        quizFirebaseHandler.startSaveUserData();
    }
}
