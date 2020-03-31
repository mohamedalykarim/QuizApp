package mohalim.app.quizapp.ui.user_main;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import mohalim.app.quizapp.core.di.base.BaseFragment;
import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.utils.ViewModelProviderFactory;
import mohalim.app.quizapp.databinding.FragmentUserMainBinding;
import mohalim.app.quizapp.ui.admin_main.AdminQuizPagedAdapter;


public class UserMainFragment extends BaseFragment {
    private static final String TAG = "UserMainFragment";

    @Inject
    ViewModelProviderFactory viewmodelProviderFactory;

    private UserMainViewModel mViewModel;
    private FragmentUserMainBinding binding;

    UserMainFragmetnClickListener userMainFragmetnClickListener;
    private UserQuizPagedAdapter adapter;
    private int accessErrors;


    public static UserMainFragment newInstance() {
        return new UserMainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentUserMainBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this, viewmodelProviderFactory).get(UserMainViewModel.class);

        if (!mViewModel.getCurrentUser().getIsAdmin()){
            mViewModel.initForUser(binding.searchEt);
        }

        binding.refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mViewModel.refresh();
                binding.refresher.setRefreshing(false);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        adapter = new UserQuizPagedAdapter(getActivity());

        binding.mainRV.setLayoutManager(linearLayoutManager);
        binding.mainRV.setAdapter(adapter);
        binding.mainRV.setHasFixedSize(true);

        mViewModel.getQuizLiveData().observe(getViewLifecycleOwner(), new Observer<PagedList<QuizItem>>() {
            @Override
            public void onChanged(PagedList<QuizItem> quizItems) {
                if (quizItems == null) return;
                adapter.submitList(quizItems);
            }
        });

        binding.showNavIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userMainFragmetnClickListener.onUserMainFragmetnClickListenerClicked(v.getId());
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
