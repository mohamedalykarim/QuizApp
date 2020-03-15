package mohalim.app.quizapp.core.services;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.DaggerIntentService;
import mohalim.app.quizapp.core.firebase.QuizFirebaseHandler;
import mohalim.app.quizapp.core.models.AnswerItem;
import mohalim.app.quizapp.core.models.QuestionItem;
import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.models.SessionItem;
import mohalim.app.quizapp.core.utils.Constants;

public class AppService extends DaggerIntentService {
    @Inject
    QuizFirebaseHandler quizFirebaseHandler;


    public AppService() {
        super("FirebaseQuiz");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (!intent.hasExtra(Constants.TYPE)) return;

        if (intent.getStringExtra(Constants.TYPE).equals(Constants.TYPE_ADD_QUIZ)){
            QuizItem quizItem = intent.getParcelableExtra(Constants.QUIZ_ITEM);
            quizFirebaseHandler.addQuiz(quizItem);
        }

        if (intent.getStringExtra(Constants.TYPE).equals(Constants.TYPE_UPDATE_QUIZ)){
            QuizItem quizItem = intent.getParcelableExtra(Constants.QUIZ_ITEM);
            quizFirebaseHandler.updateQuiz(quizItem);
        }

        if (intent.getStringExtra(Constants.TYPE).equals(Constants.TYPE_ADD_QUESTION)){
            QuizItem quizItem = intent.getParcelableExtra(Constants.QUIZ_ITEM);
            QuestionItem questionItem = intent.getParcelableExtra(Constants.QUESTION_ITEM);
            quizFirebaseHandler.addQuestion(quizItem, questionItem);
        }

        if (intent.getStringExtra(Constants.TYPE).equals(Constants.TYPE_START_CHOOSE_QUESTION_TO_SESSION)){
            QuizItem quizItem = intent.getParcelableExtra(Constants.QUIZ_ITEM);
            SessionItem sessionItem = intent.getParcelableExtra(Constants.SESSION_ITEM);
            quizFirebaseHandler.chooseQuestionToSession(quizItem, sessionItem);
        }

        if (intent.getStringExtra(Constants.TYPE).equals(Constants.TYPE_ACCESS_QUIZ)){
            String quizId = intent.getStringExtra(Constants.QUIZ_ID);
            quizFirebaseHandler.accessQuiz(quizId);
        }



    }
}
