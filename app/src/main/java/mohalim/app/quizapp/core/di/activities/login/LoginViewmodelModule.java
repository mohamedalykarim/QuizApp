package mohalim.app.quizapp.core.di.activities.login;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import mohalim.app.quizapp.core.di.key.ViewModelKey;
import mohalim.app.quizapp.ui.login.LoginViewModel;

@Module
public abstract class LoginViewmodelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    public abstract ViewModel bindLoginViewmodel(LoginViewModel loginViewModel);

}
