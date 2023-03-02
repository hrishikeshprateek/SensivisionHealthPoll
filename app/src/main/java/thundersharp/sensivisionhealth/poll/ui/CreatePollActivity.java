package thundersharp.sensivisionhealth.poll.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import thundersharp.sensivisionhealth.poll.R;
import thundersharp.sensivisionhealth.poll.core.adapters.AdminHomePollAdapter;
import thundersharp.sensivisionhealth.poll.core.models.PollDataModel;
import thundersharp.sensivisionhealth.poll.core.room.PollDatabase;

public class CreatePollActivity extends AppCompatActivity {

    private String[] pollStatus = {"0 : Poll status Private", "1 : Poll status Public","2 : Poll status Closed"};
    private TextView question, options;
    int pollStatusChoice;
    private AppCompatButton create, close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);
        ((Toolbar)findViewById(R.id.toolbar)).setNavigationOnClickListener(n -> finish());

        question = findViewById(R.id.question);
        options = findViewById(R.id.options);
        create = findViewById(R.id.create);
        close = findViewById(R.id.close);
        setUpSpinner();

        close.setOnClickListener(r -> finish());
        create.setOnClickListener(k ->{
            if (question.getText().toString().isEmpty()){
                question.setError("Question is required !!");
                question.requestFocus();
            } else if (options.getText().toString().isEmpty() ||
                    (options.getText().toString().split(",").length > 0 &&
                    options.getText().toString().split(",").length > 4)) {
                options.setError("Either the option size is 0 or more than 4, Edit it first to proceed !!");
                options.requestFocus();
            }else {
                StringBuilder stringBuilder = new StringBuilder();
                String[] optionsVal = options.getText().toString().split(",");
                Log.e("arr", Arrays.toString(optionsVal));
                stringBuilder.append("Please verify the poll data :\n");
                stringBuilder.append("------------------------------\n");
                stringBuilder.append("Question: ").append(question.getText().toString()).append("\n\n");
                stringBuilder.append("Option 1: ").append(optionsVal[0]).append("\n");
                stringBuilder.append("Option 2: ").append(optionsVal.length >= 1 ? optionsVal[1] : "n/a").append("\n");
                stringBuilder.append("Option 3: ").append(optionsVal.length - 1 >= 2 ? optionsVal[2] : "n/a").append("\n");
                stringBuilder.append("Option 4: ").append(optionsVal.length -1 >= 3 ?  optionsVal[3]: "n/a").append("\n");
                stringBuilder.append("------------------------------\n");
                stringBuilder.append("Poll visibility: ").append(pollStatus[pollStatusChoice]);

                new AlertDialog.Builder(this)
                        .setTitle("Confirm Poll")
                        .setMessage(stringBuilder.toString())
                        .setCancelable(false)
                        .setPositiveButton("CREATE",(f, t) -> {
                            try {
                                createPoll(optionsVal, question.getText().toString());
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .setNegativeButton("CANCEL",(o,g) ->o.dismiss())
                        .show();
            }
        });
    }

    private void createPoll(String[] optionsVal, String question) throws JSONException {
        JSONArray data = new JSONArray();
        for(int i = 0; i < optionsVal.length; i++){
            data.put(new JSONObject().put(AdminHomePollAdapter.getKeyFromPos(i),optionsVal[i]));
        }
        PollDatabase
                .getInstance(this)
                .pollPresenter()
                .insertAllPolls(new PollDataModel(data.toString(),question,pollStatusChoice));
        Toast.makeText(this, "Poll created please refresh !!", Toast.LENGTH_SHORT).show();
        finish();

    }

    private void setUpSpinner(){
        Spinner spinner = findViewById(R.id.cat);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);

                switch (pollStatus[i]) {
                    case "0 : Poll status Private":
                        pollStatusChoice = 0;
                        break;
                    case "1 : Poll status Public":
                        pollStatusChoice = 1;
                        break;
                    case "2 : Poll status Closed":
                        pollStatusChoice = 2;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, pollStatus);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(ad);
    }
}