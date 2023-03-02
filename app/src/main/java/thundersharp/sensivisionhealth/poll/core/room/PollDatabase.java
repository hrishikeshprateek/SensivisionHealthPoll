package thundersharp.sensivisionhealth.poll.core.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import thundersharp.sensivisionhealth.poll.core.interactors.AdminLoginPresenter;
import thundersharp.sensivisionhealth.poll.core.interactors.PollPresenter;
import thundersharp.sensivisionhealth.poll.core.models.LoginModelAdmin;
import thundersharp.sensivisionhealth.poll.core.models.PollDataModel;

@Database(entities = {PollDataModel.class}, version = 1)
public abstract class PollDatabase extends RoomDatabase {

    public abstract PollPresenter pollPresenter();

    private static PollDatabase instance = null;

    public static synchronized PollDatabase getInstance(Context context){
        if (instance == null) instance = Room.databaseBuilder(context , PollDatabase.class, "polls")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        return instance;
    }

}
