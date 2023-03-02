package thundersharp.sensivisionhealth.poll.core.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import thundersharp.sensivisionhealth.poll.core.interactors.AdminLoginPresenter;
import thundersharp.sensivisionhealth.poll.core.interactors.LoginPresenter;
import thundersharp.sensivisionhealth.poll.core.models.LoginModel;
import thundersharp.sensivisionhealth.poll.core.models.LoginModelAdmin;
import thundersharp.sensivisionhealth.poll.core.test.CreateDummyUsers;

@Database(entities = {LoginModel.class}, version = 1)
public abstract class VoterLoginDatabase extends RoomDatabase {

    public abstract LoginPresenter loginPresenter();
    private static VoterLoginDatabase instance = null;

    public static synchronized VoterLoginDatabase getInstance(Context context){
        if (instance == null) instance = Room.databaseBuilder(context ,VoterLoginDatabase.class, "users_voters")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        return instance;
    }

}
