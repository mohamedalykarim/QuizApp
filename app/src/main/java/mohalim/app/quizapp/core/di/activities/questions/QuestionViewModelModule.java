package mohalim.app.quizapp.core.di.activities.questions;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import mohalim.app.quizapp.core.di.key.ViewModelKey;
import mohalim.app.quizapp.ui.questions.QuestionsViewModel;

@Module
public abstract class QuestionViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(QuestionsViewModel.class)
    abstract ViewModel bindQuestioViewModel(QuestionsViewModel questionsViewModel);
}
