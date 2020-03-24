package mohalim.app.quizapp.ui.statistics;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import mohalim.app.quizapp.core.models.QuizItem;

public class StatisticsViewModel extends ViewModel {

    QuizItem quizItem;

    @Inject
    public StatisticsViewModel() {
    }
}
