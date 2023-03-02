package thundersharp.sensivisionhealth.poll.core.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import thundersharp.sensivisionhealth.poll.core.interactors.AdminLoginPresenter;
import thundersharp.sensivisionhealth.poll.core.models.LoginModelAdmin;

@Database(entities = {LoginModelAdmin.class}, version = 1)
public abstract class AdminLoginDatabase extends RoomDatabase {

    public abstract AdminLoginPresenter adminLoginPresenter();

    private static AdminLoginDatabase instance = null;

    public static synchronized AdminLoginDatabase getInstance(Context context){
        if (instance == null) instance = Room.databaseBuilder(context ,AdminLoginDatabase.class, "users_admin")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        return instance;
    }

}
