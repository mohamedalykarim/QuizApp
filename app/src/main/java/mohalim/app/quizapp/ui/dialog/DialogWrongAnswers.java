package mohalim.app.quizapp.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import mohalim.app.quizapp.R;
import mohalim.app.quizapp.core.models.AnswerItem;
import mohalim.app.quizapp.core.models.QuestionItem;
import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.utils.AppExecutor;
import mohalim.app.quizapp.core.utils.Constants;
import mohalim.app.quizapp.core.utils.ViewModelProviderFactory;
import mohalim.app.quizapp.databinding.DialogWrongAnswersBinding;
import mohalim.app.quizapp.ui.result.ResultViewModel;

public class DialogWrongAnswers extends DialogFragment implements HasAndroidInjector {
    @Inject
    DispatchingAndroidInjector<Object> androidInjector;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    AppExecutor appExecutor;

    private ResultViewModel mViewModel;
    DialogWrongAnswersBinding binding;

    List<QuestionItem> questionItems;
    QuizItem quizItem;


    public static DialogWrongAnswers newInstance() {
        
        Bundle args = new Bundle();
        
        DialogWrongAnswers fragment = new DialogWrongAnswers();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogWrongAnswersBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(ResultViewModel.class);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        Intent intent = getActivity().getIntent();
        quizItem = intent.getParcelableExtra(Constants.QUIZ_ITEM);
        questionItems = intent.getParcelableArrayListExtra(Constants.QUESTION_ITEM);



        for (final QuestionItem questionItem : questionItems){
            final View view = inflater.inflate(R.layout.row_question, binding.container, false);
            TextView questionTv = view.findViewById(R.id.questionTextTv);
            final TextView answersTv = view.findViewById(R.id.answersTv);

            questionTv.setText(questionItem.getQuestionText());

            appExecutor.diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    List<AnswerItem> answerItems = mViewModel.getAnswersForQuestionFromInternal(questionItem.getId(), quizItem.getId());
                    String correctAnswerText = "";
                    for (final AnswerItem answerItem: answerItems){
                        appExecutor.mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                answersTv.append(answerItem.getAnswerText() + "\n");
                            }
                        });
                        if (answerItem.isCorrect()) correctAnswerText = answerItem.getAnswerText();
                    }

                    final String finalCorrectAnswerText = correctAnswerText;
                    appExecutor.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            answersTv.append("\n" + getString(R.string.correct_answer___)+ " " + finalCorrectAnswerText);
                            binding.container.addView(view);
                        }
                    });

                }
            });

        }

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(ScrollView.LayoutParams.MATCH_PARENT,ScrollView.LayoutParams.MATCH_PARENT);
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


}
