package mohalim.app.quizapp.ui.admin_main;

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

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import mohalim.app.quizapp.core.di.base.BaseFragment;
import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.utils.ViewModelProviderFactory;
import mohalim.app.quizapp.databinding.FragmentAdminMainBinding;


public class AdminMainFragment extends BaseFragment {
    private static final String TAG = "MainFragment";
    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    private AdminMainViewModel mViewModel;
    FragmentAdminMainBinding binding;
    AddQuizBottomSheet addQuizBottomSheet;
    UpdateQuizBottomSheet updateQuizBottomSheet;

    private QuizPagedAdapter adapter;
    private int accessErrors;

    MainFragmentClick mainFragmentClick;

    public static AdminMainFragment newInstance() {
        return new AdminMainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminMainBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(AdminMainViewModel.class);

        if (mViewModel.getCurrentUser().getIsAdmin()){
            mViewModel.initForAdmin(binding.searchEt);
        }else {
            mViewModel.initForUser(binding.searchEt);
        }

        addQuizBottomSheet = new AddQuizBottomSheet();
        updateQuizBottomSheet = new UpdateQuizBottomSheet();

        binding.refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mViewModel.refresh();
                binding.refresher.setRefreshing(false);
            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        adapter = new QuizPagedAdapter(getActivity());

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


        binding.addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addQuizBottomSheet.isAdded()){
                    addQuizBottomSheet.dismiss();
                }

                addQuizBottomSheet.show(getActivity().getSupportFragmentManager(), "AddQuizBottom");
            }
        });

        binding.showNavIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mainFragmentClick.onMainFragmentClick(v.getId());
            }
        });

        binding.accessQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAccessForm();
                if (accessErrors > 0)return;
                mViewModel.startAccessQuiz(binding.accessQuizEt.getText().toString());
                binding.mainRV.invalidate();

            }
        });

        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel.getAccessedQuiz().observe(getViewLifecycleOwner(), new Observer<QuizItem>() {
            @Override
            public void onChanged(final QuizItem quizItem) {
                if (quizItem== null)return;

                new AlertDialog.Builder(getContext())
                        .setTitle("Access quiz")
                        .setMessage("You want to access quiz : "+ quizItem.getQuizName())
                        .setPositiveButton("Access", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mViewModel.initSession(quizItem);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                mViewModel.setAccessedQuiz(null);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mainFragmentClick = (MainFragmentClick) context;
        }catch (ClassCastException e){
            throw new ClassCastException("Activity must implements MainFragmentClick class "+ e.getMessage());
        }
    }

    private void validateAccessForm() {
        if (binding.accessQuizEt.getText().toString().equals("")){
            binding.accessQuizEt.setError("Please enter quiz id");
            accessErrors++;
        }
    }


    public void onAddNewQuiz() {
        mViewModel.refresh();
    }

    public void onQuizPagedAdapterClick(QuizItem item) {
        updateQuizBottomSheet.setOldQuizItem(item);
        if (updateQuizBottomSheet.isAdded())return;
        updateQuizBottomSheet.show(getActivity().getSupportFragmentManager(), "UpdateQuizBottomSheet");
    }

    public void onUpdateQuiz() {
        mViewModel.refresh();
    }

    public interface MainFragmentClick{
        void onMainFragmentClick(int id);
    }
}
