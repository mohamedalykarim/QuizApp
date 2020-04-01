package mohalim.app.quizapp.ui.splash;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import mohalim.app.quizapp.core.di.base.BaseFragment;
import mohalim.app.quizapp.core.models.UserItem;
import mohalim.app.quizapp.core.utils.AppExecutor;
import mohalim.app.quizapp.core.utils.Constants;
import mohalim.app.quizapp.core.utils.ViewModelProviderFactory;
import mohalim.app.quizapp.databinding.FragmentSplashBinding;
import mohalim.app.quizapp.ui.login.LoginActivity;
import mohalim.app.quizapp.ui.admin_main.AdminMainActivity;
import mohalim.app.quizapp.ui.user_main.UserMainActivity;


public class SplashFragment extends BaseFragment {
    FragmentSplashBinding binding;

    @Inject
    FirebaseAuth mAuth;

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
        if (mAuth.getCurrentUser() == null){

            new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    if (getActivity() == null) return;

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }
            }.start();
        }else{
            mViewModel.startGetCurrentUser();
        }

        mViewModel.getCurrentUserDetailsObservation().observe(getViewLifecycleOwner(), new Observer<UserItem>() {
            @Override
            public void onChanged(UserItem userItem) {
                if (userItem == null)return;

                if (userItem.getIsAdmin()){
                    mViewModel.setCurrentUserDetailsObservation(null);
                    mViewModel.getCurrentUserDetailsObservation().removeObservers((LifecycleOwner) getContext());

                    Intent intent = new Intent(getActivity(), UserMainActivity.class);
                    intent.putExtra(Constants.USER_ITEM, userItem);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }else{
                    mViewModel.setCurrentUserDetailsObservation(null);
                    mViewModel.getCurrentUserDetailsObservation().removeObservers((LifecycleOwner) getContext());

                    Intent intent = new Intent(getActivity(), UserMainActivity.class);
                    intent.putExtra(Constants.USER_ITEM, userItem);
                    getActivity().startActivity(intent);
                    getActivity().finish();

                }


            }
        });



        return binding.getRoot();
    }

}
