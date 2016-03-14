
package in.flashfetch.sellerapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import in.flashfetch.sellerapp.Objects.UserProfile;

/**
 * Created by SAM10795 on 25-01-2016.
 */

public class Intro extends Activity{

    ViewFlipper viewFlipper;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    Typeface font;
    int flipcount=1;
    ImageView thumb1,thumb2,thumb3,thumb4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(UserProfile.getEmail(Intro.this)!="")
        {
            checkPlayServices();
            if(UserProfile.getCategory(Intro.this) ==1){
                Intent i =new Intent(Intro.this,CategoryActivity.class);
                startActivity(i);
                finish();
            }
            else {
                Intent i = new Intent(Intro.this, MainActivity.class);
                startActivity(i);
                finish();
            }
            /*if (checkPlayServices()) {
                // Start IntentService to register this application with GCM.
                Intent intent = new Intent(LoginActivity.this, IE_RegistrationIntentService.class);
                startService(intent);
            }
            finish();*/
        }
        setContentView(R.layout.introscreens);
        checkPlayServices();


        setContentView(R.layout.intro_screens);
        thumb1 = (ImageView)findViewById(R.id.view_screen_1);
        thumb2 = (ImageView)findViewById(R.id.view_screen_2);
        thumb3 = (ImageView)findViewById(R.id.view_screen_3);
        thumb4 = (ImageView)findViewById(R.id.view_screen_4);

        thumb1.setScaleX(2);
        thumb1.setScaleY(2);
        viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);
        final Button button = (Button)findViewById(R.id.get_start);
        button.setVisibility(View.GONE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intro.this,StartActivity.class);
                startActivity(intent);
                finish();
            }
        });


       /* font = Typeface.createFromAsset(getAssets(),
                "fonts/Lato-Medium.ttf");*/

        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Point point = new Point();
                getWindowManager().getDefaultDisplay().getSize(point);
                float mid = (point.x) / 2;
                float current = event.getX();
                Log.i("Pointer", Float.toString(current) + "-" + Float.toString(mid));
                switch (flipcount)
                {
                    case 1:
                        thumb1.setScaleY(1);
                        thumb1.setScaleX(1);
                        break;
                    case 2:
                        thumb2.setScaleY(1);
                        thumb2.setScaleX(1);
                        break;
                    case 3:
                        thumb3.setScaleY(1);
                        thumb3.setScaleX(1);
                        break;
                    case 4:
                        thumb4.setScaleY(1);
                        thumb4.setScaleX(1);
                        break;
                }
                if (mid < current) {
                    if (viewFlipper.getDisplayedChild() == 3) {

                    }
                    viewFlipper.setInAnimation(Intro.this, R.anim.left_out);
                    viewFlipper.setOutAnimation(Intro.this, R.anim.right_in);
                    viewFlipper.showNext();
                    flipcount++;
                } else {
                    if (viewFlipper.getDisplayedChild() == 0) {

                    }
                    viewFlipper.setInAnimation(Intro.this, R.anim.right_out);
                    viewFlipper.setOutAnimation(Intro.this, R.anim.left_in);
                    viewFlipper.showPrevious();
                    flipcount--;

                }
                switch (flipcount)
                {
                    case 1:
                        thumb1.setScaleY(2);
                        thumb1.setScaleX(2);
                        break;
                    case 2:
                        thumb2.setScaleY(2);
                        thumb2.setScaleX(2);
                        break;
                    case 3:
                        thumb3.setScaleY(2);
                        thumb3.setScaleX(2);
                        break;
                    case 4:
                        thumb4.setScaleY(2);
                        thumb4.setScaleX(2);
                        button.setVisibility(View.VISIBLE);
                        break;
                }
                return false;
            }
        });
    }
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("tag", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}

