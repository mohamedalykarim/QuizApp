package mohalim.app.quizapp.ui.questions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import mohalim.app.quizapp.core.models.QuestionItem;
import mohalim.app.quizapp.core.utils.Constants;
import mohalim.app.quizapp.databinding.RowQuestionBinding;

public class QuestionPagedAdapter extends PagedListAdapter<QuestionItem, QuestionPagedAdapter.QuestionPagedAdapterViewHolder> {

    QuestionAdapterClickListener questionAdapterClickListener;



    protected QuestionPagedAdapter(Context context) {
        super(QuestionItem.DIFF_CALLBACK);
        try{
            questionAdapterClickListener = (QuestionAdapterClickListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException("Activity must implement QuestionAdapterClickListener class : "+ e.getMessage());
        }
    }


    @NonNull
    @Override
    public QuestionPagedAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowQuestionBinding binding = RowQuestionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new QuestionPagedAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionPagedAdapterViewHolder holder, final int position) {
        holder.binding.setQuestion(getItem(position));
        holder.binding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionAdapterClickListener.onQuestionAdapterClick(Constants.EDIT, getItem(position));
            }
        });

    }

    class QuestionPagedAdapterViewHolder extends RecyclerView.ViewHolder {
        private RowQuestionBinding binding;

        public QuestionPagedAdapterViewHolder(RowQuestionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }



    public interface QuestionAdapterClickListener{
        void onQuestionAdapterClick(String type, QuestionItem questionItem);
    }
}
