package mohalim.app.quizapp.core.di.activities.feedback;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import mohalim.app.quizapp.core.di.key.ViewModelKey;
import mohalim.app.quizapp.ui.feedback.FeedBackViewModel;

@Module
public abstract class FeedBackViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(FeedBackViewModel.class)
    abstract ViewModel bindsFeedBackViewModel(FeedBackViewModel feedBackViewModel);
}
