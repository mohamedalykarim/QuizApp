package mohalim.app.quizapp.core.firebase;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import mohalim.app.quizapp.core.database.AnswerDao;
import mohalim.app.quizapp.core.database.AppDatabase;
import mohalim.app.quizapp.core.database.QuestionDao;
import mohalim.app.quizapp.core.models.AnswerItem;
import mohalim.app.quizapp.core.models.FeedBackItem;
import mohalim.app.quizapp.core.models.QuestionItem;
import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.models.SessionItem;
import mohalim.app.quizapp.core.models.UserItem;
import mohalim.app.quizapp.core.services.AppService;
import mohalim.app.quizapp.core.utils.AppExecutor;
import mohalim.app.quizapp.core.utils.Constants;
import mohalim.app.quizapp.core.utils.Utils;


public class QuizFirebaseHandler {
    private static final String TAG = "QuizFirebaseHandler";
    private final Application application;
    private final AppExecutor appExecutor;
    FirebaseFirestore db;
    QuestionDao questionDao;
    AnswerDao answerDao;

    FirebaseAuth mAuth;

    MutableLiveData<List<QuizItem>> quizItemList;
    MutableLiveData<Boolean> quizInitiatingNow;
    MutableLiveData<QuizItem> accessedQuiz;
    MutableLiveData<QuizItem> quizitemObservation;
    MutableLiveData<List<UserItem>> usersSearchObservation;
    MutableLiveData<FeedBackItem> myFeedBackObservation;
    MutableLiveData<List<FeedBackItem>> randomFeedBack;

    @Inject
    public QuizFirebaseHandler(Application application, AppExecutor appExecutor, FirebaseAuth mAuth) {
        this.application = application;
        this.appExecutor = appExecutor;
        this.mAuth = mAuth;
         db = FirebaseFirestore.getInstance();
        AppDatabase appDatabase = AppDatabase.getDatabase(application);
        questionDao = appDatabase.questionDao();
        answerDao = appDatabase.answerDao();

        quizItemList = new MutableLiveData<>();
        quizInitiatingNow = new MutableLiveData<>(true);
        accessedQuiz = new MutableLiveData<>();
        usersSearchObservation = new MutableLiveData<>();
        quizitemObservation = new MutableLiveData<>();
        myFeedBackObservation = new MutableLiveData<>();
        randomFeedBack = new MutableLiveData<>();
    }


    public void startAddQuiz(QuizItem quizItem) {
        Intent intent = new Intent(application, AppService.class);
        intent.putExtra(Constants.TYPE, Constants.TYPE_ADD_QUIZ);
        intent.putExtra(Constants.QUIZ_ITEM, quizItem);
        application.startService(intent);
    }


    public void addQuiz(QuizItem quizItem){
        DocumentReference dr = db.collection("quiz").document();
        quizItem.setId(dr.getId());
        db.collection("quiz").document(dr.getId()).set(quizItem);

    }

    public void startGetQuiz(String quizId) {
        Intent intent = new Intent(application, AppService.class);
        intent.putExtra(Constants.TYPE, Constants.TYPE_GET_QUIZ);
        intent.putExtra(Constants.QUIZ_ID, quizId);
        application.startService(intent);
    }

