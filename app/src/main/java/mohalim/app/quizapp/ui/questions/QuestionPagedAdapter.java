package mohalim.app.quizapp.ui.questions;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import mohalim.app.quizapp.core.models.QuestionItem;
import mohalim.app.quizapp.databinding.RowQuestionBinding;

public class QuestionPagedAdapter extends PagedListAdapter<QuestionItem, QuestionPagedAdapter.QuestionPagedAdapterViewHolder> {

    protected QuestionPagedAdapter() {
        super(QuestionItem.DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public QuestionPagedAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowQuestionBinding binding = RowQuestionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new QuestionPagedAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionPagedAdapterViewHolder holder, int position) {
        holder.binding.setQuestion(getItem(position));
    }

    class QuestionPagedAdapterViewHolder extends RecyclerView.ViewHolder {
        private RowQuestionBinding binding;

        public QuestionPagedAdapterViewHolder(RowQuestionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
