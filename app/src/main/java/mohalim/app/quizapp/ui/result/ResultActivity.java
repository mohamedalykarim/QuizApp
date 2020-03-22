package mohalim.app.quizapp.ui.result;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import javax.inject.Inject;

import mohalim.app.quizapp.R;
import mohalim.app.quizapp.core.di.base.BaseActivity;
import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.utils.Constants;
import mohalim.app.quizapp.core.utils.ViewModelProviderFactory;


public class ResultActivity extends BaseActivity {
    @Inject
    ViewModelProviderFactory viewModelProviderFactory;
    ResultViewModel mViewModel;
    Intent oldIntent;
    QuizItem quizItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        mViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(ResultViewModel.class);

        oldIntent = getIntent();

        quizItem = oldIntent.getParcelableExtra(Constants.QUIZ_ITEM);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ResultFragment.newInstance())
                    .commitNow();
        }
    }


}
