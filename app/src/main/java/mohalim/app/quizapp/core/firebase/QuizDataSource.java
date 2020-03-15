package mohalim.app.quizapp.core.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.paging.PageKeyedDataSource;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


import mohalim.app.quizapp.core.models.QuizItem;

public class QuizDataSource extends PageKeyedDataSource<String, QuizItem> {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    public QuizDataSource() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull final LoadInitialCallback<String, QuizItem> callback) {

        db.collection("quiz")
                .whereEqualTo("owner", mAuth.getUid())
                .orderBy("id")
                .startAt(0)
                .limit(params.requestedLoadSize)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) return;
                        List<QuizItem> quizItems = new ArrayList<>();

                        for (DocumentSnapshot child :queryDocumentSnapshots.getDocuments()){
                            QuizItem quizItem = child.toObject(QuizItem.class);
                            quizItems.add(quizItem);
                        }

                        Log.d("TAG", "first time : "+ quizItems.get(quizItems.size()-1).getId());


                        callback.onResult(quizItems, null, quizItems.get(quizItems.size()-1).getId());

                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, QuizItem> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<String> params, @NonNull final LoadCallback<String, QuizItem> callback) {

        db.collection("quiz")
                .whereEqualTo("owner", mAuth.getUid())
               .orderBy("id")
                .startAfter(params.key)
                .limit(params.requestedLoadSize)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) return;

                        List<QuizItem> quizItems = new ArrayList<>();
                        for (DocumentSnapshot child :queryDocumentSnapshots.getDocuments()){
                            QuizItem quizItem = child.toObject(QuizItem.class);
                            quizItems.add(quizItem);
                        }

                        Log.d("TAG", "privous id: "+ params.key);


                        Log.d("TAG", "second time: "+ quizItems.get(quizItems.size()-1).getId());



                        callback.onResult(quizItems, quizItems.get(quizItems.size()-1).getId());

                    }
                });
    }
}
