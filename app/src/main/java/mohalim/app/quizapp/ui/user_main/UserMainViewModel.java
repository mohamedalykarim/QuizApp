package mohalim.app.quizapp.ui.user_main;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.Reusable;
import mohalim.app.quizapp.core.models.UserItem;

@Reusable
public class UserMainViewModel extends ViewModel {

    UserItem currentUser;

    @Inject
    public UserMainViewModel() {
    }

    public UserItem getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserItem currentUser) {
        this.currentUser = currentUser;
    }
}
