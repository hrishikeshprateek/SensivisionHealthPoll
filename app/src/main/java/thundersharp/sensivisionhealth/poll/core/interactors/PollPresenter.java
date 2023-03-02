package thundersharp.sensivisionhealth.poll.core.interactors;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import thundersharp.sensivisionhealth.poll.core.models.PollDataModel;

@Dao
public interface PollPresenter {

    @Query("SELECT * FROM polls")
    List<PollDataModel> getAllPolls();

    @Query("SELECT * FROM polls WHERE id IN (:id)")
    PollDataModel loadPollById(String id);

    @Query("SELECT * FROM polls WHERE poll_question LIKE :question LIMIT 1")
    PollDataModel findPollByQuestion(String question);

    @Query("SELECT * FROM polls WHERE poll_status LIKE :status")
    List<PollDataModel> findPollByStatus(int status);


    @Insert
    void insertAllPolls(PollDataModel... pollDataModels);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void updatePolls(PollDataModel pollDataModel);

    @Delete
    void deletePolls(PollDataModel user);

}
