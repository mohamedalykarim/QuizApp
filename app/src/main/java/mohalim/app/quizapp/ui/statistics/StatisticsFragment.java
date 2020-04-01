package mohalim.app.quizapp.ui.statistics;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import mohalim.app.quizapp.R;
import mohalim.app.quizapp.core.comparator.ResultsComparator;
import mohalim.app.quizapp.core.comparator.StudentsResultComparator;
import mohalim.app.quizapp.core.di.base.BaseFragment;
import mohalim.app.quizapp.core.models.ResultItem;
import mohalim.app.quizapp.core.models.ResultQuestionItem;
import mohalim.app.quizapp.core.models.StatisitcsQuestionItem;
import mohalim.app.quizapp.core.models.StatisticsStudentsArrangeItem;
import mohalim.app.quizapp.core.models.UserItem;
import mohalim.app.quizapp.core.utils.CircleProgressBar;
import mohalim.app.quizapp.core.utils.ViewModelProviderFactory;
import mohalim.app.quizapp.databinding.FragmentStatisticsBinding;


public class StatisticsFragment extends BaseFragment {
    private static final String TAG = StatisticsFragment.class.getName();
    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    private StatisticsViewModel mViewModel;
    private FragmentStatisticsBinding binding;

    DialogStudentResults dialogStudentResults;

