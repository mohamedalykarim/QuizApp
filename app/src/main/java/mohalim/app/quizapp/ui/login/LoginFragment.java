package mohalim.app.quizapp.ui.login;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import javax.inject.Inject;

import mohalim.app.quizapp.R;
import mohalim.app.quizapp.core.di.base.BaseFragment;
import mohalim.app.quizapp.core.models.UserItem;
import mohalim.app.quizapp.core.utils.AppExecutor;
import mohalim.app.quizapp.core.utils.Constants;
import mohalim.app.quizapp.core.utils.ViewModelProviderFactory;
import mohalim.app.quizapp.databinding.FragmentLoginBinding;
import mohalim.app.quizapp.ui.admin_main.AdminMainActivity;
import mohalim.app.quizapp.ui.user_main.UserMainActivity;


public class LoginFragment extends BaseFragment {
    FragmentLoginBinding binding;
    private LoginViewModel mViewModel;



    private GoogleSignInApi mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;
    GoogleSignInOptions gso;

    private FirebaseAuth mAuth;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    AppExecutor appExecutor;


    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(getActivity(), viewModelProviderFactory).get(LoginViewModel.class);

        mAuth = FirebaseAuth.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        final GoogleSignInClient googleApiClient = GoogleSignIn.getClient(getActivity(), gso);


        binding.googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleApiClient.signOut();
                    Intent intent = googleApiClient.getSignInIntent();
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });

        mViewModel.getCurrentUserDetailsObservation().observe((LifecycleOwner) getContext(), new Observer<UserItem>() {
            @Override
            public void onChanged(UserItem userItem) {
                if (userItem == null)return;
                if (userItem.getIsAdmin()){
                    startUserActivity(UserMainActivity.class, userItem);
                }else {
                    startUserActivity(UserMainActivity.class, userItem);
                }

                mViewModel.setCurrentUserDetailsObservation(null);
                mViewModel.getCurrentUserDetailsObservation().removeObserver(this);
            }
        });


        return binding.getRoot();
    }

    private void startUserActivity(Class<?> activity, UserItem userItem) {
        Intent intent = new Intent(getActivity(), activity);
        intent.putExtra(Constants.USER_ITEM, userItem);
        getActivity().startActivity(intent);
        Toast.makeText(getContext(), "Welcome " + mAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
        mViewModel.startSaveUserData();
        getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mAuth.getCurrentUser() != null){
            Intent intent = new Intent(getActivity(), AdminMainActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
                // ...
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            mViewModel.startGetCurrentUserData();



                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("TAG", "signInWithCredential:falure");

                        }

                        // ...
                    }
                });
    }





}
