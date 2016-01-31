/*
package in.flashfetch.sellerapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewFlipper;

*/
/**
 * Created by SAM10795 on 25-01-2016.
 *//*

public class Intro extends Activity {

    ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introscreens);
        viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);
        Button button = (Button)findViewById(R.id.get_start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Add intent to go to next activity
            }
        });

        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float last=0;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        last = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        float current = event.getX();
                        if(last>current)
                        {
                            if(viewFlipper.getDisplayedChild()==3)
                            {
                                break;
                            }
                            viewFlipper.setInAnimation(MainActivity.this,android.R.anim.slide_in_left);
                            viewFlipper.setOutAnimation(MainActivity.this,android.R.anim.slide_out_right);
                            viewFlipper.showNext();
                        }
                        else
                        {
                            if(viewFlipper.getDisplayedChild()==0)
                            {
                                break;
                            }
                            viewFlipper.setInAnimation(MainActivity.this,android.R.anim.slide_out_right);
                            viewFlipper.setOutAnimation(MainActivity.this,android.R.anim.slide_in_left);
                            viewFlipper.showPrevious();

                        }
                        break;
                }
                return false;
            }
        });
    }
}
*/
