package in.flashfetch.sellerapp;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Empty_2 extends AppCompatActivity {

    TextView boxtext,day,hour,min,sec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_box);

        boxtext = (TextView)findViewById(R.id.boxtext);
        day = (TextView)findViewById(R.id.day);
        hour = (TextView)findViewById(R.id.hr);
        min = (TextView)findViewById(R.id.min);
        sec = (TextView)findViewById(R.id.sec);

        final CountDownTimer countDownTimer = new CountDownTimer(186400000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                day.setText(millisUntilFinished/86400000+"");
                millisUntilFinished = millisUntilFinished%86400000;
                hour.setText(millisUntilFinished/3600000+"");
                millisUntilFinished = millisUntilFinished%3600000;
                hour.setText(millisUntilFinished/60000+"");
                millisUntilFinished = millisUntilFinished%60000;
                hour.setText(millisUntilFinished/1000+"");
            }

            @Override
            public void onFinish() {

            }
        };
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.empty_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.empty_contact)
        {
            //Contact
        }
        return super.onOptionsItemSelected(item);
    }
}
