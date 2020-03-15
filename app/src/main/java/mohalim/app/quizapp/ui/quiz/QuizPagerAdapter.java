package mohalim.app.quizapp.ui.quiz;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;

import mohalim.app.quizapp.core.models.QuestionItem;
import mohalim.app.quizapp.core.models.QuizItem;

public class QuizPagerAdapter extends androidx.fragment.app.FragmentPagerAdapter {
    List<QuestionItem> questionItems;
    QuizViewModel quizViewModel;
    QuizItem quizItem;

    public QuizPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        QuizFragment quizFragment = QuizFragment.newInstance();
        quizFragment.setCurrentQuizPosition(position);
        return quizFragment;
    }

    @Override
    public int getCount() {
        return questionItems == null ? 0 : questionItems.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    public void setQuestionItems(List<QuestionItem> questionItems) {
        this.questionItems = questionItems;
    }

    public void setQuizItem(QuizItem quizItem) {
        this.quizItem = quizItem;
    }

}
