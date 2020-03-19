package mohalim.app.quizapp.core.datasource;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.paging.PageKeyedDataSource;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


import mohalim.app.quizapp.core.models.QuizItem;

public class QuizDataSource extends PageKeyedDataSource<DocumentSnapshot, QuizItem> {

    private String quizSearch = "";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    public QuizDataSource() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<DocumentSnapshot> params, @NonNull final LoadInitialCallback<DocumentSnapshot, QuizItem> callback) {

        db.collection("quiz")
                .whereEqualTo("owner", mAuth.getUid())
                .orderBy("quizName")
                .startAt(quizSearch.trim())
                .endAt(quizSearch.trim()+ "\uf8ff")
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



                        callback.onResult(quizItems, null, queryDocumentSnapshots.getDocuments().get(quizItems.size()-1));

                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<DocumentSnapshot> params, @NonNull LoadCallback<DocumentSnapshot, QuizItem> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<DocumentSnapshot> params, @NonNull final LoadCallback<DocumentSnapshot, QuizItem> callback) {

        db.collection("quiz")
                .whereEqualTo("owner", mAuth.getUid())
                .orderBy("quizName")
                .startAt(quizSearch.trim())
                .endAt(quizSearch.trim()+ "\uf8ff")
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

                        callback.onResult(quizItems, queryDocumentSnapshots.getDocuments().get(quizItems.size()-1));

                    }
                });

    }

    public void uodateQuizName(String quizSearch) {
        this.quizSearch = quizSearch;
    }
}
