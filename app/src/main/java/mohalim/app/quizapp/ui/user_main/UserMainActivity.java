package mohalim.app.quizapp.ui.user_main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import javax.inject.Inject;

import mohalim.app.quizapp.R;
import mohalim.app.quizapp.core.di.base.BaseActivity;
import mohalim.app.quizapp.core.models.UserItem;
import mohalim.app.quizapp.core.utils.Constants;
import mohalim.app.quizapp.core.utils.ViewModelProviderFactory;
import mohalim.app.quizapp.databinding.ActivityUserMainBinding;


public class UserMainActivity extends BaseActivity
implements UserMainFragment.UserMainFragmetnClickListener {
    @Inject
    ViewModelProviderFactory viewmodelProviderFactory;

    private ActivityUserMainBinding binding;
    private UserMainViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_main);

        mViewModel = new ViewModelProvider(this, viewmodelProviderFactory).get(UserMainViewModel.class);

        Intent oldIntent = getIntent();
        if (oldIntent.hasExtra(Constants.USER_ITEM)){
            mViewModel.setCurrentUser((UserItem) oldIntent.getParcelableExtra(Constants.USER_ITEM));
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, UserMainFragment.newInstance())
                    .commitNow();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        initNavigation(binding.drawerLayout);
    }

    @Override
    public void onUserMainFragmetnClickListenerClicked(int viewId) {
        if (viewId == R.id.showNavIcon){
            binding.drawerLayout.openDrawer(GravityCompat.START);
        }
    }
}
