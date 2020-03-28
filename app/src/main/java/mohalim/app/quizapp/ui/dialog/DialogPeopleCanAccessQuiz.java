package mohalim.app.quizapp.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.models.UserItem;
import mohalim.app.quizapp.core.utils.AppExecutor;
import mohalim.app.quizapp.core.utils.ViewModelProviderFactory;
import mohalim.app.quizapp.databinding.DialogPeopleCanAccessQuizBinding;
import mohalim.app.quizapp.ui.main.MainViewModel;
import mohalim.app.quizapp.ui.main.PeopleCanAccessAdapter;

public class DialogPeopleCanAccessQuiz extends DialogFragment implements HasAndroidInjector {
    private static final String TAG = "DialogPeopleCanAccessQuiz";
    @Inject
    DispatchingAndroidInjector<Object> androidInjector;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    AppExecutor appExecuter;

    private QuizItem quizItem;

    private DialogPeopleCanAccessQuizBinding binding;
    private MainViewModel mViewModel;

    private int addPersonFormErrors;
    private PeopleCanAccessAdapter peopleCanAccessAdapter;
    private OnDismissPeopleCanAccessDialog onDismissPeopleCanAccessDialog;


    public static DialogPeopleCanAccessQuiz newInstance() {

        Bundle args = new Bundle();

        DialogPeopleCanAccessQuiz fragment = new DialogPeopleCanAccessQuiz();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogPeopleCanAccessQuizBinding.inflate(inflater,container, false);
        mViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(MainViewModel.class);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        binding.quizNameTv.append(quizItem.getQuizName());

        // add member to quiz
        binding.addPersonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAddPersonForm();
                mViewModel.startAddUserAccessToQuiz(quizItem, binding.userNameEt.getText().toString());
                binding.userNameEt.setText("");
            }
        });

        // text listener for member search
        binding.userNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mViewModel.startGetPersonByUsername(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // recycler view handling
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        peopleCanAccessAdapter = new PeopleCanAccessAdapter((PeopleCanAccessAdapter.OnPeopleCanAccessAdapterClick) getActivity(), quizItem);
        binding.peopleCanAccessRV.setLayoutManager(linearLayoutManager);
        binding.peopleCanAccessRV.setAdapter(peopleCanAccessAdapter);

        mViewModel.startGetQuiz(quizItem.getId());


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // User search observation
        mViewModel.getUsersSearchObservation().observe(getViewLifecycleOwner(), new Observer<List<UserItem>>() {

            @Override
            public void onChanged(List<UserItem> userItems) {
                if (userItems == null)return;

                String[] users = new String[userItems.size()];

                int i = 0;
                for (UserItem user: userItems){
                    users[i] = user.getUserName();
                    i++;
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        getContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        users
                );

                binding.userNameEt.setAdapter(arrayAdapter);

            }
        });



        mViewModel.getQuizitemObservation().observe(getViewLifecycleOwner(), new Observer<QuizItem>() {
            @Override
            public void onChanged(final QuizItem quizItemObserved) {
                if (quizItemObserved == null){
                    return;
                };
                if (quizItemObserved.getPeopleCanAccess() == null){
                    peopleCanAccessAdapter.setUserItems(new ArrayList<UserItem>());
                    peopleCanAccessAdapter.notifyDataSetChanged();
                    return;
                }


                peopleCanAccessAdapter.setUserItems(quizItemObserved.getPeopleCanAccess());
                peopleCanAccessAdapter.notifyDataSetChanged();


            }
        });



    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(
                ScrollView.LayoutParams.MATCH_PARENT,
                ScrollView.LayoutParams.MATCH_PARENT
        );


    }


    private void validateAddPersonForm() {
        addPersonFormErrors = 0;

        if (binding.userNameEt.getText().toString().equals("")){
            binding.userNameEt.setError("Please enter correct email first");
            addPersonFormErrors++;
        }


    }



    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        mViewModel.getUsersSearchObservation().removeObservers(getViewLifecycleOwner());
        mViewModel.setUsersSearchObservation(null);

        mViewModel.getQuizitemObservation().removeObservers(getViewLifecycleOwner());
        mViewModel.setQuizitemObservation(null);

        onDismissPeopleCanAccessDialog.onDismissPeopleCanAccessDialog();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);

        try {
            onDismissPeopleCanAccessDialog = (OnDismissPeopleCanAccessDialog) context;
        }catch (ClassCastException e){
            throw new ClassCastException("Activity must implement OnDismissPeopleCanAccessDialog class " + e);
        }
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return androidInjector;
    }



    public void updateQuizPeopleCanAccess(QuizItem quizItem){
        this.quizItem = quizItem;
    }

    public interface OnDismissPeopleCanAccessDialog{
        void onDismissPeopleCanAccessDialog();
    }

}
