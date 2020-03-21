package mohalim.app.quizapp.ui.feedback;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.Reusable;
import mohalim.app.quizapp.core.models.FeedBackItem;
import mohalim.app.quizapp.core.repositories.QuizRepository;

@Reusable
public class FeedBackViewModel extends ViewModel {
    @Inject
    QuizRepository quizRepository;

    FeedBackItem feedBackItem;

    @Inject
    public FeedBackViewModel() {
    }

    public void startAddFeedBack(FeedBackItem feedBackItem) {
        this.quizRepository.startAddFeedBack(feedBackItem);

    }

    public void startGetMyFeedBack() {
        this.quizRepository.startGetMyFeedBack();
    }

    public void startGetRandomFeedback(int count) {
        this.quizRepository.startGetRandomFeedback(count);
    }

    public MutableLiveData<FeedBackItem> getMyFeedBackObservation() {
        return quizRepository.getMyFeedBackObservation();
    }


    public void setMyFeedBackObservation(FeedBackItem feedBackItem) {
        this.quizRepository.setMyFeedBackObservation(feedBackItem);
    }

    public MutableLiveData<List<FeedBackItem>> getRandomFeedBack() {
        return quizRepository.getRandomFeedBack();
    }

    public void setRandomFeedBack(List<FeedBackItem> randomFeedBack) {
        this.quizRepository.setRandomFeedBack(randomFeedBack);
    }
}
