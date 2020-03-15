package mohalim.app.quizapp.core.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import mohalim.app.quizapp.core.models.AnswerItem;
import mohalim.app.quizapp.core.models.QuestionItem;
import mohalim.app.quizapp.core.models.SessionItem;

@Dao
public interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(QuestionItem questionItem);

    @Query("DELETE FROM questionitem")
    void deleteAll();

    @Query("DELETE FROM questionitem WHERE id = :id")
    void deleteQuestionById(int id);

    @Query("DELETE FROM questionitem WHERE id = :questionId AND quizId = :quizItemId")
    void deleteQuestionFromInternal(String questionId, String quizItemId);

    @Query("SELECT * from questionitem")
    List<QuestionItem> getAll();

    @Query("SELECT * FROM questionitem WHERE quizId = :id")
    LiveData<List<QuestionItem>> getQuestionsFromInternalObserved(String id);

    @Query("SELECT * FROM questionitem WHERE quizId = :id")
    List<QuestionItem> getQuestionsFromInternal(String id);


    @Query("SELECT COUNT(id) FROM questionitem")
    int getCount();


}
