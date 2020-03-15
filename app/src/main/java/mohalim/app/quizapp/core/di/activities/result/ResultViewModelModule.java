package mohalim.app.quizapp.core.di.activities.result;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import mohalim.app.quizapp.core.di.key.ViewModelKey;
import mohalim.app.quizapp.ui.result.ResultViewModel;

@Module
public abstract class ResultViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ResultViewModel.class)
    abstract ViewModel bindsResultViewModel(ResultViewModel resultViewModel);
}
