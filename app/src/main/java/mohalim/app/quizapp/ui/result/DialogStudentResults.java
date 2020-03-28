package mohalim.app.quizapp.ui.result;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import mohalim.app.quizapp.R;
import mohalim.app.quizapp.core.models.ResultItem;
import mohalim.app.quizapp.core.models.ResultQuestionItem;
import mohalim.app.quizapp.core.utils.ViewModelProviderFactory;
import mohalim.app.quizapp.databinding.DialogStudentResultsBinding;
import mohalim.app.quizapp.ui.statistics.StatisticsViewModel;

public class DialogStudentResults extends DialogFragment implements HasAndroidInjector {
    @Inject
    DispatchingAndroidInjector<Object> androidInjector;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    StatisticsViewModel mViewModel;
    DialogStudentResultsBinding binding;

    String studentId;
    ResultItem resultItem;
    private boolean wrongAnswerExists;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogStudentResultsBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(StatisticsViewModel.class);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        for (ResultItem resultItem : mViewModel.results){
            if (resultItem.getUserId().equals(studentId)){
                this.resultItem = resultItem;
            }
        }

        binding.successGradeTv.setText("Successful grade is "+ mViewModel.quizItem.getQuizResult()+"%");

        updateUi();
        wrongAnswerExists = false;
        return binding.getRoot();
    }

    private void updateUi() {

        if (resultItem == null){
            binding.resultTv.setText("NA");
            binding.correctAnswersTv.setText("NA");
            binding.wrongAnswersTv.setText("NA");
            return;
        };

        binding.nameTv.setText(resultItem.getUsername());

        binding.resultTv.setText(resultItem.getResultScore() + "%");

        double correctAnswers = 0;

        for (ResultQuestionItem resultQuestionItem : resultItem.getResultQuestion()){
            if (resultQuestionItem.getChosenAnswer() != 0){
                if (resultQuestionItem.getQuestionAnswers().get(resultQuestionItem.getChosenAnswer()-1).isCorrect()){
                    correctAnswers = correctAnswers+1;
                }else {
                    addWrongQuestion(resultQuestionItem.getQuestionText());
                }
            }else{
                addWrongQuestion(resultQuestionItem.getQuestionText());
            }

        }

        double wrongAnswers = resultItem.getResultQuestion().size() - correctAnswers;

        int correctAnswersInteger = Integer.parseInt(String.format("%.0f",correctAnswers));
        int wrongAnswersInteger = Integer.parseInt(String.format("%.0f",wrongAnswers));

        binding.correctAnswersTv.setText(correctAnswersInteger + "");
        binding.wrongAnswersTv.setText(wrongAnswersInteger+ "");

        if (!wrongAnswerExists) binding.linearLayout2.setVisibility(View.GONE);

    }

    private void addWrongQuestion(String questionText) {
        wrongAnswerExists = true;
        View view = getLayoutInflater().inflate(R.layout.row_people_can_access_quiz, binding.wrongQuestionContainer, false);
        view.findViewById(R.id.removeImg).setVisibility(View.GONE);
        TextView usernameTv = view.findViewById(R.id.userNameTv);
        usernameTv.setText(questionText);
        binding.wrongQuestionContainer.addView(view);
    }

    @Override
    public void onResume() {
        super.onResume();

        getDialog().getWindow().setLayout(
                ScrollView.LayoutParams.MATCH_PARENT,
                ScrollView.LayoutParams.MATCH_PARENT
        );

    }

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return androidInjector;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setResultItem(ResultItem resultItem) {
        this.resultItem = resultItem;
    }
}