    public void getQuiz(String quizId){
        if (quizId == null)return;

        db.collection("quiz")
                .document(quizId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (!documentSnapshot.exists())return;

                        quizitemObservation.setValue(documentSnapshot.toObject(QuizItem.class));
                    }
                });
    }

    public MutableLiveData<QuizItem> getQuizitemObservation() {
        return quizitemObservation;
    }

    public void setQuizitemObservation(QuizItem quizitemObservation) {
        this.quizitemObservation.setValue(quizitemObservation);
    }

    public void startInsertQuestion(QuizItem quizItem, QuestionItem questionItem) {
        Intent intent = new Intent(application, AppService.class);
        intent.putExtra(Constants.TYPE, Constants.TYPE_ADD_QUESTION);
        intent.putExtra(Constants.QUIZ_ITEM, quizItem);
        intent.putExtra(Constants.QUESTION_ITEM, questionItem);
        application.startService(intent);
    }

    public void addQuestion(QuizItem quizItem, QuestionItem questionItem) {
        DocumentReference quesitonDocument = db.collection("quiz").document(quizItem.getId())
                .collection("question").document();

        questionItem.setId(quesitonDocument.getId());
        quesitonDocument.set(questionItem);
    }

    public void updateQuestion(QuizItem quizItem, QuestionItem questionItem) {
        if (quizItem == null || questionItem == null)return;

        DocumentReference quesitonDocument = db.collection("quiz").document(quizItem.getId())
                .collection("question").document(questionItem.getId());

        quesitonDocument.set(questionItem);
    }

    public void startUpdateQuestion(QuizItem quizItem, QuestionItem questionItem) {
        Intent intent = new Intent(application, AppService.class);
        intent.putExtra(Constants.TYPE, Constants.TYPE_START_UPDATE_QUESTION);
        intent.putExtra(Constants.QUIZ_ITEM, quizItem);
        intent.putExtra(Constants.QUESTION_ITEM, questionItem);
        application.startService(intent);

    }

    public void startChooseQuestionToSession(QuizItem quizItem, SessionItem currentSession) {
        Intent intent = new Intent(application, AppService.class);
        intent.putExtra(Constants.TYPE, Constants.TYPE_START_CHOOSE_QUESTION_TO_SESSION);
        intent.putExtra(Constants.QUIZ_ITEM, quizItem);
        intent.putExtra(Constants.SESSION_ITEM, currentSession);
        application.startService(intent);

    }

    public void chooseQuestionToSession(final QuizItem quizItem, SessionItem sessionItem) {
        quizInitiatingNow.postValue(true);
        db.collection("quiz")
                .document(quizItem.getId())
                .collection("question")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty())return;
                        final List<QuestionItem> questions = new ArrayList<>();

                        for (QueryDocumentSnapshot question : queryDocumentSnapshots){
                            questions.add(question.toObject(QuestionItem.class));
                        }

                        Collections.shuffle(questions);
                        for (int i= 0; i < questions.size(); i++) {
                            if (i >= quizItem.getQuestionCountWanted()) continue;
                            final QuestionItem questionItem = questions.get(i);
                            appExecutor.diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    questionItem.setQuizId(quizItem.getId());
                                    questionDao.insert(questionItem);
                                }
                            });


                            int answerindex = 0;
                            for (final AnswerItem answerItem : questionItem.getQuestionAnswers()){
                                answerItem.setQuestionId(questionItem.getId());
                                answerItem.setQuizId(quizItem.getId());
                                final int finalI = i;
                                final int finalAnswerindex = answerindex;
                                appExecutor.diskIO().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        answerDao.insert(answerItem);
                                        if (finalI == questions.size()-1 && finalAnswerindex == questionItem.getQuestionAnswers().size()-1)
                                            quizInitiatingNow.postValue(false);
                                    }
                                });
                                answerindex++;
                            }


                        }



                    }
                });


    }

    public MutableLiveData<Boolean> getQuizInitiatingNow() {
        return quizInitiatingNow;
    }

    public void setQuizInitiatingNow(boolean initiatingNow) {
        this.quizInitiatingNow.postValue(initiatingNow);
    }

    public void startUpdateQuiz(QuizItem quizItem) {
        Intent intent = new Intent(application, AppService.class);
        intent.putExtra(Constants.TYPE, Constants.TYPE_UPDATE_QUIZ);
        intent.putExtra(Constants.QUIZ_ITEM, quizItem);
        application.startService(intent);

    }

    public void updateQuiz(QuizItem quizItem) {
        if (quizItem == null)return;

        if (null == quizItem.getPeopleCanAccess()){
            db.collection("quiz").document(quizItem.getId()).set(quizItem);
        }else {
            if (quizItem.getPeopleCanAccess().size() == 0){
                quizItem.setPeopleCanAccess(null);
            }
            db.collection("quiz").document(quizItem.getId()).set(quizItem);
        }
    }

    public void startAccessQuiz(String quizId) {
        Intent intent = new Intent(application, AppService.class);
        intent.putExtra(Constants.TYPE, Constants.TYPE_ACCESS_QUIZ);
        intent.putExtra(Constants.QUIZ_ID, quizId);
        application.startService(intent);


    }

    public void accessQuiz(String quizId) {
        Log.d(TAG, "accessQuiz: "+mAuth.getUid());
        db.collection("quiz")
                .document(quizId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (!documentSnapshot.exists()){

                        }else {
                            QuizItem quizItem = documentSnapshot.toObject(QuizItem.class);
                            accessedQuiz.postValue(quizItem);
                        }
                    }
                });
    }

    public MutableLiveData<QuizItem> getAccessedQuiz() {
        return accessedQuiz;
    }

    public void setAccessedQuiz(QuizItem quiz) {
        this.accessedQuiz.postValue(quiz);
    }


     /***************************************************************************/
     /**                            User Data                                  **/
     /***************************************************************************/

    public void startSaveUserData() {
        Intent intent = new Intent(application, AppService.class);
        intent.putExtra(Constants.TYPE, Constants.TYPE_START_SAVE_USER_DATA);
        application.startService(intent);
    }

    public void saveUserData() {
        if (mAuth.getUid() == null)return;
        db.collection("user").document(mAuth.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists())return;
                        UserItem userItem = new UserItem();
                        userItem.setId(mAuth.getCurrentUser().getUid());
                        userItem.setDisplayedName(mAuth.getCurrentUser().getDisplayName());
                        userItem.setUserName(mAuth.getCurrentUser().getEmail().replace("@gmail.com", ""));

                        db.collection("user").document(mAuth.getUid()).set(userItem);

                    }
                });
    }

    public void startGetPersonByUsername(String username) {
        Intent intent = new Intent(application, AppService.class);
        intent.putExtra(Constants.TYPE, Constants.TYPE_START_GET_USER_BY_USERNAME);
        intent.putExtra(Constants.USERNAME, username);
        application.startService(intent);
    }

    public void getPersonByUsername(String username) {
        if (username.length() < 1)return;
        db.collection("user")
                .orderBy("userName")
                .startAt(username.trim())
                .endAt(username.trim()+"\uf8ff")
                .limit(3)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty())return;
                        List<UserItem> users = new ArrayList<>();


                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            UserItem user = documentSnapshot.toObject(UserItem.class);
                            users.add(user);
                        }

                        usersSearchObservation.setValue(users);
                    }
                });
    }

    public MutableLiveData<List<UserItem>> getUsersSearchObservation() {
        return usersSearchObservation;
    }

    public void setUsersSearchObservation(List<UserItem> userItems) {
        this.usersSearchObservation.postValue(userItems);
    }

    public void startAddUserAccessToQuiz(QuizItem quizItem, String userName) {
        Intent intent = new Intent(application, AppService.class);
        intent.putExtra(Constants.TYPE, Constants.TYPE_START_ADD_USER_TO_QUIZ);
        intent.putExtra(Constants.USERNAME, userName);
        intent.putExtra(Constants.QUIZ_ITEM, quizItem);
        application.startService(intent);

    }

    public void addUserAccessToQuiz(final QuizItem quizItem, final String username) {
        if (quizItem == null)return;
        if (username == null)return;

        db.collection("user")
                .whereEqualTo("userName", username)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty())return;
                        final UserItem userItem = queryDocumentSnapshots.getDocuments().get(0).toObject(UserItem.class);


                        db.collection("quiz")
                                .document(quizItem.getId())
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (!documentSnapshot.exists()) return;

                                        QuizItem newQuiz = documentSnapshot.toObject(QuizItem.class);

                                        if (newQuiz.getPeopleCanAccess() == null){
                                            Log.d(TAG, "onSuccess: null");
                                            Log.d(TAG, "onSuccess: Add first user to quiz");
                                            addUserAccessToQuizFinishing(newQuiz, userItem);
                                        }else{
                                            if (Utils.isQuizListPeopleCanAccessContainsId(newQuiz.getPeopleCanAccess(), userItem.getId())){
                                                Log.d(TAG, "onSuccess: user exists");
                                            }else {
                                                Log.d(TAG, "onSuccess: add new user to quiz");
                                                addUserAccessToQuizFinishing(newQuiz, userItem);
                                            }
                                        }

