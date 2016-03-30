package in.flashfetch.sellerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class notif extends AppCompatActivity {

    TextView notifmain, notifsub1,notifsub2,notifsub3;
    ImageView ad_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notif_layout);

        notifmain = (TextView)findViewById(R.id.notif_large);
        notifsub1 = (TextView)findViewById(R.id.notif_small_1);
        notifsub2 = (TextView)findViewById(R.id.notif_small_2);
        notifsub3 = (TextView)findViewById(R.id.notif_small_3);

        ad_img = (ImageView)findViewById(R.id.ad_img);

    }
}
