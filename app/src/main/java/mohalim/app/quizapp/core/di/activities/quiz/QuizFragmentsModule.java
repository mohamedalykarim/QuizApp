package mohalim.app.quizapp.core.di.activities.quiz;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mohalim.app.quizapp.ui.quiz.QuizFragment;

@Module
public abstract class QuizFragmentsModule {

    @ContributesAndroidInjector
    abstract QuizFragment contributeQuizFragment();
}
