package mohalim.app.quizapp.ui.questions;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import mohalim.app.quizapp.R;
import mohalim.app.quizapp.core.di.base.BaseActivity;
import mohalim.app.quizapp.core.models.QuestionItem;
import mohalim.app.quizapp.core.utils.Constants;
import mohalim.app.quizapp.core.utils.ViewModelProviderFactory;


public class QuestionsActivity extends BaseActivity implements
        AddQuestionBottomSheert.OnAddNewQuestion,
        QuestionPagedAdapter.QuestionAdapterClickListener,
        EditQuestionBottomSheet.OnUpdateQuestion {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;
    private QuestionsViewModel mViewModel;

    EditQuestionBottomSheet editQuestionBottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, QuestionsFragment.newInstance())
                    .commitNow();
        }

        editQuestionBottomSheet = new EditQuestionBottomSheet();

        mViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(QuestionsViewModel.class);
    }

    @Override
    public void onAddNewQuestion() {
        ((QuestionsFragment)getSupportFragmentManager().getFragments().get(0)).onAddNewQuestion();
    }

    @Override
    public void onQuestionAdapterClick(String type, QuestionItem questionItem) {
        if (type.equals(Constants.EDIT)){
            if (!editQuestionBottomSheet.isAdded()){
                editQuestionBottomSheet.setOldQuestionItem(questionItem);
                editQuestionBottomSheet.show(getSupportFragmentManager(), "EditQuestionBottomSheet");
            }
        }

    }

    @Override
    public void onUpdateQuestion() {
        mViewModel.refresh();
    }
}
