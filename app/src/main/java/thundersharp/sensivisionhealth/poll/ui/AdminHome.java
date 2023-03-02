package thundersharp.sensivisionhealth.poll.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import thundersharp.sensivisionhealth.poll.R;
import thundersharp.sensivisionhealth.poll.core.adapters.AdminHomeAdapter;
import thundersharp.sensivisionhealth.poll.core.callbacks.SwipeCallBack;
import thundersharp.sensivisionhealth.poll.core.interactors.PollPresenter;
import thundersharp.sensivisionhealth.poll.core.models.LoginModelAdmin;
import thundersharp.sensivisionhealth.poll.core.models.PollDataModel;
import thundersharp.sensivisionhealth.poll.core.room.PollDatabase;

public class AdminHome extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LoginModelAdmin loginModelAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        loginModelAdmin = ((LoginModelAdmin) getIntent().getSerializableExtra("data"));

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe);

        ((Toolbar) findViewById(R.id.toolbar)).setOnClickListener(t -> {
            new Worker().start();
        });
        ((Toolbar) findViewById(R.id.toolbar)).setSubtitle("Hello, "+loginModelAdmin.name);

        recyclerView = findViewById(R.id.recyclerView);
        findViewById(R.id.updateBtn).setOnClickListener(o ->{
            startActivity(new Intent(this,CreatePollActivity.class));
        });
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
        AdminHomeAdapter adminHomeAdapter = new AdminHomeAdapter(PollDatabase.getInstance(this)
                .pollPresenter()
                .getAllPolls()).setOnDataModifiedListener(() -> {
            Toast.makeText(this, "Poll status updated !", Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(adminHomeAdapter);

        SwipeCallBack swipeToDeleteCallback = new SwipeCallBack(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                try {

                    final int position = viewHolder.getAdapterPosition();
                    final PollDataModel item = adminHomeAdapter.getData().get(position);

                    adminHomeAdapter.removeItem(position);

                    PollDatabase
                            .getInstance(AdminHome.this)
                            .pollPresenter()
                            .deletePolls(item);
                    Toast.makeText(AdminHome.this, "deleted poll !!", Toast.LENGTH_SHORT).show();
                }catch (Exception E){
                    E.printStackTrace();
                }

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    class Worker extends Thread {
        @Override
        public synchronized void start() {
            super.start();
            JSONArray jsonObject = new JSONArray();
            try {
                jsonObject.put(new JSONObject().put("A", "Option A"));
                jsonObject.put(new JSONObject().put("B", "Option B"));
                jsonObject.put(new JSONObject().put("C", "Option C"));
                jsonObject.put(new JSONObject().put("D", "Option D"));

                PollDatabase
                        .getInstance(getApplicationContext())
                        .pollPresenter()
                        .insertAllPolls(new PollDataModel(jsonObject.toString(), "This is a sample test question one which is made to test the simple layout for proper rendering.", 0),
                                new PollDataModel(jsonObject.toString(), "This is a sample test question two which is made to test the simple layout for proper rendering.", 0),
                                new PollDataModel(jsonObject.toString(), "This is a sample test question three which is made to test the simple layout for proper rendering.", 1),
                                new PollDataModel(jsonObject.toString(), "This is a sample test question four which is made to test the simple layout for proper rendering.", 2),
                                new PollDataModel(jsonObject.toString(), "This is a sample test question five which is made to test the simple layout for proper rendering.", 0),
                                new PollDataModel(jsonObject.toString(), "This is a sample test question six which is made to test the simple layout for proper rendering.", 0),
                                new PollDataModel(jsonObject.toString(), "This is a sample test question seven which is made to test the simple layout for proper rendering.", 0));

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void run() {
            super.run();
        }

    }
}