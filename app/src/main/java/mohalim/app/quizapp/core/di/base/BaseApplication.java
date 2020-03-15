package mohalim.app.quizapp.core.di.base;

import android.app.Application;
import android.content.ContextWrapper;

import com.pixplicity.easyprefs.library.Prefs;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import mohalim.app.quizapp.core.di.component.DaggerAppComponent;
import mohalim.app.quizapp.core.utils.Constants;


public class BaseApplication extends DaggerApplication {

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        DaggerAppComponent.builder().getInstance((Application) getApplicationContext()).build().inject(this);

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(Constants.PREFERENCE_NAME)
                .setUseDefaultSharedPreference(true)
                .build();

        return DaggerAppComponent.builder().getInstance(this).build();
    }


}
