package mohalim.app.quizapp.ui.quiz;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import mohalim.app.quizapp.R;
import mohalim.app.quizapp.core.di.base.BaseActivity;
import mohalim.app.quizapp.core.models.QuestionAnswerSavedItem;
import mohalim.app.quizapp.core.models.QuestionItem;
import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.utils.AppExecutor;
import mohalim.app.quizapp.core.utils.Constants;
import mohalim.app.quizapp.core.utils.Utils;
import mohalim.app.quizapp.core.utils.ViewModelProviderFactory;
import mohalim.app.quizapp.databinding.ActivityQuizBinding;
import mohalim.app.quizapp.ui.result.ResultActivity;


public class QuizActivity extends BaseActivity implements QuizFragment.ChangeQuizPosition, QuizFragment.ChangeNavItemColor {
    @Inject
    ViewModelProviderFactory viewModelProviderFactory;
    @Inject
    AppExecutor appExecutor;

    private QuizViewModel mViewModel;
    ActivityQuizBinding binding;

    public static final String TAG = "Quiz_app_tag";

    QuizPagerAdapter adapter;
    private boolean quizTimeInitiated;
    private boolean counterFinished;
    private CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz);

        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(Constants.QUIZ_ITEM)){
            finish();
        }

        final QuizItem quizItem = intent.getParcelableExtra(Constants.QUIZ_ITEM);
        mViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(QuizViewModel.class);
        mViewModel.initSession(quizItem);

        mViewModel.quizItem = quizItem;


        adapter = new QuizPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.setQuizItem(quizItem);
        binding.quizTitleTv.setText(quizItem.getQuizName());

        binding.quizViewPager.setAdapter(adapter);

        if (quizItem.getQuizSwipeDirection().equals(Constants.RIGHT)){
            binding.quizViewPager.setRotation(180.0f);
        }


        if (quizItem.getQuizNavigationDirection().equals(Constants.LEFT)){
            DrawerLayout.LayoutParams layoutParams = new DrawerLayout.LayoutParams(
                    (int) Utils.convertDpToPixel(130, this),
                    DrawerLayout.LayoutParams.MATCH_PARENT,
                    Gravity.LEFT
            );
            binding.navigation.setLayoutParams(layoutParams);

        }else if (quizItem.getQuizNavigationDirection().equals(Constants.RIGHT)){

            DrawerLayout.LayoutParams layoutParams = new DrawerLayout.LayoutParams(
                    (int) Utils.convertDpToPixel(130, this),
                    DrawerLayout.LayoutParams.MATCH_PARENT,
                    Gravity.RIGHT
            );
            binding.navigation.setLayoutParams(layoutParams);
        }

        binding.navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quizItem.getQuizNavigationDirection().equals(Constants.LEFT)){
                    binding.container.openDrawer(Gravity.LEFT);
                }else if (quizItem.getQuizNavigationDirection().equals(Constants.RIGHT)){
                    binding.container.openDrawer(Gravity.RIGHT);

                }

            }
        });


        /**
         * getQuestionFromInternal
         */

        mViewModel.getQuestionsFromInternalObserved(quizItem.getId()).observe(this, new Observer<List<QuestionItem>>() {
            @Override
            public void onChanged(List<QuestionItem> questionItems) {
                if (questionItems == null)return;
                if (questionItems.size() == 0){
                    Toast.makeText(QuizActivity.this, "Try again, or contact your instructor", Toast.LENGTH_SHORT).show();
                    mViewModel.resetQuiz(quizItem);
                    finish();
                }

                getSupportFragmentManager().getFragments().clear();

                binding.navViewContainer.removeAllViews();

                int i = 0;
                for (QuestionItem question: questionItems){
                    View view = LayoutInflater.from(QuizActivity.this).inflate(R.layout.row_quiz_navigation, binding.navViewContainer, false);

                    ((TextView) view.findViewById(R.id.title)).setText("Question"+ " "+ (i+1));

                    final int finalI = i;
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            binding.quizViewPager.setCurrentItem(finalI);
                            binding.container.closeDrawers();
                        }
                    });

                    binding.navViewContainer.addView(view);

                    i++;
                }

                if (mViewModel.questionItemList.size() == 0){
                    mViewModel.setQuestionItemList(questionItems);
                }

                if (mViewModel.currentSession == null){
                    finish();
                    return;
                }

                if (!quizTimeInitiated){
                    checkTime(quizItem);
                }

                adapter.setQuestionItems(questionItems);
                adapter.notifyDataSetChanged();




            }
        });


        

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (countDownTimer != null)
        countDownTimer.cancel();
    }

    @Override
    public void onChangeQuizPosition(int position) {
        binding.quizViewPager.setCurrentItem(position);
    }

    @Override
    public void onChangeNavItemColor(int currentPosition) {
        for (int i =0 ; i < binding.navViewContainer.getChildCount(); i++){
            if (i == currentPosition){
                binding.navViewContainer.getChildAt(i).setBackgroundResource(R.drawable.gredient2);
                ((TextView)binding.navViewContainer.getChildAt(i).findViewById(R.id.title)).setTextColor(Color.parseColor("#333333"));

            }else {
                binding.navViewContainer.getChildAt(i).setBackgroundResource(R.drawable.gredient1);
                ((TextView)binding.navViewContainer.getChildAt(i).findViewById(R.id.title)).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }
    }

    private void checkTime(final QuizItem quizItem){
        appExecutor.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                /*
                 * Quiz time
                 */
                Calendar calendar = Calendar.getInstance();

                long currentTimeMillisecond = calendar.getTimeInMillis();
                long timePassedMillisecond = currentTimeMillisecond - mViewModel.currentSession.getStartTime();
                long examTimeMillisecond = mViewModel.quizItem.getTimeInMinutes() * 60 * 1000;

                if (mViewModel.quizItem.getTimeInMinutes() == 0){
                    return;
                }

                // time finished
                if (timePassedMillisecond > examTimeMillisecond){
                    int i =0;
                    for (final QuestionItem questionItem : mViewModel.questionItemList){
                        QuestionAnswerSavedItem questionAnswerSavedItem = mViewModel.getSavedAnswer(mViewModel.quizItem.getId(), questionItem.getId());
                        if (questionAnswerSavedItem != null){
                            mViewModel.questionItemList.get(i).setChosenAnswer(questionAnswerSavedItem.getChosenAnswer());
                            mViewModel.questionItemList.get(i).setChosenAnswerCorrect(questionAnswerSavedItem.isChosenAnswerCorrect());
                        }
                    }


                    if (mViewModel.quizItem.isSaveResults()){
                        mViewModel.startSaveResults(mViewModel.questionItemList);
                    }

                    if (mViewModel.quizItem.isShowResults()){
                        Log.d(TAG, "run: reset session");
                        showResultsAndResetSession();
                        quizTimeInitiated = true;

                    }else{
                        mViewModel.resetQuiz(mViewModel.quizItem);
                        finish();
                    }

                    appExecutor.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(QuizActivity.this, "Exam time finished", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
                final long timeRemainsMillisecond = examTimeMillisecond - timePassedMillisecond;

                appExecutor.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {

                        countDownTimer = new CountDownTimer(timeRemainsMillisecond, 1000) {
                            @Override
                            public void onTick(long l) {
                                long h = TimeUnit.MILLISECONDS.toHours(l);
                                long m = TimeUnit.MILLISECONDS.toMinutes(l) - h*60;
                                long s = TimeUnit.MILLISECONDS.toSeconds(l) - h*60*60 - m*60;
                                String hour = String.valueOf(h);
                                String minute = String.valueOf(m);
                                String second = String.valueOf(s);
                                if (hour.length() == 1) hour = "0"+hour;
                                if (minute.length() == 1) minute = "0"+minute;
                                if (second.length() == 1) second = "0"+second;
                                mViewModel.quizItem.setRemainTime(hour+":"+minute+":"+second);
                                binding.timeCounterTv.setText(hour+":"+minute+":"+second);
                            }
                            @Override
                            public void onFinish() {
                                mViewModel.quizItem.setRemainTime("00:00:00");
                                binding.timeCounterTv.setText("00:00:00");

                                if (mViewModel.quizItem.isSaveResults()){
                                    mViewModel.startSaveResults(mViewModel.questionItemList);
                                }

                                if (mViewModel.quizItem.isShowResults()){
                                    if(!quizTimeInitiated && !counterFinished){
                                        showResultsAndResetSession();
                                        counterFinished = true;
                                    }
                                }else{
                                    mViewModel.resetQuiz(mViewModel.quizItem);
                                    finish();
                                }

                            }
                        }.start();
                    }
                });


            }
        });
    }

    private void showResultsAndResetSession() {
        Intent intent = new Intent(this, ResultActivity.class);
        ArrayList<QuestionItem> questions = new ArrayList<>(mViewModel.questionItemList);
        intent.putParcelableArrayListExtra(Constants.QUESTION_ITEM, questions);
        intent.putExtra(Constants.QUIZ_ITEM, mViewModel.quizItem);
        intent.putExtra(Constants.RESET_QUIZ, Constants.RESET_QUIZ);
        startActivity(intent);
        if (mViewModel.quizItem.isSaveResults()){
            mViewModel.startSaveResults(mViewModel.questionItemList);
        }
        finish();
    }
}
