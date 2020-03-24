package mohalim.app.quizapp.core.di.activities.statistics;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import mohalim.app.quizapp.core.di.key.ViewModelKey;
import mohalim.app.quizapp.ui.statistics.StatisticsViewModel;

@Module
public abstract class StatisticsViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(StatisticsViewModel.class)
    abstract ViewModel bindsStatisticsViewModel(StatisticsViewModel statisticsViewModel);
}
