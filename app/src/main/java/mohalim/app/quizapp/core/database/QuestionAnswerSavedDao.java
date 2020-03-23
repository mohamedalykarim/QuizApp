package mohalim.app.quizapp.core.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import mohalim.app.quizapp.core.models.QuestionAnswerSavedItem;
import mohalim.app.quizapp.core.models.SessionItem;

@Dao
public interface QuestionAnswerSavedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(QuestionAnswerSavedItem questionAnswerSavedItem);

    @Query("DELETE FROM questionanswersaveditem")
    void deleteAll();

    @Query("DELETE FROM questionanswersaveditem WHERE id = :id")
    void deleteSessionById(int id);

    @Query("SELECT * from questionanswersaveditem")
    List<QuestionAnswerSavedItem> getAll();

    @Query("SELECT * FROM questionanswersaveditem WHERE quizId = :quizId AND questionId = :questionId")
    QuestionAnswerSavedItem getQuestionAnsweredSavedForQuestion(String quizId, String questionId);

    @Query("SELECT COUNT(id) FROM questionanswersaveditem")
    int getCount();

    @Query("DELETE FROM questionanswersaveditem WHERE quizId = :quizId")
    void deleteQuestionAnsweredSavedForQuiz(String quizId);
}
