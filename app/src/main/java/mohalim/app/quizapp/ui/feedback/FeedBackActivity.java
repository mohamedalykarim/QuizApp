package mohalim.app.quizapp.ui.feedback;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import mohalim.app.quizapp.R;
import mohalim.app.quizapp.core.di.base.BaseActivity;


public class FeedBackActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, FeedBackFragment.newInstance())
                    .commitNow();
        }
    }
}
