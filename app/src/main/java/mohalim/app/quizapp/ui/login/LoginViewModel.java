package mohalim.app.quizapp.ui.login;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

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
}
