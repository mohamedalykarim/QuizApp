package mohalim.app.quizapp.core.datasource;

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

public class QuestionDataSource extends PageKeyedDataSource<DocumentSnapshot, QuestionItem> {
    private final String quizId;
    private FirebaseFirestore db;
    private String questionSearch = "";

    public QuestionDataSource(String quizId) {
        db = FirebaseFirestore.getInstance();
        this.quizId = quizId;
    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<DocumentSnapshot> params, @NonNull final LoadInitialCallback<DocumentSnapshot, QuestionItem> callback) {
        db.collection("quiz").document(quizId).collection("question")
                .orderBy("questionText")
                .startAt(questionSearch.trim())
                .endAt(questionSearch.trim()+ "\uf8ff")
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

                        callback.onResult(questionItems,null, queryDocumentSnapshots.getDocuments().get(questionItems.size()-1));
                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<DocumentSnapshot> params, @NonNull LoadCallback<DocumentSnapshot, QuestionItem> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<DocumentSnapshot> params, @NonNull final LoadCallback<DocumentSnapshot, QuestionItem> callback) {
        db.collection("quiz").document(quizId).collection("question")
                .orderBy("questionText")
                .startAt(questionSearch.trim())
                .endAt(questionSearch.trim()+ "\uf8ff")
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

                        callback.onResult(questionItems, queryDocumentSnapshots.getDocuments().get(questionItems.size()-1));
                    }
                });
    }

    public void setQuestionSearch(String questionSearch) {
        this.questionSearch = questionSearch;
    }
}
