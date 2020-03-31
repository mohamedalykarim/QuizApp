package mohalim.app.quizapp.ui.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import mohalim.app.quizapp.core.models.UserItem;
import mohalim.app.quizapp.core.repositories.QuizRepository;

public class LoginViewModel extends ViewModel {

    @Inject
    QuizRepository quizRepository;

    @Inject
    public LoginViewModel() {
    }

    public void startSaveUserData() {
        quizRepository.startSaveUserData();
    }

    public void startGetCurrentUserData() {
        quizRepository.startGetCurrentUserDetails();
    }

    public MutableLiveData<UserItem> getCurrentUserDetailsObservation() {
        return quizRepository.getCurrentUserDetailsObservation();
    }

    public void setCurrentUserDetailsObservation(UserItem currentUserDetails) {
        this.quizRepository.setCurrentUserDetailsObservation(currentUserDetails);
    }
}
