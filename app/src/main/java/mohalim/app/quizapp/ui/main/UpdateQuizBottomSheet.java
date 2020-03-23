package mohalim.app.quizapp.ui.main;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.repositories.QuizRepository;
import mohalim.app.quizapp.core.utils.Constants;
import mohalim.app.quizapp.core.utils.InputFilterMinMax;
import mohalim.app.quizapp.core.utils.ViewModelProviderFactory;
import mohalim.app.quizapp.databinding.BottomAddQuizBinding;
import mohalim.app.quizapp.databinding.BottomUpdateQuizBinding;

public class UpdateQuizBottomSheet extends BottomSheetDialogFragment implements HasAndroidInjector {
    @Inject
    DispatchingAndroidInjector<Object> androidInjector;

    @Inject
    QuizRepository quizRepository;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    BottomUpdateQuizBinding binding;

    UpdateQuizListener updateQuizListener;

    int formErrors = 0;
    private MainViewModel mViewModel;
    QuizItem oldQuizItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomUpdateQuizBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(MainViewModel.class);
        binding.quizResultGradeEt.setFilters(new InputFilter[]{new InputFilterMinMax("1", "100")});
        binding.timeInMinutesEt.setFilters(new InputFilter[]{new InputFilterMinMax("0", "300")});


        binding.setOldQuizItem(oldQuizItem);

        String[] spinnerItems = new String[]{"Left", "Right"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        binding.swipeDirectionSpinner.setAdapter(spinnerAdapter);
        binding.navDirectionSpinner.setAdapter(spinnerAdapter);


        binding.quizIdTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("quiz id", oldQuizItem.getId());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Quiz id copied", Toast.LENGTH_SHORT).show();

            }
        });


        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateForm();
                if (formErrors > 0) return;

                QuizItem quizItem = new QuizItem();
                quizItem.setId(oldQuizItem.getId());
                quizItem.setQuizName(binding.nameEt.getText().toString());
                quizItem.setQuizDescription(binding.descriptionEt.getText().toString());
                quizItem.setQuestionCountWanted(Integer.parseInt(binding.questionsCountEt.getText().toString()));
                quizItem.setOwner(oldQuizItem.getOwner());
                quizItem.setQuizResult(Integer.parseInt(binding.quizResultGradeEt.getText().toString()));
                quizItem.setCheckAnswerWorking(binding.checkAnswerWorkingSwitch.isChecked());
                quizItem.setTimeInMinutes(Integer.parseInt(binding.timeInMinutesEt.getText().toString()));
                quizItem.setShowResults(binding.showResultsSwitch.isChecked());
                quizItem.setSaveResults(binding.saveResultSwitch.isChecked());
                quizItem.setPeopleCanAccess(oldQuizItem.getPeopleCanAccess());

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



                mViewModel.startUpdateQuiz(quizItem);
                dismiss();
                updateQuizListener.onUpdateQuiz();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (oldQuizItem.getQuizSwipeDirection().equals(Constants.RIGHT)){
            binding.swipeDirectionSpinner.setSelection(1);
        }

        if (oldQuizItem.getQuizNavigationDirection().equals(Constants.RIGHT)){
            binding.navDirectionSpinner.setSelection(1);
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        binding.nameEt.setText("");
        binding.descriptionEt.setText("");
        binding.questionsCountEt.setText("");
        binding.quizResultGradeEt.setText("");

        binding.navDirectionSpinner.setSelection(0);
        binding.swipeDirectionSpinner.setSelection(0);


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

        if (binding.timeInMinutesEt.getText().toString().equals("")){
            formErrors = formErrors+1;
            binding.quizResultGradeEt.setError("Enter quiz time");
        }

    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);

        try {
            updateQuizListener = (UpdateQuizListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException("Activity must implement AddNewQuizListener class");
        }
    }

    public void setOldQuizItem(QuizItem oldQuizItem) {
        this.oldQuizItem = oldQuizItem;
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return androidInjector;
    }


    public interface UpdateQuizListener{
        void onUpdateQuiz();
    }
}
