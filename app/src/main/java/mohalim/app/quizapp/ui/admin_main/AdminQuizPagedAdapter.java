package mohalim.app.quizapp.ui.admin_main;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.utils.Constants;
import mohalim.app.quizapp.databinding.RowQuizBinding;
import mohalim.app.quizapp.ui.questions.QuestionsActivity;

public class AdminQuizPagedAdapter extends PagedListAdapter<QuizItem, AdminQuizPagedAdapter.MainViewHolder> {
    QuizPagedAdapterClick quizPagedAdapterClick;

    public AdminQuizPagedAdapter(Context context) {
        super(QuizItem.DIFF_CALLBACK);
        try {
            quizPagedAdapterClick = (QuizPagedAdapterClick) context;
        }catch (ClassCastException e){
            throw new ClassCastException("Activity must implements QuizPagedAdapterClick class");
        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowQuizBinding binding = RowQuizBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MainViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder, final int position) {
        holder.binding.setQuizItem(getItem(position));

        final QuizItem quizItem = getItem(position);

        holder.binding.questionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.binding.getRoot().getContext(), QuestionsActivity.class);
                intent.putExtra(Constants.QUIZ_ITEM, quizItem);
                holder.binding.getRoot().getContext().startActivity(intent);
            }
        });

        holder.binding.startQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quizPagedAdapterClick.onQuizPagedAdapterClick(Constants.START, getItem(position), position);
            }
        });

        holder.binding.editQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quizPagedAdapterClick.onQuizPagedAdapterClick(Constants.EDIT, getItem(position), position);
            }
        });

        holder.binding.peopleCanAccessIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizPagedAdapterClick.onQuizPagedAdapterClick(Constants.CLICK_TYPE_QUIZ_PEOPLE_ACCESS, getItem(position), position);
            }
        });

        holder.binding.statisticsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizPagedAdapterClick.onQuizPagedAdapterClick(Constants.CLICK_TYPE_QUIZ_Statistics, getItem(position), position);
            }
        });

    }

    class MainViewHolder extends RecyclerView.ViewHolder{
        RowQuizBinding binding;
        public MainViewHolder(@NonNull RowQuizBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface QuizPagedAdapterClick{
        void onQuizPagedAdapterClick(String clickType, QuizItem item, int position);
    }
}
