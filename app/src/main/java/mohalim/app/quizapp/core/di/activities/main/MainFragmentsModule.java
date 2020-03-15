package mohalim.app.quizapp.core.di.activities.main;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mohalim.app.quizapp.ui.dialog.DialogLoading;
import mohalim.app.quizapp.ui.main.AddQuizBottomSheet;
import mohalim.app.quizapp.ui.main.MainFragment;
import mohalim.app.quizapp.ui.main.UpdateQuizBottomSheet;

@Module
public abstract class MainFragmentsModule {
    @ContributesAndroidInjector
    abstract MainFragment contributeMainFragment();

    @ContributesAndroidInjector
    abstract AddQuizBottomSheet contributeAddQuizBottomSheet();

    @ContributesAndroidInjector
    abstract UpdateQuizBottomSheet contributeUpdateQuizBottomSheet();

    @ContributesAndroidInjector
    abstract DialogLoading contributeLoadingDialog();
}