//


                                    }
                                });


                    }
                });
    }

    private void addUserAccessToQuizFinishing(QuizItem quizItem, UserItem userItem) {
        if (userItem == null)return;

        if (quizItem.getPeopleCanAccess() == null){
            quizItem.setPeopleCanAccess(new ArrayList<UserItem>());
            quizItem.getPeopleCanAccess().add(userItem);

        }else {
            quizItem.getPeopleCanAccess().add(userItem);
        }

        Log.d(TAG, "addUserAccessToQuizFinishing: "+quizItem.getPeopleCanAccess().size());

        db.collection("quiz")
                .document(quizItem.getId())
                .set(quizItem);

    }


    /***************************************************************************/
    /**                             Feedback                                  **/
    /***************************************************************************/

    public void startAddFeedBack(FeedBackItem feedBackItem) {
        Intent intent = new Intent(application, AppService.class);
        intent.putExtra(Constants.TYPE, Constants.TYPE_START_ADD_FEEDBACK);
        intent.putExtra(Constants.FEEDBACK_ITEM, feedBackItem);
        application.startService(intent);
    }

    public void addFeedBack(final FeedBackItem feedBackItem) {
        if (feedBackItem == null)return;
        if (mAuth.getCurrentUser() == null)return;

        db.collection("feedback")
                .whereEqualTo("userId", mAuth.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty())return;

                        DocumentReference document = db.collection("feedback").document();
                        feedBackItem.setId(document.getId());
                        feedBackItem.setRandom(String.valueOf(Utils.randInt(10000, 2147483647)));

                        document.set(feedBackItem);

                    }
                });
    }

    public void startGetMyFeedBack() {
        Intent intent = new Intent(application, AppService.class);
        intent.putExtra(Constants.TYPE, Constants.TYPE_START_GET_MY_FEEDBACK);
        application.startService(intent);

    }

    public void getMyFeedBack() {
        if (mAuth.getCurrentUser() == null)return;
        db.collection("feedback")
                .whereEqualTo("userId", mAuth.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty())return;
                        FeedBackItem feedBackItem = queryDocumentSnapshots.getDocuments().get(0).toObject(FeedBackItem.class);
                        myFeedBackObservation.setValue(feedBackItem);
                    }
                });
    }

    public MutableLiveData<FeedBackItem> getMyFeedBackObservation() {
        return myFeedBackObservation;
    }

    public void setMyFeedBackObservation(FeedBackItem feedBack) {
        this.myFeedBackObservation.setValue(feedBack);
    }

    public void startGetRandomFeedback(int count) {
        Intent intent = new Intent(application, AppService.class);
        intent.putExtra(Constants.TYPE, Constants.TYPE_START_GET_RANDOM_FEEDBACK);
        intent.putExtra(Constants.COUNT, count);
        application.startService(intent);

    }

    public void getRandomFeedBack(int count) {
        if (count == 0) return;

        int randomNumber = Utils.randInt(1,9);
        Log.d(TAG, "getRandomFeedBack: "+ randomNumber);

        db.collection("feedback")
                .orderBy("random")
                .startAt(randomNumber)
                .endAt(randomNumber+ "\uf8ff")
                .limit(count)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty())return;
                        List<FeedBackItem> feedBackItems = new ArrayList<>();

                        for (DocumentSnapshot feedbackSnapshot : queryDocumentSnapshots){
                            feedBackItems.add(feedbackSnapshot.toObject(FeedBackItem.class));
                        }

                        randomFeedBack.setValue(feedBackItems);
                    }
                });
    }

    public MutableLiveData<List<FeedBackItem>> getRandomFeedBack() {
        return randomFeedBack;
    }

    public void setRandomFeedBack(List<FeedBackItem> randomFeedBack) {
        this.randomFeedBack.setValue(randomFeedBack);
    }


}


