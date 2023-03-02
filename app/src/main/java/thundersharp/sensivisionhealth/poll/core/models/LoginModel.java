package thundersharp.sensivisionhealth.poll.core.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "users_voters")
public class LoginModel implements Serializable {

    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "login_id")
    public String loginId;

    @ColumnInfo(name = "user_name")
    public String name;

    @ColumnInfo(name = "auth_pin")
    public String pin;

    @ColumnInfo(name = "auth_level")
    public int authLevel;

    public LoginModel(String id, String loginId, String name, String pin, int authLevel) {
        this.id = id;
        this.loginId = loginId;
        this.name = name;
        this.pin = pin;
        this.authLevel = authLevel;
    }
}
