package thundersharp.sensivisionhealth.poll.core.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "polls")
public class PollDataModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public long id;
    @ColumnInfo(name = "poll_options")
    public String options;
    @ColumnInfo(name = "poll_question")
    public String question;
    @ColumnInfo(name = "poll_status")
    public int status;

    public PollDataModel(String options, String question, int status) {
        this.options = options;
        this.question = question;
        this.status = status;
    }
}
