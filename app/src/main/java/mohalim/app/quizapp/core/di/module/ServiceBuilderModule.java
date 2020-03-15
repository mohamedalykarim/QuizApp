package mohalim.app.quizapp.core.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mohalim.app.quizapp.core.services.AppService;

@Module
public abstract class ServiceBuilderModule {
    @ContributesAndroidInjector
    abstract AppService contributeAppService();
}
