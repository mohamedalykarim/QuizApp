package mohalim.app.quizapp.core.di.module;


import androidx.lifecycle.ViewModelProvider;


import dagger.Binds;
import dagger.Module;
import mohalim.app.quizapp.core.utils.ViewModelProviderFactory;

@Module
public abstract class ViewmodelFactoryModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory factory);
}
