package mohalim.app.quizapp.ui.statistics;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.Reusable;
import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.models.ResultItem;
import mohalim.app.quizapp.core.repositories.QuizRepository;

@Reusable
public class StatisticsViewModel extends ViewModel {
    public List<ResultItem> results;
    public QuizItem quizItem;

    @Inject
    QuizRepository quizRepository;


    @Inject
    public StatisticsViewModel() {
    }

    public void startGetQuizResults(String quizId) {
        this.quizRepository.startGetQuizResults(quizId);
    }

    public MutableLiveData<List<ResultItem>> getResultsObservation() {
        return this.quizRepository.getResultsObservation();
    }

    public void setResultsObservation(List<ResultItem> results) {
        this.quizRepository.setResultsObservation(results);
    }
}
