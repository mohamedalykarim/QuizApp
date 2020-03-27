package mohalim.app.quizapp.ui.splash;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import mohalim.app.quizapp.R;
import mohalim.app.quizapp.core.di.base.BaseFragment;
import mohalim.app.quizapp.core.utils.AppExecutor;
import mohalim.app.quizapp.core.utils.ViewModelProviderFactory;
import mohalim.app.quizapp.databinding.FragmentSplashBinding;
import mohalim.app.quizapp.ui.login.LoginActivity;
import mohalim.app.quizapp.ui.main.MainActivity;


public class SplashFragment extends BaseFragment {
    FragmentSplashBinding binding;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    AppExecutor appExecutor;


    private SplashViewModel mViewModel;

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentSplashBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(SplashViewModel.class);

        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                if (getActivity() == null) return;

                Intent intent;
                if (FirebaseAuth.getInstance().getCurrentUser() == null){
                    intent = new Intent(getActivity(), LoginActivity.class);
                }else{
                    intent = new Intent(getActivity(), MainActivity.class);
                }
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        }.start();

        return binding.getRoot();
    }


}
