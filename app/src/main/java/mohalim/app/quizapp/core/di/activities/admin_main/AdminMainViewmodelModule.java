package mohalim.app.quizapp.core.di.activities.admin_main;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import mohalim.app.quizapp.core.di.key.ViewModelKey;
import mohalim.app.quizapp.ui.admin_main.AdminMainViewModel;

@Module
public abstract class AdminMainViewmodelModule {
    @Binds
    @IntoMap
    @ViewModelKey(AdminMainViewModel.class)
    abstract ViewModel bindsMainViewmodel(AdminMainViewModel adminMainViewModel);
}
