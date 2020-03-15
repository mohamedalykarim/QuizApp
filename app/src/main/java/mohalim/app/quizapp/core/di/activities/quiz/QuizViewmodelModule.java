package mohalim.app.quizapp.core.di.activities.quiz;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import mohalim.app.quizapp.core.di.key.ViewModelKey;
import mohalim.app.quizapp.ui.quiz.QuizViewModel;

@Module
public abstract class QuizViewmodelModule {

    @Binds
    @IntoMap
    @ViewModelKey(QuizViewModel.class)
    abstract ViewModel bindsQuizViewModel(QuizViewModel quizViewModel);
}
