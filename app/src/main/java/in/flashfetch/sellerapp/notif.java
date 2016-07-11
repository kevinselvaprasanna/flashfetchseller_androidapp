package in.flashfetch.sellerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import in.flashfetch.sellerapp.Adapters.NotAdapter;
import in.flashfetch.sellerapp.Objects.Nots;

public class notif extends AppCompatActivity {

    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private ArrayList<Nots> notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notif_layout);

        recyclerView = (RecyclerView)findViewById(R.id.rvNots);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(notif.this);

        recyclerView.setLayoutManager(layoutManager);

        notifications = Nots.getAllNots(notif.this);

        NotAdapter notsAdapter = new NotAdapter(notif.this,notifications);
        recyclerView.setAdapter(notsAdapter);

    }
}