    public static StatisticsFragment newInstance() {
        return new StatisticsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentStatisticsBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(StatisticsViewModel.class);
        binding.setViewmodel(mViewModel);

        dialogStudentResults = new DialogStudentResults();

        binding.refresher.setRefreshing(true);
        mViewModel.startGetQuizResults(mViewModel.quizItem.getId());

        if (Locale.getDefault().getLanguage().equals("ar")){
            binding.nameTv.setText("احصائيات امتحان" + " " + mViewModel.quizItem.getQuizName());
        }else {
            binding.nameTv.setText(mViewModel.quizItem.getQuizName() + " " + "Statistics");
        }

        binding.refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mViewModel.startGetQuizResults(mViewModel.quizItem.getId());
            }
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    binding.statistics.setVisibility(View.VISIBLE);
                    binding.students.setVisibility(View.GONE);
                    binding.questions.setVisibility(View.GONE);
                    binding.oldResultsContainer.setVisibility(View.GONE);
                }else if (tab.getPosition() == 1){
                    binding.statistics.setVisibility(View.GONE);
                    binding.students.setVisibility(View.VISIBLE);
                    binding.questions.setVisibility(View.GONE);
                    binding.oldResultsContainer.setVisibility(View.GONE);

                }else if (tab.getPosition() == 2){
                    binding.statistics.setVisibility(View.GONE);
                    binding.students.setVisibility(View.GONE);
                    binding.questions.setVisibility(View.VISIBLE);
                    binding.oldResultsContainer.setVisibility(View.GONE);
                }else if (tab.getPosition() == 3){
                    binding.statistics.setVisibility(View.GONE);
                    binding.students.setVisibility(View.GONE);
                    binding.questions.setVisibility(View.GONE);
                    binding.oldResultsContainer.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.studentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.statistics.setVisibility(View.GONE);
                binding.students.setVisibility(View.VISIBLE);
                binding.questions.setVisibility(View.GONE);

                binding.tabLayout.getTabAt(1).select();
            }
        });

        binding.questionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.statistics.setVisibility(View.GONE);
                binding.students.setVisibility(View.GONE);
                binding.questions.setVisibility(View.VISIBLE);

                binding.tabLayout.getTabAt(2).select();
            }
        });







        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel.getResultsObservation().observe(getViewLifecycleOwner(), new Observer<List<ResultItem>>() {
            @Override
            public void onChanged(List<ResultItem> resultItems) {
                if (resultItems == null)return;
                mViewModel.results = resultItems;

                updateUI();

            }
        });
    }

    private void updateUI() {

        /**
         * results tab
         */
        binding.oldResultsContainer.removeAllViews();
        Collections.sort(mViewModel.results, new ResultsComparator());
        for (final ResultItem resultItem: mViewModel.results){
            View view = getLayoutInflater().inflate(R.layout.row_people_can_access_quiz, binding.students, false);
            view.findViewById(R.id.removeImg).setVisibility(View.GONE);
            TextView usernameTv = view.findViewById(R.id.userNameTv);
            TextView gradeTv = view.findViewById(R.id.gradeTv);
            TextView finishTv = view.findViewById(R.id.finishTv);
            TextView waitingTv = view.findViewById(R.id.waitingTv);

            usernameTv.setText(resultItem.getUsername());
            int gradeInt = (int) resultItem.getResultScore();
            gradeTv.setText(gradeInt + "");

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogStudentResults.setResultItem(null);
                    dialogStudentResults.setStudentId(resultItem.getUserId());
                    if (!dialogStudentResults.isAdded()){
                        dialogStudentResults.show(getActivity().getSupportFragmentManager(), "DialogStudentResults");
                    }
                }
            });

            binding.oldResultsContainer.addView(view);
        }


        if (mViewModel.quizItem.getPeopleCanAccess() != null){
            final List<StatisticsStudentsArrangeItem> studentsArrangeItems = new ArrayList<>();
            for (final UserItem userItem : mViewModel.quizItem.getPeopleCanAccess()){
                StatisticsStudentsArrangeItem item = new StatisticsStudentsArrangeItem();
                item.setUserId(userItem.getId());
                item.setName(userItem.getUserName());
                for (ResultItem resultItem : mViewModel.results){
                    if (resultItem.getUserId().equals(userItem.getId())){
                        item.setGrade(resultItem.getResultScore());
                        item.setFinished(true);
                    }
                }
                studentsArrangeItems.add(item);
            }

            Collections.sort(studentsArrangeItems, new StudentsResultComparator());


            binding.students.removeAllViews();

            for (final StatisticsStudentsArrangeItem item: studentsArrangeItems){
                View view = getLayoutInflater().inflate(R.layout.row_people_can_access_quiz, binding.students, false);
                view.findViewById(R.id.removeImg).setVisibility(View.GONE);
                TextView usernameTv = view.findViewById(R.id.userNameTv);
                TextView gradeTv = view.findViewById(R.id.gradeTv);
                TextView finishTv = view.findViewById(R.id.finishTv);
                TextView waitingTv = view.findViewById(R.id.waitingTv);

                usernameTv.setText(item.getName());
                int gradeInt = (int) item.getGrade();
                gradeTv.setText(gradeInt + "");

                if (item.isFinished()){
                    finishTv.setVisibility(View.VISIBLE);
                }else {
                    waitingTv.setVisibility(View.VISIBLE);
                }

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogStudentResults.setResultItem(null);
                        dialogStudentResults.setStudentId(item.getUserId());
                        if (!dialogStudentResults.isAdded()){
                            dialogStudentResults.show(getActivity().getSupportFragmentManager(), "DialogStudentResults");
                        }
                    }
                });

                binding.students.addView(view);
            }


        }




        double totalSuccess = 0;
        double totalFullGrade = 0;
        double totalZeroGrade = 0;



        for (ResultItem resultItem : mViewModel.results){

            /**
             * Total success - Full grade - Zero grade
             */

            if (resultItem.getResultScore() >= mViewModel.quizItem.getQuizResult()){
                totalSuccess = totalSuccess+1;
            }

            if (resultItem.getResultScore() == 100){
                totalFullGrade = totalFullGrade+1;
            }

            if (resultItem.getResultScore() == 0){
                totalZeroGrade = totalZeroGrade + 1;
            }
        }



        if (mViewModel.results.size() == 0){
        }else {
            int totalFullGradeInteger = Integer.parseInt(String.format("%.0f",totalFullGrade));
            int totalZeroGradeInteger = Integer.parseInt(String.format("%.0f",totalZeroGrade));

            binding.fullGradeTv.setText(totalFullGradeInteger+"");
            binding.zeroGradeTv.setText(totalZeroGradeInteger+"");
        }



        /**
         * Result question analysis
         */

        // Add question analysis view
        binding.questions.removeAllViews();

        List<StatisitcsQuestionItem> statisitcsQuestionItems = new ArrayList<>();
        for (ResultItem resultItem : mViewModel.results){
                for (ResultQuestionItem resultQuestionItem : resultItem.getResultQuestion()){
                    int corrctAnswers = 0;
                    int wrongAnswers = 0;

                    if (resultQuestionItem.getChosenAnswer() == 0){
                        wrongAnswers++;
                    }else {
                        if (resultQuestionItem.getQuestionAnswers().get(resultQuestionItem.getChosenAnswer()-1).isCorrect()){
                            corrctAnswers++;
                        }else {
                            wrongAnswers++;
                        }
                    }

                    StatisitcsQuestionItem statisitcsQuestionItem = new StatisitcsQuestionItem();
                    statisitcsQuestionItem.setQuestionId(resultQuestionItem.getQuestionId());
                    statisitcsQuestionItem.setQuestionText(resultQuestionItem.getQuestionText());
                    statisitcsQuestionItem.setCorrect(corrctAnswers);
                    statisitcsQuestionItem.setWrong(wrongAnswers);
                    statisitcsQuestionItems.add(statisitcsQuestionItem);
                }
        }

        List<String> questionsIds = new ArrayList<>();
        for (StatisitcsQuestionItem statisitcsQuestionItem : statisitcsQuestionItems){
            if (questionsIds.indexOf(statisitcsQuestionItem.getQuestionId()) < 0){
                questionsIds.add(statisitcsQuestionItem.getQuestionId());
            }
        }

        binding.questions.removeAllViews();
        for (String questionId : questionsIds){
            int count = 0;
            int correct = 0;
            int wrong = 0;
            String questionText = "";

            for (StatisitcsQuestionItem statisitcsQuestionItem : statisitcsQuestionItems){
                if (statisitcsQuestionItem.getQuestionId().equals(questionId)){
                    count++;
                    questionText = statisitcsQuestionItem.getQuestionText();
                    correct = correct + statisitcsQuestionItem.getCorrect();
                    wrong = wrong + statisitcsQuestionItem.getWrong();

                }
            }


            View view = getLayoutInflater().inflate(R.layout.row_statistics_question, binding.questions, false);
            TextView questionTextTv = view.findViewById(R.id.questionTextTv);
            CircleProgressBar countCircle = view.findViewById(R.id.countCircle);
            CircleProgressBar correctCircle = view.findViewById(R.id.correctCircle);
            CircleProgressBar wrongCircle = view.findViewById(R.id.wrongCircle);

            TextView countTv = view.findViewById(R.id.countTv);
            TextView correctTv = view.findViewById(R.id.correctTv);
            TextView wrongTv = view.findViewById(R.id.wrongTv);

            questionTextTv.setText(questionText);

            countCircle.setMax(count);
            countCircle.setProgress(count);
            countTv.setText(count+ "");

            correctCircle.setMax(count);
            correctCircle.setProgress(correct);
            correctTv.setText(correct + "");


            wrongCircle.setMax(count);
            wrongCircle.setProgress(wrong);
            wrongTv.setText(wrong + "");

            binding.questions.addView(view);

        }


        /**
         * Dashboard
         */


        if (mViewModel.results.size() == 0){
            binding.totalResultCircleBar.setProgress(100);
            binding.totalResultTv.setText("NA");
        }else{
            double sucessPercentage = totalSuccess / mViewModel.results.size() *100 ;

            binding.totalResultCircleBar.setProgress((float) sucessPercentage);
            int totalResultInt = Integer.parseInt(String.format("%.0f",sucessPercentage));
            binding.totalResultTv.setText(totalResultInt + "%");
        }


        if (mViewModel.quizItem.getPeopleCanAccess() == null || mViewModel.quizItem.getPeopleCanAccess().size() == 0 ){

            binding.studentCountCircle.setMax(100);
            binding.studentFinishedCircle.setMax(100);
            binding.studentRemainsCircle.setMax(100);

            binding.studentCountCircle.setProgress(100);
            binding.studentFinishedCircle.setProgress(100);
            binding.studentRemainsCircle.setProgress(100);

            binding.studentsCountTv.setText("NA");
            binding.studentsFinishedTv.setText("NA");
            binding.studentsRemainTv.setText("NA");


        }else {

            binding.studentCountCircle.setMax(mViewModel.quizItem.getPeopleCanAccess().size());
            binding.studentFinishedCircle.setMax(mViewModel.quizItem.getPeopleCanAccess().size());
            binding.studentRemainsCircle.setMax(mViewModel.quizItem.getPeopleCanAccess().size());

            binding.studentCountCircle.setProgress(mViewModel.quizItem.getPeopleCanAccess().size());
            binding.studentFinishedCircle.setProgress(mViewModel.results.size());
            binding.studentRemainsCircle.setProgress(mViewModel.quizItem.getPeopleCanAccess().size() - mViewModel.results.size());

            binding.studentsCountTv.setText("" + mViewModel.quizItem.getPeopleCanAccess().size());
            binding.studentsFinishedTv.setText("" + mViewModel.results.size());

            int userfinished = 0;
            for (ResultItem resultItem : mViewModel.results){
                for (UserItem userItem: mViewModel.quizItem.getPeopleCanAccess()){
                    if (resultItem.getUserId().equals(userItem.getId())){
                        userfinished++;
                    }
                }
            }

            binding.studentsRemainTv.setText("" +  (mViewModel.quizItem.getPeopleCanAccess().size() - userfinished));


        }

        binding.refresher.setRefreshing(false);




    }


    @Override
    public void onResume() {
        super.onResume();

    }
}
