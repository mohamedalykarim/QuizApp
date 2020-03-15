package mohalim.app.quizapp.core.di.activities.result;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mohalim.app.quizapp.ui.dialog.DialogWrongAnswers;
import mohalim.app.quizapp.ui.result.ResultFragment;

@Module
public abstract class ResultFragmentsModule {
    @ContributesAndroidInjector
    abstract ResultFragment contributeResultFragment();

    @ContributesAndroidInjector
    abstract DialogWrongAnswers contributeDialogWrongAnswers();
}
