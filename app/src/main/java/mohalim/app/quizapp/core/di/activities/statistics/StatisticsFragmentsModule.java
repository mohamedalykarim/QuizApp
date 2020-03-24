package mohalim.app.quizapp.core.di.activities.statistics;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mohalim.app.quizapp.ui.statistics.StatisticsActivity;
import mohalim.app.quizapp.ui.statistics.StatisticsFragment;

@Module
public abstract class StatisticsFragmentsModule {

    @ContributesAndroidInjector
    abstract StatisticsFragment contributeStatisticsFragment();



}
