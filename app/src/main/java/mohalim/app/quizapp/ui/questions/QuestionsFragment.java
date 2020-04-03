package mohalim.app.quizapp.ui.questions;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
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

import javax.inject.Inject;

import mohalim.app.quizapp.core.di.base.BaseFragment;
import mohalim.app.quizapp.core.models.QuestionItem;
import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.utils.Constants;
import mohalim.app.quizapp.core.utils.ViewModelProviderFactory;
import mohalim.app.quizapp.databinding.FragmentQuestionsBinding;


public class QuestionsFragment extends BaseFragment {
    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    QuestionPagedAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    private QuestionsViewModel mViewModel;
    private FragmentQuestionsBinding binding;
    private AddQuestionBottomSheert addQuestionBottomSheet;
    private QuizItem quizItem;
    private int questionsSize;
    private CountDownTimer countDownTimer;

    public static QuestionsFragment newInstance() {
        return new QuestionsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        if (!intent.hasExtra(Constants.QUIZ_ITEM)){
            getActivity().finish();
        }
        quizItem = intent.getParcelableExtra(Constants.QUIZ_ITEM);


        binding = FragmentQuestionsBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(QuestionsViewModel.class);
        mViewModel.init(quizItem.getId(), binding.searchEt );
        binding.refresher.setRefreshing(true);

        binding.refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mViewModel.refresh(); }
        });


        addQuestionBottomSheet = new AddQuestionBottomSheert();

        binding.addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addQuestionBottomSheet.isAdded()) return;
                addQuestionBottomSheet.show(getActivity().getSupportFragmentManager(), "AddQuestionBottomSheert");
            }
        });

        binding.addNewQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addQuestionBottomSheet.isAdded()) return;
                addQuestionBottomSheet.show(getActivity().getSupportFragmentManager(), "AddQuestionBottomSheert");
            }
        });

        linearLayoutManager = new LinearLayoutManager(getContext());
        adapter = new QuestionPagedAdapter(getContext());

        binding.questionsRv.setLayoutManager(linearLayoutManager);
        binding.questionsRv.setHasFixedSize(true);
        binding.questionsRv.setAdapter(adapter);



        mViewModel.getQuestionsLiveData().observe(getViewLifecycleOwner(), new Observer<PagedList<QuestionItem>>() {
            @Override
            public void onChanged(PagedList<QuestionItem> questionItems) {
                if (questionItems == null)return;
                adapter.submitList(questionItems);

                questionItems.addWeakCallback(null, new PagedList.Callback() {
                    @Override
                    public void onChanged(int position, int count) {

                    }

                    @Override
                    public void onInserted(int position, int count) {
                        binding.refresher.setRefreshing(false);
                        binding.noContentContainer.setVisibility(View.GONE);
                        questionsSize = count;
                    }

                    @Override
                    public void onRemoved(int position, int count) {

                    }
                });

            }
        });

        countDownTimer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (questionsSize == 0){
                    binding.noContentContainer.setVisibility(View.VISIBLE);
                    binding.refresher.setRefreshing(false);
                }
                questionsSize =0;
            }
        };
        countDownTimer.start();

        return binding.getRoot();
    }


    public void onAddNewQuestion() {
        mViewModel.refresh();
    }
}
