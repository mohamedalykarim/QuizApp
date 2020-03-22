package mohalim.app.quizapp.ui.quiz;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import mohalim.app.quizapp.core.di.base.BaseFragment;
import mohalim.app.quizapp.core.models.AnswerItem;
import mohalim.app.quizapp.core.models.QuestionItem;
import mohalim.app.quizapp.core.utils.Constants;
import mohalim.app.quizapp.core.utils.ViewModelProviderFactory;
import mohalim.app.quizapp.databinding.FragmentQuizBinding;
import mohalim.app.quizapp.ui.result.ResultActivity;

public class QuizFragment extends BaseFragment {
    public static final String TAG = "QuizFragment";

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;
    private QuizViewModel mViewModel;
    private FragmentQuizBinding binding;

    private int currentQuizPosition;

    private ChangeQuizPosition changeQuizPosition;
    private ChangeNavItemColor changeNavItemColor;

    private boolean startResetQuiz;


    public static QuizFragment newInstance() {
        return new QuizFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentQuizBinding.inflate(inflater, container, false);


        Intent intent = getActivity().getIntent();
        if (intent == null || !intent.hasExtra(Constants.QUIZ_ITEM)){
            getActivity().finish();
        }

        mViewModel = new ViewModelProvider(getActivity(), viewModelProviderFactory).get(QuizViewModel.class);
        mViewModel.quizItem = intent.getParcelableExtra(Constants.QUIZ_ITEM);

        /**
         * Quiz time
         */
        Calendar calendar = Calendar.getInstance();

        long currentTimeMillisecond = calendar.getTimeInMillis();
        long timePassedMillisecond = currentTimeMillisecond - mViewModel.currentSession.getStartTime();
        long examTimeMillisecond = mViewModel.quizItem.getTimeInMinutes() * 60 * 1000;

        if (timePassedMillisecond > examTimeMillisecond){
            Toast.makeText(getContext(), "Exam time finished", Toast.LENGTH_SHORT).show();
        }

        long timeRemainsMillisecond = examTimeMillisecond - timePassedMillisecond;




        new CountDownTimer(timeRemainsMillisecond, 1000) {
            @Override
            public void onTick(long l) {
                long h = TimeUnit.MILLISECONDS.toHours(l);
                long m = TimeUnit.MILLISECONDS.toMinutes(l) - h*60;
                long s = TimeUnit.MILLISECONDS.toSeconds(l) - h*60*60 - m*60;

                String hour = String.valueOf(h);
                String minute = String.valueOf(m);
                String second = String.valueOf(s);

                if (hour.length() == 1) hour = "0"+hour;
                if (minute.length() == 1) minute = "0"+minute;
                if (second.length() == 1) second = "0"+second;

                binding.timeCounter.setText(hour+":"+minute+":"+second);


            }

            @Override
            public void onFinish() {
                binding.timeCounter.setText("00:00:00");

                if (mViewModel.quizItem.isSaveResults()){
                    mViewModel.startSaveResults();
                }

//                getActivity().finish();
            }
        }.start();


        if (mViewModel.quizItem.getQuizSwipeDirection().equals(Constants.RIGHT)){
            binding.getRoot().setRotation(180.0f);
        }

        if (mViewModel.quizItem.getQuizSwipeDirection().equals(Constants.RIGHT)){
            binding.bottomBtnsContainer.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }else{
            binding.bottomBtnsContainer.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        /**
         * Next button
         */

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int targeted = currentQuizPosition + 1;
                if (targeted >= mViewModel.questionItemList.size())return;
                changeQuizPosition.onChangeQuizPosition(targeted);
            }
        });

        /**
         * previous button
         */

        binding.previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int targeted = currentQuizPosition - 1;
                if (targeted < 0)return;
                changeQuizPosition.onChangeQuizPosition(targeted);            }
        });


        /**
         * check answer button
         */
        binding.checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = ((RadioGroup)binding.answersContainer).getCheckedRadioButtonId();
                if (id < 1)return;

                RadioButton radioButton = binding.getRoot().findViewById(id);
                boolean isTrue = (boolean) radioButton.getTag();

                if (isTrue){
                    Toast.makeText(getContext(), "Correct answer", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "Try again", Toast.LENGTH_SHORT).show();
                }
            }
        });


        /**
         * Finish button
         */


        binding.finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!mViewModel.quizItem.isShowResults()){

                    new AlertDialog.Builder(getContext())
                            .setTitle("Finish")
                            .setMessage("Are you sure! you want finish the exam?")
                            .setPositiveButton("Finish exam", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    mViewModel.resetQuiz(mViewModel.quizItem);
                                    startResetQuiz = true;

                                    if (mViewModel.quizItem.isSaveResults()){
                                        mViewModel.startSaveResults();
                                    }

                                    getActivity().finish();

                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }else {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Results!")
                            .setMessage("Do you want finish the session and go to results page ?")
                            .setPositiveButton("Finish and reset Session", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent intent = new Intent(getActivity(), ResultActivity.class);
                                    ArrayList<QuestionItem> questions = new ArrayList<>(mViewModel.questionItemList);
                                    intent.putParcelableArrayListExtra(Constants.QUESTION_ITEM, questions);
                                    intent.putExtra(Constants.QUIZ_ITEM, mViewModel.quizItem);
                                    getActivity().startActivity(intent);

                                    mViewModel.resetQuiz(mViewModel.quizItem);

                                    startResetQuiz = true;

                                    if (mViewModel.quizItem.isSaveResults()){
                                        mViewModel.startSaveResults();
                                    }

                                    getActivity().finish();

                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton("Finish", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Intent intent = new Intent(getActivity(), ResultActivity.class);
                                    ArrayList<QuestionItem> questions = new ArrayList<>(mViewModel.questionItemList);
                                    intent.putParcelableArrayListExtra(Constants.QUESTION_ITEM, questions);
                                    intent.putExtra(Constants.QUIZ_ITEM, mViewModel.quizItem);
                                    getActivity().startActivity(intent);

                                    if (mViewModel.quizItem.isSaveResults()){
                                        mViewModel.startSaveResults();
                                    }

                                    getActivity().finish();

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }



            }
        });



        if (currentQuizPosition == 0){
            binding.previousBtn.setVisibility(View.GONE);
            binding.finishBtn.setVisibility(View.GONE);
            binding.nextBtn.setVisibility(View.VISIBLE);
        }else if (currentQuizPosition == mViewModel.questionItemList.size()-1){
            binding.previousBtn.setVisibility(View.VISIBLE);
            binding.finishBtn.setVisibility(View.VISIBLE);
            binding.nextBtn.setVisibility(View.GONE);
        }else{
            binding.previousBtn.setVisibility(View.VISIBLE);
            binding.finishBtn.setVisibility(View.GONE);
            binding.nextBtn.setVisibility(View.VISIBLE);
        }

        if (mViewModel.questionItemList.size() == 1){
            binding.previousBtn.setVisibility(View.GONE);
            binding.finishBtn.setVisibility(View.VISIBLE);
            binding.nextBtn.setVisibility(View.GONE);
        }

        if (mViewModel.quizItem.isCheckAnswerWorking()){
            binding.checkBtn.setVisibility(View.VISIBLE);
        }else {
            binding.checkBtn.setVisibility(View.GONE);


        }


        new CountDownTimer(3000, 1000) {
            @Override public void onTick(long l) {
                if (currentQuizPosition == mViewModel.questionItemList.size())return;

                binding.questionTv.setText(mViewModel.questionItemList.get(currentQuizPosition).getQuestionText());
                binding.quizName.setText(mViewModel.quizItem.getQuizName());
                binding.questionCounterTv.setText("Question "+(currentQuizPosition+1)+" from "+mViewModel.questionItemList.size());

            }
            @Override public void onFinish() {
                binding.questionCounterTv.setText("Question "+(currentQuizPosition+1)+" from "+mViewModel.questionItemList.size());
            }
        }.start();

        setRetainInstance(true);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel.getAnswersForQuestionFromInternalObserved(mViewModel.questionItemList.get(currentQuizPosition).getId(), mViewModel.quizItem.getId())
                .observe(getViewLifecycleOwner(), new Observer<List<AnswerItem>>() {
                    @Override
                    public void onChanged(List<AnswerItem> answerItems) {
                        if (answerItems==null)return;
                        if (startResetQuiz)return;

                        binding.answersContainer.removeAllViews();
                        binding.answersLoading.setVisibility(View.GONE);

                        int i = 0;
                        for (AnswerItem answerItem: answerItems){
                            RadioButton radioButton = new RadioButton(getContext());
                            radioButton.setBackgroundColor(Color.parseColor("#dadada"));
                            radioButton.setText(answerItem.getAnswerText());
                            radioButton.setTag(answerItem.isCorrect());
                            RadioGroup.LayoutParams params
                                    = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.setMargins(0,10,0,0);
                            radioButton.setLayoutParams(params);
                            radioButton.setGravity(Gravity.CENTER_VERTICAL | Gravity.START );
                            final int finalI = i;
                            radioButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mViewModel.questionItemList.get(currentQuizPosition).setAnswered(true);
                                    mViewModel.questionItemList.get(currentQuizPosition).setChosenAnswer(finalI+1);
                                    if (view.getTag().equals(true)){
                                        mViewModel.questionItemList.get(currentQuizPosition).setChosenAnswerCorrect(true);
                                    }else {
                                        mViewModel.questionItemList.get(currentQuizPosition).setChosenAnswerCorrect(false);
                                    }
                                }
                            });

                            binding.answersContainer.addView(radioButton);

                            i++;
                        }

                        if (mViewModel.questionItemList.size() <= currentQuizPosition)return;
                        int index = mViewModel.questionItemList.get(currentQuizPosition).getChosenAnswer() - 1;
                        RadioButton radioButton = (RadioButton) binding.answersContainer.getChildAt(index);
                        if (radioButton != null && mViewModel.questionItemList.get(currentQuizPosition).getChosenAnswer() != 0)  radioButton.setChecked(true);
                    }
                });

    }

    @Override
    public void onResume() {
        super.onResume();

        changeNavItemColor.onChangeNavItemColor(currentQuizPosition);

    }

    @Override
    public void onPause() {
        super.onPause();

        mViewModel.getAnswersForQuestionFromInternalObserved(mViewModel.questionItemList.get(currentQuizPosition).getId(), mViewModel.quizItem.getId())
                .removeObservers(getViewLifecycleOwner());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            changeQuizPosition = (ChangeQuizPosition) context;
        }catch (ClassCastException e){
            throw new ClassCastException("Activity must implement ChangeQuizPosition class");
        }

        try {
            changeNavItemColor = (ChangeNavItemColor) context;
        }catch (ClassCastException e){
            throw new ClassCastException("Activity must implement ChangeNavItemColor class");
        }
    }

    public void setCurrentQuizPosition(int currentQuizPosition) {
        this.currentQuizPosition = currentQuizPosition;
    }



    public interface ChangeQuizPosition{
        void onChangeQuizPosition(int position);
    }

    public interface ChangeNavItemColor{
        void onChangeNavItemColor(int currentPosition);
    }
}
