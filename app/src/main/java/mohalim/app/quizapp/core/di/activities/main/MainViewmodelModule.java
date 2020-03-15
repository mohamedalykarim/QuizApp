package mohalim.app.quizapp.core.di.activities.main;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;
import mohalim.app.quizapp.core.di.key.ViewModelKey;
import mohalim.app.quizapp.ui.main.MainViewModel;

@Module
public abstract class MainViewmodelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel bindsMainViewmodel(MainViewModel mainViewModel);
}
