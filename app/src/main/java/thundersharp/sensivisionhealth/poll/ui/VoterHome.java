package thundersharp.sensivisionhealth.poll.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import thundersharp.sensivisionhealth.poll.R;
import thundersharp.sensivisionhealth.poll.core.adapters.AdminHomeAdapter;
import thundersharp.sensivisionhealth.poll.core.adapters.VoterHomeAdapter;
import thundersharp.sensivisionhealth.poll.core.callbacks.SwipeCallBack;
import thundersharp.sensivisionhealth.poll.core.enums.StatusCodes;
import thundersharp.sensivisionhealth.poll.core.models.LoginModel;
import thundersharp.sensivisionhealth.poll.core.models.LoginModelAdmin;
import thundersharp.sensivisionhealth.poll.core.models.PollDataModel;
import thundersharp.sensivisionhealth.poll.core.room.PollDatabase;


public class VoterHome extends AppCompatActivity {

    private RecyclerView recyclerView;
    public static LoginModel loginModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voter_home);
        loginModel = ((LoginModel) getIntent().getSerializableExtra("data"));
        ((Toolbar) findViewById(R.id.toolbar)).setSubtitle("Hello, "+loginModel.name);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe);
        recyclerView = findViewById(R.id.recyclerView);

        update();
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                update();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void update() {
        List<PollDataModel> pollDataModels = PollDatabase.getInstance(this).pollPresenter().getAllPolls();
        List<PollDataModel> pollDataModelList = new ArrayList<>();
        for (PollDataModel poll : pollDataModels) {
            if (poll.status != StatusCodes.STATUS_PRIVATE) pollDataModelList.add(poll);
        }
        VoterHomeAdapter adminHomeAdapter = new VoterHomeAdapter(pollDataModelList);
        recyclerView.setAdapter(adminHomeAdapter);
    }
}