package thundersharp.sensivisionhealth.poll.core.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "poll_response")
public class UserPollResponseModel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "response_id")
    public long id;

    @ColumnInfo(name = "response_time")
    public String time;

    @ColumnInfo(name = "poll_id")
    public long pollId;

    @ColumnInfo(name = "poll_option_selected")
    public String option_selected;

    @ColumnInfo(name = "user_id")
    public String userId;

    public UserPollResponseModel(String time, long pollId, String option_selected, String userId) {
        this.time = time;
        this.pollId = pollId;
        this.option_selected = option_selected;
        this.userId = userId;
    }
}
