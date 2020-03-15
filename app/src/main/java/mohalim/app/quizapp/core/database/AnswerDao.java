package mohalim.app.quizapp.core.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import mohalim.app.quizapp.core.models.AnswerItem;
import mohalim.app.quizapp.core.models.SessionItem;

@Dao
public interface AnswerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AnswerItem answerItem);

    @Query("DELETE FROM answeritem")
    void deleteAll();

    @Query("DELETE FROM answeritem WHERE id = :id")
    void deleteAnswerById(int id);

    @Query("DELETE FROM answeritem WHERE questionId = :questionId AND quizId = :quizId")
    void deleteAnswerFromInternal(String questionId, String quizId);

    @Query("SELECT * from answeritem")
    List<AnswerItem> getAll();

    @Query("SELECT * FROM answeritem WHERE questionId = :questionId AND quizId = :quizId ORDER BY arrange")
    LiveData<List<AnswerItem>> getAnswersForQuestionFromInternalObserved(String questionId, String quizId);

    @Query("SELECT * FROM answeritem WHERE questionId = :questionId AND quizId = :quizId ORDER BY arrange")
    List<AnswerItem> getAnswersForQuestionFromInternal(String questionId, String quizId);

    @Query("SELECT COUNT(id) FROM answeritem")
    int getCount();

}
