package mohalim.app.quizapp.core.di.activities.user_main;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mohalim.app.quizapp.ui.dialog.DialogLoading;
import mohalim.app.quizapp.ui.user_main.UserMainFragment;

@Module
public abstract class UserMainFragmentsModules {

    @ContributesAndroidInjector
    abstract UserMainFragment contributeUserMainFragment();

    @ContributesAndroidInjector
    abstract DialogLoading contributeLoadingDialog();
}
