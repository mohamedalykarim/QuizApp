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

    public void resetQuiz(final QuizItem quizItem) {

        appExecutor.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<QuestionItem> questionItems = quizRepository.getQuestionsFromInternal(quizItem.getId());
                for (QuestionItem questionItem: questionItems){
                    quizRepository.deleteAnswerFromInternal(questionItem.getId(), quizItem.getId());
                    quizRepository.deleteQuestionFromInternal(questionItem.getId(), quizItem.getId());
                }
                quizRepository.deleteSessionFromInternal(quizItem.getId());
                quizRepository.deleteSavedAnswersForQuiz(quizItem.getId());
            }
        });
    }

}
