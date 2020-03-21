package mohalim.app.quizapp.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.models.UserItem;
import mohalim.app.quizapp.databinding.RowPeopleCanAccessQuizBinding;

public class PeopleCanAccessAdapter extends RecyclerView.Adapter<PeopleCanAccessAdapter.PeopleCanAccessAdapterViewHolder>{

    List<UserItem> userItems;
    OnPeopleCanAccessAdapterClick onPeopleCanAccessAdapterClick;
    QuizItem quizItem;

    public PeopleCanAccessAdapter(OnPeopleCanAccessAdapterClick onPeopleCanAccessAdapterClick, QuizItem quizItem) {
        this.onPeopleCanAccessAdapterClick = onPeopleCanAccessAdapterClick;
        this.quizItem = quizItem;
    }

    @NonNull
    @Override
    public PeopleCanAccessAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowPeopleCanAccessQuizBinding binding = RowPeopleCanAccessQuizBinding.inflate(
          LayoutInflater.from(parent.getContext()),
          parent,
          false
        );

        return new PeopleCanAccessAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleCanAccessAdapterViewHolder holder, final int position) {
        holder.binding.userNameTv.setText(
                userItems.get(position).getDisplayedName()
                + "   " +
                userItems.get(position).getUserName()
        );

        holder.binding.imageView15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userItems.size() == 0)return;
                userItems.remove(position);
                quizItem.setPeopleCanAccess(userItems);
                onPeopleCanAccessAdapterClick.onPeopleCanAccessAdapterClick(quizItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userItems == null ? 0 : userItems.size();
    }

    class PeopleCanAccessAdapterViewHolder extends RecyclerView.ViewHolder{

        private final RowPeopleCanAccessQuizBinding binding;

        public PeopleCanAccessAdapterViewHolder(@NonNull RowPeopleCanAccessQuizBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setUserItems(List<UserItem> userItems) {
        this.userItems = userItems;
    }

    public interface OnPeopleCanAccessAdapterClick{
        void onPeopleCanAccessAdapterClick(QuizItem quizItem);
    }
}
