package mohalim.app.quizapp.core.di.activities.user_main;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import mohalim.app.quizapp.core.di.key.ViewModelKey;
import mohalim.app.quizapp.ui.user_main.UserMainViewModel;

@Module
public abstract class UserMainViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(UserMainViewModel.class)
    abstract ViewModel bindsUserMainViewModel(UserMainViewModel userMainViewModel);
}
