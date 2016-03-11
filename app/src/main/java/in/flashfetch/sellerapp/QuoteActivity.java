package in.flashfetch.sellerapp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class QuoteActivity extends AppCompatActivity {

    ViewFlipper flipper,imflipper;
    Typeface font;
    Button submit;
    EditText comnts,price_quote;
    ImageView upimg;
    TextView uplimg,name,price,buyer_name,buyer_location,timer,ptype,same,similar,deltype,home_del,shop_vis,comments,tv8,more_deals,quote_now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);

        name = (TextView)findViewById(R.id.name);   //Name of product
        name.setText("Product Name: Name Surname");
        price = (TextView)findViewById(R.id.price_product);  //Retrieved price of product
        price.setText("Price: Rs. XYZ");
        buyer_name = (TextView)findViewById(R.id.buyer_name);
        buyer_name.setText("Buyer: Some Buyer");
        buyer_location = (TextView)findViewById(R.id.buyer_location);
        buyer_location.setText("Location: GPS Coordinates");
        timer = (TextView)findViewById(R.id.timer);
        CountDownTimer countDownTimer = new CountDownTimer(30000,1000) {    //30000->30s, time of timer 1000->1s, time of update
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                // Do stuff if it overshoots
            }
        };
        more_deals = (TextView)findViewById(R.id.deals_more); //Go Back

        submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Carry over data to provided page
            }
        });

        comnts = (EditText)findViewById(R.id.Comments);

        upimg = (ImageView)findViewById(R.id.uploadimg);
        uplimg = (TextView)findViewById(R.id.up_img_text);


        home_del = (TextView)findViewById(R.id.home_del);
        shop_vis = (TextView)findViewById(R.id.shop_vis);
        home_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_del.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.ff_green));
                shop_vis.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.ff_gray));
                //Home del selected
            }
        });
        shop_vis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_del.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.ff_gray));
                shop_vis.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.ff_green));
                //Shop vis selected
            }
        });

        same = (TextView)findViewById(R.id.same);
        similar = (TextView)findViewById(R.id.similar);
        same.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                same.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.ff_green));
                similar.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.ff_gray));
                //same selected
            }
        });
        similar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                same.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.ff_gray));
                similar.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.ff_green));
                //similar selected
            }
        });

        price_quote = (EditText)findViewById(R.id.price);    //This is the price which the seller quotes

        flipper = (ViewFlipper)findViewById(R.id.mainflipper);
        imflipper = (ViewFlipper)findViewById(R.id.flipperimg);

        quote_now = (TextView)findViewById(R.id.quote_now);
        quote_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipper.showNext();
                flipper.setOutAnimation(QuoteActivity.this,R.anim.top_up);
                flipper.setInAnimation(QuoteActivity.this,R.anim.bottom_up);
            }
        });

        uplimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Add method to upload images and show in upimg
            }
        });
/*
        font = Typeface.createFromAsset(getAssets(),
                "fonts/Lato-Medium.ttf");*/

        //TODO: Set typeface for text


        AppBarLayout appBar = (AppBarLayout)findViewById(R.id.appbar);
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout)findViewById(R.id.user_profile_coordinatorlayout);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBar.getLayoutParams();
        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
        behavior.setTopAndBottomOffset(-500);
        params.setBehavior(behavior);
        appBar.setLayoutParams(params);

        imflipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imflipper.showNext();
                imflipper.setInAnimation(QuoteActivity.this, R.anim.right_in);
                imflipper.setOutAnimation(QuoteActivity.this, R.anim.left_out);
            }
        });

    }

}
