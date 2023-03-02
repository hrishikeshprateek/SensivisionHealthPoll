package thundersharp.sensivisionhealth.poll.core.interactors;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import thundersharp.sensivisionhealth.poll.core.models.LoginModel;

@Dao
public interface LoginPresenter {

    @Query("SELECT * FROM users_voters")
    List<LoginModel> getAllUsers();

    @Query("SELECT * FROM users_voters WHERE id IN (:userIds)")
    List<LoginModel> loadAllUsersByIds(int[] userIds);

    @Query("SELECT * FROM users_voters WHERE user_name LIKE :first LIMIT 1")
    LoginModel findUsersByName(String first);

    @Query("SELECT * FROM users_voters WHERE login_id LIKE :loginId LIMIT 1")
    LoginModel findUsersByLogin(String loginId);

    @Insert
    void insertAllUsers(LoginModel... users);

    @Delete
    void deleteUser(LoginModel user);

}
