package mohalim.app.quizapp.ui.admin_main;

import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
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
import mohalim.app.quizapp.databinding.ActivityAdminMainBinding;
import mohalim.app.quizapp.ui.dialog.DialogLoading;
import mohalim.app.quizapp.ui.dialog.DialogPeopleCanAccessQuiz;
import mohalim.app.quizapp.ui.quiz.QuizActivity;
import mohalim.app.quizapp.ui.statistics.StatisticsActivity;
import mohalim.app.quizapp.ui.user_main.UserMainActivity;


public class AdminMainActivity extends BaseActivity implements
        AdminMainFragment.MainFragmentClick,
        AddQuizBottomSheet.AddNewQuizListener,
        UpdateQuizBottomSheet.UpdateQuizListener,
        QuizPagedAdapter.QuizPagedAdapterClick,
        PeopleCanAccessAdapter.OnPeopleCanAccessAdapterClick,
        DialogPeopleCanAccessQuiz.OnDismissPeopleCanAccessDialog {

    private static final String TAG = "MainActivity";

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;
    AdminMainViewModel mViewModel;
    ActivityAdminMainBinding binding;

    DialogLoading dialogLoading;
    DialogPeopleCanAccessQuiz dialogPeopleCanAccessQuiz;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_main);
        mViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(AdminMainViewModel.class);

        Intent oldIntent = getIntent();
        if (oldIntent.hasExtra(Constants.USER_ITEM)){
            mViewModel.setCurrentUser((UserItem) oldIntent.getParcelableExtra(Constants.USER_ITEM));
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, AdminMainFragment.newInstance())
                    .commitNow();

        }

        dialogLoading = DialogLoading.newInstance();
        dialogPeopleCanAccessQuiz = DialogPeopleCanAccessQuiz.newInstance();

        /**
         * when Start initiate quiz to start it
         */
        mViewModel.getQuizInitiatingNow().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)return;
                if (mViewModel.initedQuiz == null)return;

                Intent intent = new Intent(AdminMainActivity.this, QuizActivity.class);
                intent.putExtra(Constants.QUIZ_ITEM, mViewModel.initedQuiz);
                startActivity(intent);

                if (dialogLoading.isAdded()){
                    dialogLoading.dismiss();
                }

                mViewModel.setQuizInitiatingNow(true);
            }
        });


        /**
         * Observe current user data
         */
        mViewModel.getCurrentUserDetailsObservation().observe(this, new Observer<UserItem>() {
            @Override
            public void onChanged(UserItem userItem) {
                if(userItem == null)return;
                if (!userItem.getIsAdmin()){
                    mViewModel.setCurrentUserDetailsObservation(null);
                    mViewModel.getCurrentUserDetailsObservation().removeObservers(AdminMainActivity.this);

                    Intent intent = new Intent(AdminMainActivity.this, UserMainActivity.class);
                    intent.putExtra(Constants.USER_ITEM, userItem);
                    startActivity(intent);
                    AdminMainActivity.this.finish();
                }
                mViewModel.setCurrentUser(userItem);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        initNavigation(binding.drawerLayout);
        if (dialogLoading.isAdded())dialogLoading.dismiss();

    }

    @Override
    public void onAddNewQuiz() {
        ((AdminMainFragment)getSupportFragmentManager().getFragments().get(0)).onAddNewQuiz();
    }

    @Override
    public void onQuizPagedAdapterClick(String clickType, QuizItem quizItem, int position) {
        switch (clickType) {
            case Constants.START:
                mViewModel.initSession(quizItem);

                if (!dialogLoading.isAdded()) {
                    dialogLoading.show(getSupportFragmentManager(), "LoadingDialog");
                }

                break;
            case Constants.EDIT:
                ((AdminMainFragment) getSupportFragmentManager().getFragments().get(0)).onQuizPagedAdapterClick(quizItem);
                break;
            case Constants.CLICK_TYPE_QUIZ_PEOPLE_ACCESS:
                dialogPeopleCanAccessQuiz.updateQuizPeopleCanAccess(quizItem);
                if (!dialogPeopleCanAccessQuiz.isAdded()) {
                    dialogPeopleCanAccessQuiz.show(getSupportFragmentManager(), "DialogPeopleCanAccessQuiz");
                }
                break;

            case Constants.CLICK_TYPE_QUIZ_Statistics:
                Intent intent = new Intent(AdminMainActivity.this, StatisticsActivity.class);
                intent.putExtra(Constants.QUIZ_ITEM, quizItem);
                startActivity(intent);
                break;
        }

    }

    @Override
    public void onUpdateQuiz() {
        ((AdminMainFragment)getSupportFragmentManager().getFragments().get(0)).onUpdateQuiz();
    }

    @Override
    public void onPeopleCanAccessAdapterClick(QuizItem quizItem) {
        mViewModel.startUpdateQuiz(quizItem);
    }

    @Override
    public void onDismissPeopleCanAccessDialog() {
        mViewModel.refresh();
    }

    @Override
    public void onMainFragmentClick(int id) {
        if (id == R.id.showNavIcon){
            binding.drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mViewModel.setCurrentUserDetailsObservation(null);
        mViewModel.getCurrentUserDetailsObservation().removeObservers(this);
    }
}
