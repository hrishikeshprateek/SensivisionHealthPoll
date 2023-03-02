package thundersharp.sensivisionhealth.poll.core.interactors;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import thundersharp.sensivisionhealth.poll.core.models.LoginModel;
import thundersharp.sensivisionhealth.poll.core.models.LoginModelAdmin;

@Dao
public interface AdminLoginPresenter {

    @Query("SELECT * FROM users_admin")
    List<LoginModelAdmin> getAllAdmins();

    @Query("SELECT * FROM users_admin WHERE id IN (:userIds)")
    List<LoginModelAdmin> loadAllAdminsByIds(int[] userIds);

    @Query("SELECT * FROM users_admin WHERE user_name LIKE :first LIMIT 1")
    LoginModelAdmin findAdminByName(String first);

    @Query("SELECT * FROM users_admin WHERE login_id LIKE :loginId LIMIT 1")
    LoginModelAdmin findAdminByLogin(String loginId);

    @Insert
    void insertAllAdmins(LoginModelAdmin... users);

    @Delete
    void deleteAdmin(LoginModelAdmin user);

}
