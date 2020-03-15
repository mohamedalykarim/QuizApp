package mohalim.app.quizapp.core.di.component;


import android.app.Application;


import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import mohalim.app.quizapp.core.di.base.BaseApplication;
import mohalim.app.quizapp.core.di.module.ActivityBuildersModule;
import mohalim.app.quizapp.core.di.module.AppModule;
import mohalim.app.quizapp.core.di.module.ServiceBuilderModule;
import mohalim.app.quizapp.core.di.module.ViewmodelFactoryModule;
import mohalim.app.quizapp.core.utils.ViewModelProviderFactory;

@Singleton
@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                ActivityBuildersModule.class,
                ServiceBuilderModule.class,
                AppModule.class,
                ViewmodelFactoryModule.class
        }
)
public interface AppComponent extends AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder{
        @BindsInstance
        Builder getInstance(Application application);

        AppComponent build();
    }
}
