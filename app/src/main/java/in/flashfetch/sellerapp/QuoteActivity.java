package in.flashfetch.sellerapp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class QuoteActivity extends AppCompatActivity {

    ViewFlipper flipper,imflipper;
    Typeface font;
    TextView name,price,buyer_name,buyer_location,timer,price_2,ptype,same,similar,deltype,home_del,shop_vis,comments,tv8,more_deals,quote_now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);

        flipper = (ViewFlipper)findViewById(R.id.mainflipper);
        imflipper = (ViewFlipper)findViewById(R.id.flipperimg);

        TextView textView = (TextView)findViewById(R.id.quote_now);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipper.showNext();
                flipper.setOutAnimation(QuoteActivity.this,R.anim.top_up);
                flipper.setInAnimation(QuoteActivity.this,R.anim.bottom_up);
            }
        });

        font = Typeface.createFromAsset(getAssets(),
                "fonts/Lato-Medium.ttf");

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
