package mohalim.app.quizapp.core.di.module;

import android.app.Application;
import android.content.ContextWrapper;
import android.os.CountDownTimer;

import com.google.firebase.auth.FirebaseAuth;
import com.pixplicity.easyprefs.library.Prefs;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mohalim.app.quizapp.core.firebase.QuizFirebaseHandler;
import mohalim.app.quizapp.core.repositories.QuizRepository;
import mohalim.app.quizapp.core.utils.AppExecutor;
import mohalim.app.quizapp.core.utils.Constants;

@Module
public class AppModule {

    @Singleton
    @Provides
    static AppExecutor provideAppExecuter(){
        return AppExecutor.getInstance();
    }


    @Singleton
    @Provides
    static QuizFirebaseHandler provideQuizFirebaseHandler(Application application, AppExecutor appExecutor){
        return new QuizFirebaseHandler(application, appExecutor);
    }

    @Singleton
    @Provides
    static QuizRepository provideQuizRepository(QuizFirebaseHandler quizFirebaseHandler, Application application){
        return new QuizRepository(quizFirebaseHandler, application);
    }

    @Singleton
    @Provides
    static FirebaseAuth provideFirebaseAuth(){
        return FirebaseAuth.getInstance();
    }


}
