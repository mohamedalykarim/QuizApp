package mohalim.app.quizapp.core.di.activities.questions;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mohalim.app.quizapp.ui.questions.AddQuestionBottomSheert;
import mohalim.app.quizapp.ui.questions.QuestionsFragment;

@Module
public abstract class QuestionFragmentModule {
    @ContributesAndroidInjector
    abstract QuestionsFragment contributeQuestionsFragment();

    @ContributesAndroidInjector
    abstract AddQuestionBottomSheert contributeAddQuestionBottomSheet();
}
