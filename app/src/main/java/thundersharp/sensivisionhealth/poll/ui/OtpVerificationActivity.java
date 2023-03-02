package thundersharp.sensivisionhealth.poll.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;
import thundersharp.sensivisionhealth.poll.R;
import thundersharp.sensivisionhealth.poll.core.models.LoginModel;
import thundersharp.sensivisionhealth.poll.core.models.LoginModelAdmin;

public class OtpVerificationActivity extends AppCompatActivity {

    AppCompatButton submit;
    TextView cancel;
    boolean isSubmitEnabled = false;
    private boolean otpVerified;
    private LoginModel loginModel;
    private LoginModelAdmin loginModelAdmin;
    private OtpTextView otpTextView;
    private int accLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        accLevel = getIntent().getIntExtra("accLevel",0);
        if (accLevel == 0) {
            loginModel = ((LoginModel) getIntent().getSerializableExtra("loginData"));
        }else loginModelAdmin = ((LoginModelAdmin) getIntent().getSerializableExtra("loginData")) ;


        otpTextView = findViewById(R.id.otp_view);
        submit = findViewById(R.id.vieify);
        cancel = findViewById(R.id.cancel);

        submit.setOnClickListener(view -> {
            if (isSubmitEnabled && otpVerified){
                Intent i = new Intent();
                i.putExtra("loginData",accLevel == 0 ? loginModel : loginModelAdmin);
                i.putExtra("login",true);
                i.putExtra("accLevel",accLevel);
                setResult(1002,i);
                finish();
            }else {
                Toast.makeText(this, "Pin invalid !!", Toast.LENGTH_SHORT).show();
            }
        });

        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }

            @Override
            public void onOTPComplete(String otpS) {
                isSubmitEnabled = true;
                otpVerified = otpS.equals(accLevel == 0 ? loginModel.pin : loginModelAdmin.pin);
            }
        });


    }
}