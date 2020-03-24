package mohalim.app.quizapp.ui.statistics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import javax.inject.Inject;

import mohalim.app.quizapp.R;
import mohalim.app.quizapp.core.di.base.BaseActivity;
import mohalim.app.quizapp.core.utils.Constants;
import mohalim.app.quizapp.core.utils.ViewModelProviderFactory;
import mohalim.app.quizapp.databinding.ActivityStatisticsBinding;


public class StatisticsActivity extends BaseActivity {
    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    private ActivityStatisticsBinding binding;
    private StatisticsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_statistics);
        mViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(StatisticsViewModel.class);

        Intent oldIntent = getIntent();
        if (!oldIntent.hasExtra(Constants.QUIZ_ITEM))finish();
        mViewModel.quizItem = oldIntent.getParcelableExtra(Constants.QUIZ_ITEM);

        Toast.makeText(this, ""+ mViewModel.quizItem.getQuizName(), Toast.LENGTH_SHORT).show();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, StatisticsFragment.newInstance())
                    .commitNow();
        }
    }
}
