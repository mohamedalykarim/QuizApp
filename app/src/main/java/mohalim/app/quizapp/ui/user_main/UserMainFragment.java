package mohalim.app.quizapp.ui.user_main;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import mohalim.app.quizapp.core.di.base.BaseFragment;
import mohalim.app.quizapp.core.utils.ViewModelProviderFactory;
import mohalim.app.quizapp.databinding.FragmentUserMainBinding;


public class UserMainFragment extends BaseFragment {

    @Inject
    ViewModelProviderFactory viewmodelProviderFactory;


    private UserMainViewModel mViewModel;
    private FragmentUserMainBinding binding;

    UserMainFragmetnClickListener userMainFragmetnClickListener;


    public static UserMainFragment newInstance() {
        return new UserMainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentUserMainBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this, viewmodelProviderFactory).get(UserMainViewModel.class);

        binding.showNavIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userMainFragmetnClickListener.onUserMainFragmetnClickListenerClicked(v.getId());
            }
        });

        Toast.makeText(getContext(), "user fragment", Toast.LENGTH_SHORT).show();
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            userMainFragmetnClickListener = (UserMainFragmetnClickListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException("Activity must implements UserMainFragmetnClickListener class "+ e.getMessage());
        }
    }

    public interface  UserMainFragmetnClickListener{
        void onUserMainFragmetnClickListenerClicked(int viewId);
    }



}
