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

import java.util.List;

import javax.inject.Inject;

import mohalim.app.quizapp.R;
import mohalim.app.quizapp.core.di.base.BaseFragment;
import mohalim.app.quizapp.core.models.ResultItem;
import mohalim.app.quizapp.core.models.UserItem;
import mohalim.app.quizapp.core.utils.ViewModelProviderFactory;
import mohalim.app.quizapp.databinding.FragmentStatisticsBinding;
import mohalim.app.quizapp.ui.result.DialogStudentResults;


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
                }else if (tab.getPosition() == 1){
                    binding.statistics.setVisibility(View.GONE);
                    binding.students.setVisibility(View.VISIBLE);
                    binding.questions.setVisibility(View.GONE);

                }else if (tab.getPosition() == 2){
                    binding.statistics.setVisibility(View.GONE);
                    binding.students.setVisibility(View.GONE);
                    binding.questions.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




        if (mViewModel.quizItem.getPeopleCanAccess() != null){
            for (final UserItem userItem : mViewModel.quizItem.getPeopleCanAccess()){
                View view = getLayoutInflater().inflate(R.layout.row_people_can_access_quiz, binding.students, false);
                view.findViewById(R.id.removeImg).setVisibility(View.GONE);
                TextView usernameTv = view.findViewById(R.id.userNameTv);
                usernameTv.setText(userItem.getUserName());

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogStudentResults.setResultItem(null);
                        dialogStudentResults.setStudentId(userItem.getId());
                        if (!dialogStudentResults.isAdded()){
                            dialogStudentResults.show(getActivity().getSupportFragmentManager(), "DialogStudentResults");
                        }
                    }
                });

                binding.students.addView(view);
            }
        }





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

        double totalSuccess = 0;
        double totalFullGrade = 0;
        double totalZeroGrade = 0;

        for (ResultItem resultItem : mViewModel.results){
            if (resultItem.getResultScore() >= mViewModel.quizItem.getQuizResult()){
                totalSuccess = totalSuccess+1;
            }

            if (resultItem.getResultScore() == 100){
                totalFullGrade = totalFullGrade+1;
            }

            if (resultItem.getResultScore() == 100){
                totalZeroGrade = totalZeroGrade + 1;
            }
        }


        if (mViewModel.results.size() == 0){
            binding.totalResultCircleBar.setProgress(100);
            binding.totalResultTv.setText("NA");
        }else{
            double sucessPercentage = totalSuccess / mViewModel.results.size() *100 ;

            binding.totalResultCircleBar.setProgress((float) sucessPercentage);
            int totalResultInt = Integer.parseInt(String.format("%.0f",sucessPercentage));
            binding.totalResultTv.setText(totalResultInt + "%");
        }


        if (mViewModel.quizItem.getPeopleCanAccess() == null || mViewModel.quizItem.getPeopleCanAccess().size() == 0){

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
            binding.studentsRemainTv.setText("" +  (mViewModel.quizItem.getPeopleCanAccess().size() - mViewModel.results.size()));

        }


        if (mViewModel.results.size() == 0){
        }else {
            int totalFullGradeInteger = Integer.parseInt(String.format("%.0f",totalFullGrade));
            int totalZeroGradeInteger = Integer.parseInt(String.format("%.0f",totalZeroGrade));

            binding.fullGradeTv.setText(totalFullGradeInteger+"");
            binding.zeroGradeTv.setText(totalZeroGradeInteger+"");
        }

        binding.refresher.setRefreshing(false);


    }


    @Override
    public void onResume() {
        super.onResume();

    }
}
