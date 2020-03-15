package mohalim.app.quizapp.core.di.activities.splash;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mohalim.app.quizapp.ui.splash.SplashFragment;

@Module
public abstract class SplashFragmentModule {
    @ContributesAndroidInjector
    abstract SplashFragment provideSplashFragment();
}
