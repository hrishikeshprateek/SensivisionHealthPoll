package thundersharp.sensivisionhealth.poll.core.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import thundersharp.sensivisionhealth.poll.core.interactors.PollPresenter;
import thundersharp.sensivisionhealth.poll.core.interactors.UserPollResponsePresenter;
import thundersharp.sensivisionhealth.poll.core.models.PollDataModel;
import thundersharp.sensivisionhealth.poll.core.models.UserPollResponseModel;

@Database(entities = {UserPollResponseModel.class}, version = 1)
public abstract class PollResponseDatabase extends RoomDatabase {

    public abstract UserPollResponsePresenter userPollResponsePresenter();

    private static PollResponseDatabase instance = null;

    public static synchronized PollResponseDatabase getInstance(Context context){
        if (instance == null) instance = Room.databaseBuilder(context , PollResponseDatabase.class, "poll_response")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        return instance;
    }

}
