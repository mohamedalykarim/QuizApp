package mohalim.app.quizapp.ui.user_main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.utils.Constants;
import mohalim.app.quizapp.databinding.RowQuizBinding;
import mohalim.app.quizapp.databinding.RowUserQuizBinding;
import mohalim.app.quizapp.ui.admin_main.AdminQuizPagedAdapter;

public class UserQuizPagedAdapter extends PagedListAdapter<QuizItem, UserQuizPagedAdapter.MainViewHolder> {

    UserQuizPagedAdapterClick userQuizPagedAdapterClick;

    public UserQuizPagedAdapter(Context context) {
        super(QuizItem.DIFF_CALLBACK);
        try {
            userQuizPagedAdapterClick = (UserQuizPagedAdapterClick) context;
        }catch (ClassCastException e){
            throw new ClassCastException("Activity must implements UserQuizPagedAdapterClick class");
        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowUserQuizBinding binding = RowUserQuizBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MainViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, final int position) {
        holder.binding.setQuizItem(getItem(position));

        holder.binding.startQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userQuizPagedAdapterClick.onUserQuizPagedAdapterClick(Constants.START, getItem(position), position);
            }
        });
    }

    class MainViewHolder extends RecyclerView.ViewHolder{
        RowUserQuizBinding binding;
        public MainViewHolder(@NonNull RowUserQuizBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface UserQuizPagedAdapterClick{
        void onUserQuizPagedAdapterClick(String clickType, QuizItem item, int position);
    }
}
