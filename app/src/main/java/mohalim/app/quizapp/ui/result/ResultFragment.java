package mohalim.app.quizapp.ui.result;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import javax.inject.Inject;

import mohalim.app.quizapp.R;
import mohalim.app.quizapp.core.di.base.BaseFragment;
import mohalim.app.quizapp.core.models.QuestionItem;
import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.utils.Constants;
import mohalim.app.quizapp.core.utils.ViewModelProviderFactory;
import mohalim.app.quizapp.databinding.FragmentResultBinding;
import mohalim.app.quizapp.ui.dialog.DialogWrongAnswers;


public class ResultFragment extends BaseFragment {
    double questionCount;
    double correctAnswersCount;
    double wrongAnswersCount;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;
    private ResultViewModel mViewModel;
    FragmentResultBinding binding;
    private double result;

    ArrayList<QuestionItem> questionItems;
    QuizItem quizItem;

    DialogWrongAnswers dialogWrongAnswers;

    public static ResultFragment newInstance() {
        return new ResultFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        if (intent == null || !intent.hasExtra(Constants.QUESTION_ITEM)){
            getActivity().finish();
        }


        binding = FragmentResultBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(ResultViewModel.class);


        /**
         * Get Intent Extras
         */
        questionItems = intent.getParcelableArrayListExtra(Constants.QUESTION_ITEM);
        quizItem = intent.getParcelableExtra(Constants.QUIZ_ITEM);


        /**
         * Show Results
         */
        questionCount = questionItems.size();
        for (QuestionItem questionItem : questionItems){
            if (questionItem.isChosenAnswerCorrect()){
                correctAnswersCount++;
            }else {
                wrongAnswersCount++;
            }
        }

        dialogWrongAnswers = DialogWrongAnswers.newInstance();

        if (wrongAnswersCount == 0 ){
            binding.wrongAnswersBtn.setVisibility(View.GONE);
        }

        binding.wrongAnswersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogWrongAnswers.show(getActivity().getSupportFragmentManager(), "DialogWrongAnswers");
            }
        });

        result = ((correctAnswersCount/questionCount) * 100);
        binding.resultTv.setText(String.format("%.0f", result )+ "%");
        if (quizItem != null){
            binding.quizTitleTv.setText(quizItem.getQuizName() + " Result");
        }

        if (result < quizItem.getQuizResult()){
            binding.congratsTopImg.setVisibility(View.GONE);
        }

        binding.quizGradeAlertTv.setText("Success grade on this quiz is" + " " + quizItem.getQuizResult() + "%");

        return binding.getRoot();
    }


}
