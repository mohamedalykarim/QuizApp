package mohalim.app.quizapp.core.di.activities.statistics;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mohalim.app.quizapp.ui.statistics.DialogStudentResults;
import mohalim.app.quizapp.ui.statistics.StatisticsFragment;

@Module
public abstract class StatisticsFragmentsModule {

    @ContributesAndroidInjector
    abstract StatisticsFragment contributeStatisticsFragment();

    @ContributesAndroidInjector
    abstract DialogStudentResults contributeDialogStudentResults();

}
