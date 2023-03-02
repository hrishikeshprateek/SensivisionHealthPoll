package thundersharp.sensivisionhealth.poll.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import thundersharp.sensivisionhealth.poll.R;
import thundersharp.sensivisionhealth.poll.core.interactors.AdminLoginPresenter;
import thundersharp.sensivisionhealth.poll.core.interactors.LoginPresenter;
import thundersharp.sensivisionhealth.poll.core.models.LoginModel;
import thundersharp.sensivisionhealth.poll.core.models.LoginModelAdmin;
import thundersharp.sensivisionhealth.poll.core.room.AdminLoginDatabase;
import thundersharp.sensivisionhealth.poll.core.room.VoterLoginDatabase;
import thundersharp.sensivisionhealth.poll.core.test.CreateDummyUsers;

public class LoginActivity extends AppCompatActivity {

    int accLevel = 0; //default for user
    private String[] levels = {"Access level 0 : USER", "Access level 1 : ADMIN"};
    private TextView accessLevelText;

    private AdminLoginPresenter adminLoginPresenter;
    private LoginPresenter userLoginPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.fb).setOnClickListener(i -> Toast.makeText(this, "Provider disabled !!", Toast.LENGTH_SHORT).show());
        findViewById(R.id.google).setOnClickListener(i -> Toast.makeText(this, "Provider disabled !!", Toast.LENGTH_SHORT).show());
        accessLevelText = findViewById(R.id.accessLevel);
        findViewById(R.id.sendotp).setOnClickListener(y -> startActivity(new Intent(this,OtpVerificationActivity.class)));
        findViewById(R.id.skip).setOnClickListener(i -> showAccountLevelChoice());

        new CreateDummyUsers(this).execute();

        TextView loginId = findViewById(R.id.editText_carrierNumber);
        AppCompatButton proceed = findViewById(R.id.sendotp);

        proceed.setOnClickListener(o -> {
            if (accLevel == 0){
                LoginModel loginModelVoter = VoterLoginDatabase.getInstance(this).loginPresenter().findUsersByLogin(loginId.getText().toString());
                if (loginModelVoter == null){
                    Toast.makeText(this, "No user found or this user have a ADMIN account !!", Toast.LENGTH_SHORT).show();
                }else startActivityForResult(new Intent(this,OtpVerificationActivity.class).putExtra("accLevel",accLevel).putExtra("loginData",loginModelVoter),1001);

            }else {
                LoginModelAdmin loginModelAdmin = AdminLoginDatabase.getInstance(this).adminLoginPresenter().findAdminByLogin(loginId.getText().toString());
                if (loginModelAdmin == null){
                    Toast.makeText(this, "No user found or this user have a VOTER account !!", Toast.LENGTH_SHORT).show();
                }else startActivityForResult(new Intent(this,OtpVerificationActivity.class).putExtra("accLevel",accLevel).putExtra("loginData",loginModelAdmin),1001);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == 1002){
            if (data.getBooleanExtra("login",false)){
                LoginModel loginModel;
                LoginModelAdmin loginModelAdmin;
                if (data.getIntExtra("accLevel",0) == 0) {
                    loginModel = ((LoginModel) data.getSerializableExtra("loginData"));
                    startActivity(new Intent(this, VoterHome.class).putExtra("data",loginModel));
                    Toast.makeText(this, "Welcome "+loginModel.name+" you are logged in as voter !!", Toast.LENGTH_SHORT).show();
                }else {
                    loginModelAdmin = ((LoginModelAdmin) data.getSerializableExtra("loginData"));
                    startActivity(new Intent(this, AdminHome.class).putExtra("data",loginModelAdmin));
                    Toast.makeText(this, "Welcome "+loginModelAdmin.name+" you are logged in as admin !!", Toast.LENGTH_SHORT).show();
                }

                finish();
            }else Toast.makeText(this, "Error in Logging in", Toast.LENGTH_SHORT).show();
        }

    }

    public void Alert(View view){
        Toast.makeText(this, "Contact Sensivision health.", Toast.LENGTH_SHORT).show();
    }

    int opened;
    private void showAccountLevelChoice() {
        opened = 0;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this).setCancelable(true);
        alertDialog.setView(R.layout.dialog_user_drop_down);
        AlertDialog dialog = alertDialog.create();
        dialog.show();
        dialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);

        Spinner spinner = dialog.findViewById(R.id.cat);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (levels[i]) {
                    case "Access level 1 : ADMIN":
                        accLevel = 1;
                        accessLevelText.setText("Logging as : ADMIN");
                        opened++;
                        if (opened >= 2) dialog.dismiss();
                        break;
                    case "Access level 0 : USER":
                        accessLevelText.setText("Logging as : USER");
                        accLevel = 0;
                        opened++;
                        if (opened >= 2) dialog.dismiss();
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, levels);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(ad);

    }
}