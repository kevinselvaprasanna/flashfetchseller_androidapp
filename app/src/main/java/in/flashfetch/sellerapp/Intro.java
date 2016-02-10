
package in.flashfetch.sellerapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewFlipper;

/**
 * Created by SAM10795 on 25-01-2016.
 */

public class Intro extends Activity{

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
                Intent intent = new Intent(Intro.this,MainActivity.class);
                startActivity(intent);
            }
        });

        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Point point = new Point();
                getWindowManager().getDefaultDisplay().getSize(point);
                float mid = (point.x)/2;
                float current = event.getX();
                Log.i("Pointer", Float.toString(current) + "-" + Float.toString(mid));
                if (mid < current) {
                    if (viewFlipper.getDisplayedChild() == 3) {

                    }
                    viewFlipper.setInAnimation(Intro.this, R.anim.left_out);
                    viewFlipper.setOutAnimation(Intro.this, R.anim.right_in);
                    viewFlipper.showNext();
                } else {
                    if (viewFlipper.getDisplayedChild() == 0) {

                    }
                    viewFlipper.setInAnimation(Intro.this, R.anim.right_out);
                    viewFlipper.setOutAnimation(Intro.this, R.anim.left_in);
                    viewFlipper.showPrevious();

                }
                return false;
            }
        });
    }
}

