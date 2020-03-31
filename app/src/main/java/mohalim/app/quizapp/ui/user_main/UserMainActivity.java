package mohalim.app.quizapp.ui.user_main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import javax.inject.Inject;

import mohalim.app.quizapp.R;
import mohalim.app.quizapp.core.di.base.BaseActivity;
import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.models.UserItem;
import mohalim.app.quizapp.core.utils.Constants;
import mohalim.app.quizapp.core.utils.ViewModelProviderFactory;
import mohalim.app.quizapp.databinding.ActivityUserMainBinding;
import mohalim.app.quizapp.ui.admin_main.AdminMainActivity;
import mohalim.app.quizapp.ui.admin_main.AdminMainFragment;
import mohalim.app.quizapp.ui.dialog.DialogLoading;
import mohalim.app.quizapp.ui.quiz.QuizActivity;
import mohalim.app.quizapp.ui.statistics.StatisticsActivity;


public class UserMainActivity extends BaseActivity
implements UserMainFragment.UserMainFragmetnClickListener,
        UserQuizPagedAdapter.UserQuizPagedAdapterClick {
    @Inject
    ViewModelProviderFactory viewmodelProviderFactory;
    private ActivityUserMainBinding binding;
    private UserMainViewModel mViewModel;

    DialogLoading dialogLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_main);
        mViewModel = new ViewModelProvider(this, viewmodelProviderFactory).get(UserMainViewModel.class);

        Intent oldIntent = getIntent();
        if (oldIntent.hasExtra(Constants.USER_ITEM)){
            mViewModel.setCurrentUser((UserItem) oldIntent.getParcelableExtra(Constants.USER_ITEM));
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, UserMainFragment.newInstance())
                    .commitNow();
        }

        dialogLoading = DialogLoading.newInstance();

        /**
         * when Start initiate quiz to start it
         */
        mViewModel.getQuizInitiatingNow().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)return;
                if (mViewModel.initedQuiz == null)return;

                Intent intent = new Intent(UserMainActivity.this, QuizActivity.class);
                intent.putExtra(Constants.QUIZ_ITEM, mViewModel.initedQuiz);
                startActivity(intent);

                if (dialogLoading.isAdded()){
                    dialogLoading.dismiss();
                }

                mViewModel.setQuizInitiatingNow(true);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        initNavigation(binding.drawerLayout);
    }

    @Override
    public void onUserMainFragmetnClickListenerClicked(int viewId) {
        if (viewId == R.id.showNavIcon){
            binding.drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onUserQuizPagedAdapterClick(String clickType, QuizItem quizItem, int position) {
        switch (clickType) {
            case Constants.START:
                mViewModel.initSession(quizItem);

                if (!dialogLoading.isAdded()) {
                    dialogLoading.show(getSupportFragmentManager(), "LoadingDialog");
                }

                break;
        }
    }
}
