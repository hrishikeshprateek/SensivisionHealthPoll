package thundersharp.sensivisionhealth.poll.core.test;

import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Room;

import java.lang.ref.WeakReference;

import thundersharp.sensivisionhealth.poll.core.interactors.AdminLoginPresenter;
import thundersharp.sensivisionhealth.poll.core.interactors.LoginPresenter;
import thundersharp.sensivisionhealth.poll.core.models.LoginModel;
import thundersharp.sensivisionhealth.poll.core.models.LoginModelAdmin;
import thundersharp.sensivisionhealth.poll.core.room.AdminLoginDatabase;
import thundersharp.sensivisionhealth.poll.core.room.VoterLoginDatabase;

public class CreateDummyUsers extends AsyncTask<Void, Void, Void> {

    private AdminLoginPresenter adminLoginPresenter;
    private LoginPresenter userLoginPresenter;
    private WeakReference<Context> context;

    public CreateDummyUsers(Context context) {
        this.context = new WeakReference<>(context);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        adminLoginPresenter = AdminLoginDatabase.getInstance(context.get()).adminLoginPresenter();

        userLoginPresenter = VoterLoginDatabase.getInstance(context.get()).loginPresenter();

        ///////////////////GENERATING DUMMY ADMIN ACCOUNTS//////////////////
        if (adminLoginPresenter.getAllAdmins().size() < 1) {
            adminLoginPresenter.insertAllAdmins(new LoginModelAdmin("AD01", "7301694135", "Hrishikesh prateek", "1909", 1),
                    new LoginModelAdmin("AD02", "7506081242", "Amala Nair C", "2234", 1));
        }
        //////////////////////////////////////////////////////////////

        ///////////////////GENERATING DUMMY VOTER ACCOUNTS//////////////////
        if (userLoginPresenter.getAllUsers().size() < 1) {
            userLoginPresenter.insertAllUsers(new LoginModel("VT01", "7301694136", "Hrishikesh prateek User", "1909", 0),
                    new LoginModel("VT02", "7506081243", "Amala Nair C User", "2234", 0));
        }
        //////////////////////////////////////////////////////////////
        return null;
    }
}
