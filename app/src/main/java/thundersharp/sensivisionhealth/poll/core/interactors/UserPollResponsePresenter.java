package thundersharp.sensivisionhealth.poll.core.interactors;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import thundersharp.sensivisionhealth.poll.core.models.PollDataModel;
import thundersharp.sensivisionhealth.poll.core.models.UserPollResponseModel;

@Dao
public interface UserPollResponsePresenter {

    @Query("SELECT * FROM poll_response")
    List<UserPollResponseModel> getAllResponses();

    @Query("SELECT * FROM poll_response WHERE poll_id IN (:pollId)")
    List<UserPollResponseModel> loadPollResponseByPollId(long pollId);

    @Query("SELECT * FROM poll_response WHERE user_id LIKE :userId")
    List<UserPollResponseModel> findPollByUser(String userId);

    @Query("SELECT * FROM poll_response WHERE poll_id LIKE :pollId AND poll_option_selected LIKE :option")
    List<UserPollResponseModel> findPollResponseByPollAndResponse(long pollId, String option);


    @Query("SELECT * FROM poll_response WHERE poll_id LIKE :pollId AND user_id LIKE :userId LIMIT 1")
    UserPollResponseModel findPollResponseByUserAndPollId(long pollId, String userId);

    @Query("SELECT * FROM poll_response WHERE response_id LIKE :responseId")
    UserPollResponseModel findPollResponseID(long responseId);

    @Insert
    void insertAllPollsResponse(UserPollResponseModel... pollDataModels);

    @Delete
    void deletePollsResponse(UserPollResponseModel user);

}
