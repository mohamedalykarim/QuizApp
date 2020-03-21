package mohalim.app.quizapp.core.di.activities.feedback;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mohalim.app.quizapp.ui.feedback.FeedBackFragment;

@Module
public abstract class FeedBackFragmentsModule {

    @ContributesAndroidInjector
    abstract FeedBackFragment contributeFeedbackFragment();
}
