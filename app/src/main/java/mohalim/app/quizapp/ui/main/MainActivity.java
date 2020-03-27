package mohalim.app.quizapp.ui.main;

import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import javax.inject.Inject;

import mohalim.app.quizapp.R;
import mohalim.app.quizapp.core.di.base.BaseActivity;
import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.utils.Constants;
import mohalim.app.quizapp.core.utils.ViewModelProviderFactory;
import mohalim.app.quizapp.databinding.ActivityMainBinding;
import mohalim.app.quizapp.ui.dialog.DialogLoading;
import mohalim.app.quizapp.ui.dialog.DialogPeopleCanAccessQuiz;
import mohalim.app.quizapp.ui.quiz.QuizActivity;
import mohalim.app.quizapp.ui.statistics.StatisticsActivity;


public class MainActivity extends BaseActivity implements
        MainFragment.MainFragmentClick,
        AddQuizBottomSheet.AddNewQuizListener,
        UpdateQuizBottomSheet.UpdateQuizListener,
        QuizPagedAdapter.QuizPagedAdapterClick,
        PeopleCanAccessAdapter.OnPeopleCanAccessAdapterClick,
        DialogPeopleCanAccessQuiz.OnDismissPeopleCanAccessDialog {

    private static final String TAG = "MainActivity";

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;
    MainViewModel mViewModel;
    ActivityMainBinding binding;

    DialogLoading dialogLoading;
    DialogPeopleCanAccessQuiz dialogPeopleCanAccessQuiz;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(MainViewModel.class);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();

        }

        dialogLoading = DialogLoading.newInstance();
        dialogPeopleCanAccessQuiz = DialogPeopleCanAccessQuiz.newInstance();


        mViewModel.getQuizInitiatingNow().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)return;
                if (mViewModel.initedQuiz == null)return;

                Intent intent = new Intent(MainActivity.this, QuizActivity.class);
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
        if (dialogLoading.isAdded())dialogLoading.dismiss();

    }

    @Override
    public void onAddNewQuiz() {
        ((MainFragment)getSupportFragmentManager().getFragments().get(0)).onAddNewQuiz();
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
                ((MainFragment) getSupportFragmentManager().getFragments().get(0)).onQuizPagedAdapterClick(quizItem);
                break;
            case Constants.CLICK_TYPE_QUIZ_PEOPLE_ACCESS:
                dialogPeopleCanAccessQuiz.updateQuizPeopleCanAccess(quizItem);
                if (!dialogPeopleCanAccessQuiz.isAdded()) {
                    dialogPeopleCanAccessQuiz.show(getSupportFragmentManager(), "DialogPeopleCanAccessQuiz");
                }
                break;

            case Constants.CLICK_TYPE_QUIZ_Statistics:
                Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
                intent.putExtra(Constants.QUIZ_ITEM, quizItem);
                startActivity(intent);
                break;
        }

    }

    @Override
    public void onUpdateQuiz() {
        ((MainFragment)getSupportFragmentManager().getFragments().get(0)).onUpdateQuiz();
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
}
