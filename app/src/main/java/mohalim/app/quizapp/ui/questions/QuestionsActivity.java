package mohalim.app.quizapp.ui.questions;

import android.os.Bundle;

import mohalim.app.quizapp.R;
import mohalim.app.quizapp.core.di.base.BaseActivity;
import mohalim.app.quizapp.ui.main.MainFragment;
import mohalim.app.quizapp.ui.quiz.QuizFragment;


public class QuestionsActivity extends BaseActivity implements AddQuestionBottomSheert.OnAddNewQuestion {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, QuestionsFragment.newInstance())
                    .commitNow();
        }
    }

    @Override
    public void onAddNewQuestion() {
        ((QuestionsFragment)getSupportFragmentManager().getFragments().get(0)).onAddNewQuestion();
    }
}
