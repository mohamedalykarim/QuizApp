package mohalim.app.quizapp.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import mohalim.app.quizapp.R;
import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.repositories.QuizRepository;
import mohalim.app.quizapp.core.utils.Constants;
import mohalim.app.quizapp.core.utils.InputFilterMinMax;
import mohalim.app.quizapp.core.utils.ViewModelProviderFactory;
import mohalim.app.quizapp.databinding.BottomAddQuizBinding;

public class AddQuizBottomSheet extends BottomSheetDialogFragment implements HasAndroidInjector {
    @Inject
    DispatchingAndroidInjector<Object> androidInjector;

    @Inject
    QuizRepository quizRepository;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    FirebaseAuth mAuth;

    BottomAddQuizBinding binding;

    AddNewQuizListener addNewQuizListener;

    int formErrors = 0;
    private MainViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomAddQuizBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(MainViewModel.class);
        binding.quizResultGradeEt.setFilters(new InputFilter[]{new InputFilterMinMax("1", "100")});

        String[] spinnerItems = new String[]{"Left", "Right"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        binding.swipeDirectionSpinner.setAdapter(spinnerAdapter);
        binding.navDirectionSpinner.setAdapter(spinnerAdapter);



        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateForm();
                if (formErrors > 0) return;

                QuizItem quizItem = new QuizItem();
                quizItem.setQuizName(binding.nameEt.getText().toString());
                quizItem.setQuizDescription(binding.descriptionEt.getText().toString());
                quizItem.setQuestionCountWanted(Integer.parseInt(binding.questionsCountEt.getText().toString()));
                quizItem.setOwner(mAuth.getUid());
                quizItem.setQuizResult(Integer.parseInt(binding.quizResultGradeEt.getText().toString()));
                quizItem.setCheckAnswerWorking(binding.checkAnswerWorkingSwitch.isChecked());

                if (binding.swipeDirectionSpinner.getSelectedItem().toString().equals("Right")){
                    quizItem.setQuizSwipeDirection(Constants.RIGHT);
                }else{
                    quizItem.setQuizSwipeDirection(Constants.LEFT);
                }

                if (binding.navDirectionSpinner.getSelectedItem().toString().equals("Right")){
                    quizItem.setQuizNavigationDirection(Constants.RIGHT);
                }else {
                    quizItem.setQuizNavigationDirection(Constants.LEFT);
                }



                mViewModel.startAddQuiz(quizItem);
                binding.nameEt.setText("");
                binding.descriptionEt.setText("");
                binding.questionsCountEt.setText("");
                binding.quizResultGradeEt.setText("");
                dismiss();
                addNewQuizListener.onAddNewQuiz();
            }
        });

        return binding.getRoot();
    }

    private void validateForm() {
        formErrors = 0;
        if (binding.nameEt.getText().toString().equals("")){
            formErrors = formErrors +1;
            binding.nameEt.setError("Enter Quiz name");
        }

        if (binding.descriptionEt.getText().toString().equals("")){
            formErrors = formErrors +1;
            binding.descriptionEt.setError("Enter Quiz description");
        }

        if (binding.questionsCountEt.getText().toString().equals("")){
            formErrors = formErrors+1;
            binding.questionsCountEt.setError("Enter Question count wanted");
        }

        if (binding.quizResultGradeEt.getText().toString().equals("")){
            formErrors = formErrors+1;
            binding.quizResultGradeEt.setError("Enter quiz result wanted");
        }

    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);

        try {
            addNewQuizListener = (AddNewQuizListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException("Activity must implement AddNewQuizListener class");
        }
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return androidInjector;
    }


    public interface AddNewQuizListener{
        void onAddNewQuiz();
    }
}
