package mohalim.app.quizapp.ui.questions;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import mohalim.app.quizapp.core.models.AnswerItem;
import mohalim.app.quizapp.core.models.QuestionItem;
import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.utils.Constants;
import mohalim.app.quizapp.core.utils.ViewModelProviderFactory;
import mohalim.app.quizapp.databinding.BottomAddQuestionBinding;

public class AddQuestionBottomSheert extends BottomSheetDialogFragment implements HasAndroidInjector {
    @Inject
    DispatchingAndroidInjector<Object> androidInjector;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    private BottomAddQuestionBinding binding;
    QuestionsViewModel mViewModel;
    private int formError;
    QuizItem quizItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomAddQuestionBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(QuestionsViewModel.class);
        Intent intent = getActivity().getIntent();
        if (!intent.hasExtra(Constants.QUIZ_ITEM)){
            dismiss();
        }

        quizItem = intent.getParcelableExtra(Constants.QUIZ_ITEM);

        initView();

        return binding.getRoot();
    }

    private void initView() {

        binding.switchAnswer1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked){
                    if (binding.answerEt1.getText().toString().equals("")){
                        binding.answerEt1.setError("Enter Answer First");
                        binding.switchAnswer1.setChecked(false);
                        return;
                    }else {
                        binding.switchAnswer2.setChecked(false);
                        binding.switchAnswer3.setChecked(false);
                        binding.switchAnswer4.setChecked(false);
                        binding.switchAnswer5.setChecked(false);
                    }
                }
            }
        });

        binding.switchAnswer2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked){
                    if (binding.answerEt2.getText().toString().equals("")){
                        binding.answerEt2.setError("Enter Answer First");
                        binding.switchAnswer2.setChecked(false);
                        return;
                    }else {
                        binding.switchAnswer1.setChecked(false);
                        binding.switchAnswer3.setChecked(false);
                        binding.switchAnswer4.setChecked(false);
                        binding.switchAnswer5.setChecked(false);
                    }
                }
            }
        });

        binding.switchAnswer3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked){
                    if (binding.answerEt3.getText().toString().equals("")){
                        binding.answerEt3.setError("Enter Answer First");
                        binding.switchAnswer3.setChecked(false);
                        return;
                    }else {
                        binding.switchAnswer2.setChecked(false);
                        binding.switchAnswer1.setChecked(false);
                        binding.switchAnswer4.setChecked(false);
                        binding.switchAnswer5.setChecked(false);
                    }
                }
            }
        });

        binding.switchAnswer4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked){
                    if (binding.answerEt4.getText().toString().equals("")){
                        binding.answerEt4.setError("Enter Answer First");
                        binding.switchAnswer4.setChecked(false);
                        return;
                    }else {
                        binding.switchAnswer2.setChecked(false);
                        binding.switchAnswer3.setChecked(false);
                        binding.switchAnswer1.setChecked(false);
                        binding.switchAnswer5.setChecked(false);
                    }
                }
            }
        });

        binding.switchAnswer5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked){
                    if (binding.answerEt5.getText().toString().equals("")){
                        binding.answerEt5.setError("Enter Answer First");
                        binding.switchAnswer5.setChecked(false);
                        return;
                    }else {
                        binding.switchAnswer2.setChecked(false);
                        binding.switchAnswer3.setChecked(false);
                        binding.switchAnswer4.setChecked(false);
                        binding.switchAnswer1.setChecked(false);
                    }
                }
            }
        });

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateForm();
                if (formError > 0)return;

                QuestionItem questionItem = new QuestionItem();
                questionItem.setQuestionText(binding.questionEt.getText().toString());

                List<AnswerItem> answers = new ArrayList<>();

                if (!binding.answerEt1.getText().toString().equals("")){
                    AnswerItem answerItem = new AnswerItem();
                    answerItem.setAnswerText(binding.answerEt1.getText().toString());
                    answerItem.setCorrect(binding.switchAnswer1.isChecked());
                    answerItem.setArrange(1);
                    answers.add(answerItem);
                }

                if (!binding.answerEt2.getText().toString().equals("")){
                    AnswerItem answerItem = new AnswerItem();
                    answerItem.setAnswerText(binding.answerEt2.getText().toString());
                    answerItem.setCorrect(binding.switchAnswer2.isChecked());
                    answerItem.setArrange(2);
                    answers.add(answerItem);
                }

                if (!binding.answerEt3.getText().toString().equals("")){
                    AnswerItem answerItem = new AnswerItem();
                    answerItem.setAnswerText(binding.answerEt3.getText().toString());
                    answerItem.setCorrect(binding.switchAnswer3.isChecked());
                    answerItem.setArrange(3);
                    answers.add(answerItem);
                }

                if (!binding.answerEt4.getText().toString().equals("")){
                    AnswerItem answerItem = new AnswerItem();
                    answerItem.setAnswerText(binding.answerEt4.getText().toString());
                    answerItem.setCorrect(binding.switchAnswer4.isChecked());
                    answerItem.setArrange(4);
                    answers.add(answerItem);
                }

                if (!binding.answerEt5.getText().toString().equals("")){
                    AnswerItem answerItem = new AnswerItem();
                    answerItem.setAnswerText(binding.answerEt5.getText().toString());
                    answerItem.setCorrect(binding.switchAnswer5.isChecked());
                    answerItem.setArrange(5);
                    answers.add(answerItem);
                }

                questionItem.setQuestionAnswers(answers);

                mViewModel.startInsertQuestion(quizItem, questionItem);
                dismiss();

                binding.questionEt.setText("");
                binding.answerEt1.setText("");
                binding.answerEt2.setText("");
                binding.answerEt3.setText("");
                binding.answerEt4.setText("");
                binding.answerEt5.setText("");
                binding.switchAnswer1.setChecked(false);
                binding.switchAnswer2.setChecked(false);
                binding.switchAnswer3.setChecked(false);
                binding.switchAnswer4.setChecked(false);
                binding.switchAnswer5.setChecked(false);




            }
        });
    }

    private void validateForm() {
        formError = 0;
        if (binding.questionEt.getText().toString().equals("")){
            binding.questionEt.setError("Enter Question please");
            formError++;
        }

        if (
                !binding.switchAnswer1.isChecked()
                && !binding.switchAnswer2.isChecked()
                && !binding.switchAnswer3.isChecked()
                && !binding.switchAnswer4.isChecked()
                && !binding.switchAnswer5.isChecked()
        ){
            Toast.makeText(getContext(), "Please choose correct answer", Toast.LENGTH_SHORT).show();
            formError++;
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

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
