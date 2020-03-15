package mohalim.app.quizapp.core.di.activities.splash;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import mohalim.app.quizapp.core.di.key.ViewModelKey;
import mohalim.app.quizapp.ui.splash.SplashViewModel;

@Module
public abstract class SplashViewmodelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel.class)
    public abstract ViewModel bindMainViewModel(SplashViewModel splashViewModel);
}
