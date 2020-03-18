package mohalim.app.quizapp.core.firebase;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import mohalim.app.quizapp.core.database.AnswerDao;
import mohalim.app.quizapp.core.database.AppDatabase;
import mohalim.app.quizapp.core.database.QuestionDao;
import mohalim.app.quizapp.core.models.AnswerItem;
import mohalim.app.quizapp.core.models.QuestionItem;
import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.models.SessionItem;
import mohalim.app.quizapp.core.models.UserItem;
import mohalim.app.quizapp.core.services.AppService;
import mohalim.app.quizapp.core.utils.AppExecutor;
import mohalim.app.quizapp.core.utils.Constants;


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
        db.collection("quiz").document(quizItem.getId()).set(quizItem);
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
                        userItem.setEmail(mAuth.getCurrentUser().getEmail());
                        userItem.setDisplayedName(mAuth.getCurrentUser().getDisplayName());
                        userItem.setPhoneNumber(mAuth.getCurrentUser().getPhoneNumber());

                        db.collection("user").document(mAuth.getUid()).set(userItem);

                    }
                });
    }
}


