package mohalim.app.quizapp.core.firebase;

import androidx.annotation.NonNull;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PageKeyedDataSource;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import mohalim.app.quizapp.core.models.QuestionItem;

public class QuestionDataSource extends PageKeyedDataSource<String, QuestionItem> {
    private final String quizId;
    private FirebaseFirestore db;

    public QuestionDataSource(String quizId) {
        db = FirebaseFirestore.getInstance();
        this.quizId = quizId;
    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull final LoadInitialCallback<String, QuestionItem> callback) {
        db.collection("quiz").document(quizId).collection("question")
                .orderBy("id")
                .startAt(0)
                .limit(params.requestedLoadSize)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty())return;
                        List<QuestionItem> questionItems = new ArrayList<>();
                        for (DocumentSnapshot questionSnapShot : queryDocumentSnapshots){
                            QuestionItem questionItem = questionSnapShot.toObject(QuestionItem.class);
                            questionItems.add(questionItem);
                        }

                        callback.onResult(questionItems,null, questionItems.get(questionItems.size()-1).getId());
                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, QuestionItem> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<String> params, @NonNull final LoadCallback<String, QuestionItem> callback) {
        db.collection("quiz").document(quizId).collection("question")
                .orderBy("id")
                .startAfter(params.key)
                .limit(params.requestedLoadSize)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty())return;
                        List<QuestionItem> questionItems = new ArrayList<>();
                        for (DocumentSnapshot questionSnapShot : queryDocumentSnapshots){
                            QuestionItem questionItem = questionSnapShot.toObject(QuestionItem.class);
                            questionItems.add(questionItem);
                        }

                        callback.onResult(questionItems, questionItems.get(questionItems.size()-1).getId());
                    }
                });
    }
}
