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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import mohalim.app.quizapp.R;
import mohalim.app.quizapp.core.di.base.BaseActivity;
import mohalim.app.quizapp.core.models.QuestionItem;
import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.utils.AppExecutor;
import mohalim.app.quizapp.core.utils.Constants;
import mohalim.app.quizapp.core.utils.ViewModelProviderFactory;
import mohalim.app.quizapp.databinding.ActivityQuizBinding;


public class QuizActivity extends BaseActivity implements QuizFragment.ChangeQuizPosition, QuizFragment.ChangeNavItemColor {
    @Inject
    ViewModelProviderFactory viewModelProviderFactory;
    @Inject
    AppExecutor appExecutor;

    private QuizViewModel mViewModel;
    ActivityQuizBinding binding;

    public static final String TAG = "Quiz_app_tag";

    QuizPagerAdapter adapter;



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



        adapter = new QuizPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.setQuizItem(quizItem);

        binding.quizViewPager.setAdapter(adapter);

        if (quizItem.getQuizSwipeDirection().equals(Constants.RIGHT)){
            binding.quizViewPager.setRotation(180.0f);
        }


        if (quizItem.getQuizNavigationDirection().equals(Constants.LEFT)){
            DrawerLayout.LayoutParams layoutParams = new DrawerLayout.LayoutParams(
                    DrawerLayout.LayoutParams.MATCH_PARENT,
                    DrawerLayout.LayoutParams.MATCH_PARENT,
                    Gravity.LEFT
            );
            binding.navigation.setLayoutParams(layoutParams);

        }else if (quizItem.getQuizNavigationDirection().equals(Constants.RIGHT)){

            DrawerLayout.LayoutParams layoutParams = new DrawerLayout.LayoutParams(
                    DrawerLayout.LayoutParams.MATCH_PARENT,
                    DrawerLayout.LayoutParams.MATCH_PARENT,
                    Gravity.RIGHT
            );
            binding.navigation.setLayoutParams(layoutParams);
        }


        /**
         * getQuestionFromInternal
         */

        mViewModel.getQuestionsFromInternalObserved(quizItem.getId()).observe(this, new Observer<List<QuestionItem>>() {
            @Override
            public void onChanged(List<QuestionItem> questionItems) {
                if (questionItems == null)return;

                getSupportFragmentManager().getFragments().clear();

                binding.navViewContainer.removeAllViews();

                int i = 0;
                for (QuestionItem question: questionItems){
                    View view = LayoutInflater.from(QuizActivity.this).inflate(R.layout.row_quiz_navigation, binding.navViewContainer, false);

                    ((TextView) view.findViewById(R.id.title)).setText("Question Number "+ (i+1));

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

                adapter.setQuestionItems(questionItems);
                adapter.notifyDataSetChanged();
            }
        });
        

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

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
}
