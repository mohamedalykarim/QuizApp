package mohalim.app.quizapp.ui.questions;

import android.os.Bundle;

import mohalim.app.quizapp.R;
import mohalim.app.quizapp.core.di.base.BaseActivity;


public class QuestionsActivity extends BaseActivity {

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
}
