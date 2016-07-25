package in.flashfetch.sellerapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import in.flashfetch.sellerapp.Adapters.NotificationsAdapter;
import in.flashfetch.sellerapp.Objects.Notifications;

public class NotificationsActivity extends AppCompatActivity {

    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private ArrayList<Notifications> notifications;
    private TextView emptyNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.notif_layout);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Notifications");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationsActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        emptyNotifications = (TextView)findViewById(R.id.empty_notifications);

        recyclerView = (RecyclerView)findViewById(R.id.rvNots);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(NotificationsActivity.this);

        recyclerView.setLayoutManager(layoutManager);

        notifications = Notifications.getAllNots(NotificationsActivity.this);

        if(notifications.size() > 0) {
            NotificationsAdapter notsAdapter = new NotificationsAdapter(NotificationsActivity.this, notifications);
            recyclerView.setAdapter(notsAdapter);
        }else{
            emptyNotifications.setVisibility(View.VISIBLE);
        }


    }
}