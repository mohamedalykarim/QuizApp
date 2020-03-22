package mohalim.app.quizapp.ui.result;

import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.Reusable;
import mohalim.app.quizapp.core.models.AnswerItem;
import mohalim.app.quizapp.core.models.QuestionItem;
import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.repositories.QuizRepository;
import mohalim.app.quizapp.core.utils.AppExecutor;

@Reusable
public class ResultViewModel extends ViewModel {
    @Inject
    QuizRepository quizRepository;

    @Inject
    AppExecutor appExecutor;

    @Inject
    public ResultViewModel() {
    }

    public List<AnswerItem> getAnswersForQuestionFromInternal(String questionId, String quizId) {
       return quizRepository.getAnswersForQuestionFromInternal(questionId, quizId);
    }


}
