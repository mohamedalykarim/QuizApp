package mohalim.app.quizapp.core.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import mohalim.app.quizapp.core.models.SessionItem;

@Dao
public interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SessionItem sessionItem);

    @Query("DELETE FROM SessionItem")
    void deleteAll();

    @Query("DELETE FROM SessionItem WHERE id = :id")
    void deleteSessionById(int id);

    @Query("SELECT * from SessionItem")
    List<SessionItem> getAll();

    @Query("SELECT * FROM SessionItem WHERE quizId = :id")
    SessionItem getSessionById(String id);

    @Query("SELECT COUNT(id) FROM sessionitem")
    int getCount();

    @Query("DELETE FROM SessionItem WHERE quizId = :quizId")
    void deleteSessionFromInternal(String quizId);
}
