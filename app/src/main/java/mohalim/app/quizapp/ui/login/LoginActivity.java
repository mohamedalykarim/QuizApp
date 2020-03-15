package mohalim.app.quizapp.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import mohalim.app.quizapp.R;
import mohalim.app.quizapp.core.di.base.BaseActivity;


public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, LoginFragment.newInstance())
                    .commitNow();
        }
    }
}
