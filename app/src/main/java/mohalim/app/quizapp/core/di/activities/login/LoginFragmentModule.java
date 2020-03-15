package mohalim.app.quizapp.core.di.activities.login;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mohalim.app.quizapp.ui.login.LoginFragment;

@Module
public abstract class LoginFragmentModule {

    @ContributesAndroidInjector
    abstract LoginFragment contributeLoginFragment();

}
