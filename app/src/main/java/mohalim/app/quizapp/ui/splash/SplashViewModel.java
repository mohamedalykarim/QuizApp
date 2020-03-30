package mohalim.app.quizapp.ui.splash;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import mohalim.app.quizapp.core.models.UserItem;
import mohalim.app.quizapp.core.repositories.QuizRepository;

public class SplashViewModel extends ViewModel {
    @Inject
    QuizRepository quizRepository;

    @Inject
    public SplashViewModel() {
    }


    public void startGetCurrentUser() {
        this.quizRepository.startGetGetCurrentUserDetails();
    }

    public MutableLiveData<UserItem> getCurrentUserDetailsObservation() {
        return this.quizRepository.getCurrentUserDetailsObservation();
    }

    public void setCurrentUserDetailsObservation(UserItem currentUserDetails) {
        this.quizRepository.setCurrentUserDetailsObservation(currentUserDetails);
    }
}
