package mohalim.app.quizapp.core.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;

import mohalim.app.quizapp.core.database.AnswerDao;
import mohalim.app.quizapp.core.database.AppDatabase;
import mohalim.app.quizapp.core.database.QuestionAnswerSavedDao;
import mohalim.app.quizapp.core.database.QuestionDao;
import mohalim.app.quizapp.core.database.SessionDao;
import mohalim.app.quizapp.core.firebase.QuizFirebaseHandler;
import mohalim.app.quizapp.core.models.AnswerItem;
import mohalim.app.quizapp.core.models.FeedBackItem;
import mohalim.app.quizapp.core.models.QuestionAnswerSavedItem;
import mohalim.app.quizapp.core.models.QuestionItem;
import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.models.ResultItem;
import mohalim.app.quizapp.core.models.SessionItem;
import mohalim.app.quizapp.core.models.UserItem;

public class QuizRepository {
    private final QuizFirebaseHandler quizFirebaseHandler;
    SessionDao sessionDao;
    QuestionDao questionDao;
    AnswerDao answerDao;
    QuestionAnswerSavedDao questionAnswerSavedDao;

    @Inject
    public QuizRepository(QuizFirebaseHandler quizFirebaseHandler, Application application) {
        this.quizFirebaseHandler = quizFirebaseHandler;
        AppDatabase appDatabase = AppDatabase.getDatabase(application);
        sessionDao = appDatabase.sessionDao();
        questionDao = appDatabase.questionDao();
        answerDao = appDatabase.answerDao();
        questionAnswerSavedDao = appDatabase.questionAnswerSavedDao();
    }

    public void startAddQuiz(QuizItem quizItem){
        quizFirebaseHandler.startAddQuiz(quizItem);
    }

    public void startGetQuiz(String quizId) {
        this.quizFirebaseHandler.startGetQuiz(quizId);
    }

    public MutableLiveData<QuizItem> getQuizitemObservation() {
        return this.quizFirebaseHandler.getQuizitemObservation();
    }

    public void startAddUserAccessToQuiz(QuizItem quizItem, String userName) {
        this.quizFirebaseHandler.startAddUserAccessToQuiz(quizItem, userName);
    }



    public void setQuizitemObservation(QuizItem quizItem) {
        this.quizFirebaseHandler.setQuizitemObservation(quizItem);
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

    public void removeSessionForQuiz(String quizId) {
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


    public void saveAnswer(QuestionAnswerSavedItem questionAnswerSavedItem) {
        questionAnswerSavedDao.insert(questionAnswerSavedItem);
    }

    public QuestionAnswerSavedItem getSavedAnswer(String quizId, String questionId) {
        return this.questionAnswerSavedDao.getQuestionAnsweredSavedForQuestion(quizId, questionId);
    }

    public void deleteSavedAnswersForQuiz(String quizId) {
        this.questionAnswerSavedDao.deleteQuestionAnsweredSavedForQuiz(quizId);
    }


    /***************************************************************************/
    /**                            User Data                                  **/
    /***************************************************************************/

    public void startSaveUserData() {
        quizFirebaseHandler.startSaveUserData();
    }

    public void startGetPersonByUsername(String username) {
        quizFirebaseHandler.startGetPersonByUsername(username);
    }

    public MutableLiveData<List<UserItem>> getUsersSearchObservation() {
        return quizFirebaseHandler.getUsersSearchObservation();
    }

    public void setUsersSearchObservation(List<UserItem> userItems) {
        this.quizFirebaseHandler.setUsersSearchObservation(userItems);
    }

    public void startGetCurrentUserDetails() {
        this.quizFirebaseHandler.startGetCurrentUserDetails();
    }

    public MutableLiveData<UserItem> getCurrentUserDetailsObservation() {
        return quizFirebaseHandler.getCurrentUserDetailsObservation();
    }

    public void setCurrentUserDetailsObservation(UserItem currentUserDetails) {
        this.quizFirebaseHandler.setCurrentUserDetailsObservation(currentUserDetails);
    }




    /***************************************************************************/
    /**                             Feedback                                  **/
    /***************************************************************************/

    public void startAddFeedBack(FeedBackItem feedBackItem) {
        this.quizFirebaseHandler.startAddFeedBack(feedBackItem);
    }

    public void startGetMyFeedBack() {
        this.quizFirebaseHandler.startGetMyFeedBack();

    }

    public MutableLiveData<FeedBackItem> getMyFeedBackObservation() {
        return quizFirebaseHandler.getMyFeedBackObservation();
    }

    public void setMyFeedBackObservation(FeedBackItem feedBackItem) {
        this.quizFirebaseHandler.setMyFeedBackObservation(feedBackItem);
    }

    public void startGetRandomFeedback(int count) {
        this.quizFirebaseHandler.startGetRandomFeedback(count);
    }

    public MutableLiveData<List<FeedBackItem>> getRandomFeedBack() {
        return quizFirebaseHandler.getRandomFeedBack();
    }

    public void setRandomFeedBack(List<FeedBackItem> randomFeedBack) {
        this.quizFirebaseHandler.setRandomFeedBack(randomFeedBack);
    }


    public void startUpdateQuestion(QuizItem quizItem, QuestionItem questionItem) {
        this.quizFirebaseHandler.startUpdateQuestion(quizItem, questionItem);
    }

    public void startSaveResults(ResultItem resultItem) {
        this.quizFirebaseHandler.startSaveResults(resultItem);
    }

    public void startGetQuizResults(String quizId) {
        this.quizFirebaseHandler.startGetQuizResults(quizId);
    }

    public MutableLiveData<List<ResultItem>> getResultsObservation() {
        return this.quizFirebaseHandler.getResultsObservation();
    }

    public void setResultsObservation(List<ResultItem> results) {
        this.quizFirebaseHandler.setResultsObservation(results);
    }


}
