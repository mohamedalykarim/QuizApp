package mohalim.app.quizapp.ui.feedback;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import javax.inject.Inject;

import mohalim.app.quizapp.R;
import mohalim.app.quizapp.core.di.base.BaseFragment;
import mohalim.app.quizapp.core.models.FeedBackItem;
import mohalim.app.quizapp.core.utils.ViewModelProviderFactory;
import mohalim.app.quizapp.databinding.FragmentFeedBackBinding;

public class FeedBackFragment extends BaseFragment {
    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    FirebaseAuth mAuth;

    private FeedBackViewModel mViewModel;
    FragmentFeedBackBinding binding;

    private int addReviewFormError;

    public static FeedBackFragment newInstance() {
        return new FeedBackFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFeedBackBinding.inflate(
                inflater,
                container,
                false
        );

        mAuth = FirebaseAuth.getInstance();

        mViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(FeedBackViewModel.class);
        binding.setMyFeedback(mViewModel.feedBackItem);
        mViewModel.startGetMyFeedBack();
        mViewModel.startGetRandomFeedback(5);



        binding.addFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAddReviewForm();
                if (addReviewFormError > 0)return;
                String username = mAuth.getCurrentUser().getEmail().replace("@gmail.com","");

                FeedBackItem feedBackItem = new FeedBackItem();
                feedBackItem.setFeedBackText(binding.feedbackEt.getText().toString());
                feedBackItem.setName(mAuth.getCurrentUser().getDisplayName());
                feedBackItem.setUserId(mAuth.getCurrentUser().getUid());
                feedBackItem.setUserName(username);
                feedBackItem.setReviewed(false);

                View feedbackView = getLayoutInflater().inflate(R.layout.row_feedback, binding.feedbacksContainer, false);
                TextView title = feedbackView.findViewById(R.id.title);
                TextView text = feedbackView.findViewById(R.id.text);

                title.setText(feedBackItem.getName());
                text.setText(feedBackItem.getFeedBackText());

                binding.feedbacksContainer.addView(feedbackView);

                binding.feedbackEt.setText("");

                new CountDownTimer(2000, 1000) {
                    @Override public void onTick(long millisUntilFinished) {}
                    @Override public void onFinish() { mViewModel.startGetMyFeedBack(); }
                }.start();

                mViewModel.startAddFeedBack(feedBackItem);
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel.getMyFeedBackObservation().observe(getViewLifecycleOwner(), new Observer<FeedBackItem>() {
            @Override
            public void onChanged(FeedBackItem feedBackItem) {
                mViewModel.feedBackItem = feedBackItem;
                binding.setMyFeedback(feedBackItem);
            }
        });

        mViewModel.getRandomFeedBack().observe(getViewLifecycleOwner(), new Observer<List<FeedBackItem>>() {
            @Override
            public void onChanged(List<FeedBackItem> feedBackItems) {
                if (feedBackItems == null)return;

                binding.feedbacksContainer.removeAllViews();

                for (FeedBackItem feedBackItem : feedBackItems){
                    View feedbackView = getLayoutInflater().inflate(R.layout.row_feedback, binding.feedbacksContainer, false);
                    TextView title = feedbackView.findViewById(R.id.title);
                    TextView text = feedbackView.findViewById(R.id.text);

                    title.setText(feedBackItem.getName());
                    text.setText(feedBackItem.getFeedBackText());

                    binding.feedbacksContainer.addView(feedbackView);
                }
            }
        });

    }

    private void validateAddReviewForm() {
        addReviewFormError = 0;
        if (binding.feedbackEt.getText().toString().equals("")){
            binding.feedbackEt.setError("Please enter review first");
            addReviewFormError++;
        }

    }

    @Override
    public void onStop() {
        super.onStop();

        mViewModel.setMyFeedBackObservation(null);
        mViewModel.setRandomFeedBack(null);

        mViewModel.getMyFeedBackObservation().removeObservers(getViewLifecycleOwner());
        mViewModel.getRandomFeedBack().removeObservers(getViewLifecycleOwner());
    }
}
