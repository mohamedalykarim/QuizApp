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

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
    private int quizSize;
    private CountDownTimer countDownTimer;


    public static UserMainFragment newInstance() {
        return new UserMainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentUserMainBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this, viewmodelProviderFactory).get(UserMainViewModel.class);

        mViewModel.initForUser(binding.searchEt);

        binding.refresher.setRefreshing(true);


        binding.refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mViewModel.refresh();
                binding.refresher.setRefreshing(true);
                countDownTimer.start();
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
                if (quizItems == null){
                    binding.noContentContainer.setVisibility(View.VISIBLE);
                    return;
                }
                quizItems.addWeakCallback(null, new PagedList.Callback() {
                    @Override
                    public void onChanged(int position, int count) {

                    }

                    @Override
                    public void onInserted(int position, int count) {
                        quizSize = count;
                        binding.noContentContainer.setVisibility(View.GONE);
                        binding.refresher.setRefreshing(false);

                    }

                    @Override
                    public void onRemoved(int position, int count) {

                    }
                });

                adapter.submitList(quizItems);

            }
        });

        binding.showNavIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userMainFragmetnClickListener.onUserMainFragmetnClickListenerClicked(v.getId());
            }
        });

        countDownTimer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (quizSize == 0){
                    binding.noContentContainer.setVisibility(View.VISIBLE);
                    binding.refresher.setRefreshing(false);

                }
            }
        };
        countDownTimer.start();

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }

    public interface  UserMainFragmetnClickListener{
        void onUserMainFragmetnClickListenerClicked(int viewId);
    }



}
