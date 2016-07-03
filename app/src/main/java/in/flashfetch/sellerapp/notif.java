package in.flashfetch.sellerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import in.flashfetch.sellerapp.Adapters.NotAdapter;
import in.flashfetch.sellerapp.Adapters.NotificationAdapter;
import in.flashfetch.sellerapp.Objects.Notification;
import in.flashfetch.sellerapp.Objects.Nots;
//import it.gmariotti.recyclerview.adapter.AlphaAnimatorAdapter;
//import it.gmariotti.recyclerview.itemanimator.SlideInOutLeftItemAnimator;

public class notif extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notif_layout);
        LinearLayoutManager layoutManager;
        RecyclerView rvNot;
        ArrayList<Nots> nots;


        rvNot = (RecyclerView)findViewById(R.id.rvNots);
        //rvNot.setItemAnimator(new SlideInOutLeftItemAnimator(rvNot));
        layoutManager = new LinearLayoutManager(notif.this);

        //set the recycler view to use the linear layout manager
        rvNot.setLayoutManager(layoutManager);

        // Load events from Database
        // events = Event.getAllRelevantEvents(getActivity());
        nots = Nots.getAllNots(notif.this);

        //initialize events feed adapter
        NotAdapter notsAdapter = new NotAdapter(notif.this,nots);
        rvNot.setAdapter(notsAdapter);


    }
}
