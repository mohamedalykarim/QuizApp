package mohalim.app.quizapp.core.datasource;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.models.UserItem;

public class UserQuizDataSource extends PageKeyedDataSource<DocumentSnapshot, QuizItem> {

    private String quizSearch = "";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private UserItem userItem;


    public UserQuizDataSource() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<DocumentSnapshot> params, @NonNull final LoadInitialCallback<DocumentSnapshot, QuizItem> callback) {

        db.collection("quiz")
                .whereArrayContains("peopleCanAccess", userItem)
                .orderBy("quizName")
                .startAt(quizSearch.trim())
                .endAt(quizSearch.trim()+ "\uf8ff")
                .limit(params.requestedLoadSize)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (queryDocumentSnapshots.isEmpty()) {
                            callback.onResult(new ArrayList<QuizItem>(), null, null);
                            return;
                        };
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
                .whereArrayContains("peopleCanAccess", userItem)
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

    public void updateQuizName(String quizSearch) {
        this.quizSearch = quizSearch;
    }

    public void setUserItem(UserItem userItem) {
        this.userItem = userItem;
    }
}
