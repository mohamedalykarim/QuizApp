package mohalim.app.quizapp.core.di.activities.admin_main;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mohalim.app.quizapp.ui.admin_main.AdminMainFragment;
import mohalim.app.quizapp.ui.dialog.DialogLoading;
import mohalim.app.quizapp.ui.dialog.DialogPeopleCanAccessQuiz;
import mohalim.app.quizapp.ui.admin_main.AddQuizBottomSheet;
import mohalim.app.quizapp.ui.admin_main.UpdateQuizBottomSheet;

@Module
public abstract class AdminMainFragmentsModule {
    @ContributesAndroidInjector
    abstract AdminMainFragment contributeMainFragment();

    @ContributesAndroidInjector
    abstract AddQuizBottomSheet contributeAddQuizBottomSheet();

    @ContributesAndroidInjector
    abstract UpdateQuizBottomSheet contributeUpdateQuizBottomSheet();

    @ContributesAndroidInjector
    abstract DialogLoading contributeLoadingDialog();

    @ContributesAndroidInjector
    abstract DialogPeopleCanAccessQuiz contributeDialogPeopleCanAccessQuiz();
}
