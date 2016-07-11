package in.flashfetch.sellerapp;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.flashfetch.sellerapp.Constants.URLConstants;
import in.flashfetch.sellerapp.Helper.DatabaseHelper;
import in.flashfetch.sellerapp.Network.PostRequest;
import in.flashfetch.sellerapp.Objects.Notification;
import in.flashfetch.sellerapp.Objects.PostParam;
import in.flashfetch.sellerapp.Objects.UserProfile;

public class QuoteActivity extends AppCompatActivity {

    ViewFlipper flipper,imflipper;
    Typeface font;
    JSONObject ResponseJSON;
    EditText comnts,price_quote;
    ImageView upimg;
    QuoteTask qt;
    int deltype,type;
    String distance;
    String comment,qprice,id,pname;
    TextView uplimg,name,tprice,buyer_name,buyer_location,timer,same,similar,home_del,shop_vis,more_deals,quote_now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_quote);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        final ArrayList<Notification> mItem =  Notification.getNotification(this, id);
        name = (TextView)findViewById(R.id.name);   //Name of product
        name.setText(mItem.get(0).name);
        pname = mItem.get(0).name;
        tprice = (TextView)findViewById(R.id.price_product);  //Retrieved price of product
        tprice.setText("Rs. "+mItem.get(0).price);
        buyer_name = (TextView)findViewById(R.id.buyer_name);
        buyer_name.setText(mItem.get(0).buyer_name);
        buyer_location = (TextView)findViewById(R.id.buyer_location);
        distance = mItem.get(0).loc;
        timer = (TextView)findViewById(R.id.timer);
        upimg = (ImageView)findViewById(R.id.picture1);
        Glide
                .with(QuoteActivity.this)
                .load(mItem.get(0).imgurl)
                .centerCrop()
                .into(upimg);
        if(mItem.get(0).time - System.currentTimeMillis() > 0) {
            CountDownTimer countDownTimer = new CountDownTimer( mItem.get(0).time - System.currentTimeMillis(), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int hr = (int) (millisUntilFinished / 3600000);
                    int min = (int) ((millisUntilFinished / 60000) - 60 * hr);
                    int sec = (int) ((millisUntilFinished / 1000) - 60 * min - 3600 * hr);
                    timer.setText(hr + " h : " + min + " m");
                }

                @Override
                public void onFinish() {
                    timer.setText("Time Up");
                }
            };
            countDownTimer.start();
        }else
        {
            timer.setText("Time Up");
        }
        more_deals = (TextView)findViewById(R.id.deals_more); //Go Back
        more_deals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mItem.get(0).url));
                startActivity(browserIntent);
            }
        });

        //buyer_location.setText("Distance:"+distance+"km");  //Enter distance in KM
        buyer_location.setText(distance);
        comnts = (EditText)findViewById(R.id.Comments);

       //upimg = (ImageView)findViewById(R.id.uploadimg);
        uplimg = (TextView)findViewById(R.id.uplimg);
        uplimg.setVisibility(View.GONE);


        home_del = (TextView)findViewById(R.id.home_del);
        shop_vis = (TextView)findViewById(R.id.shop_vis);
        home_del.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.icons));
        shop_vis.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.ff_green));
        deltype =1;
        home_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_del.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.ff_green));
                shop_vis.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.icons));
                deltype =0;
                //Home del selected
            }
        });
        shop_vis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_del.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.icons));
                shop_vis.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.ff_green));
                deltype =1;
                //Shop vis selected
            }
        });

        same = (TextView)findViewById(R.id.same);
        similar = (TextView)findViewById(R.id.similar);
        same.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.ff_green));
        similar.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.ff_gray));
        type =0;
        same.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                same.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.ff_green));
                similar.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.ff_gray));
                type =0;
                //same selected
            }
        });
        similar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                same.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.icons));
                similar.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.ff_green));
                type =1;
                //similar selected
            }
        });

        price_quote = (EditText)findViewById(R.id.price);    //This is the price which the seller quotes

        imflipper = (ViewFlipper)findViewById(R.id.flipperimg);

        quote_now = (TextView)findViewById(R.id.quote_now);

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
        quote_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    qt = new QuoteTask();
                    qt.execute();
                }

            }
        });

    }

    private boolean validate()
    {
        if(price_quote.getText().length()!=0)
        {
            return true;
        }
        Toast.makeText(this,"Enter a price",Toast.LENGTH_SHORT).show();
        return false;
    }

    public class QuoteTask extends AsyncTask<Void, Void, Boolean> {
        QuoteTask(){

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            qprice = price_quote.getText().toString();
            comment = comnts.getText().toString();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ArrayList<PostParam> instiPostParams = new ArrayList<PostParam>();
            instiPostParams.add(new PostParam("type",String.valueOf(type)));
            instiPostParams.add(new PostParam("deltype",String.valueOf(deltype)));
            instiPostParams.add(new PostParam("comment",comment));
            instiPostParams.add(new PostParam("qprice",qprice));
            instiPostParams.add(new PostParam("id",id));
            instiPostParams.add(new PostParam("email", UserProfile.getEmail(QuoteActivity.this)));
            instiPostParams.add(new PostParam("token",UserProfile.getToken(QuoteActivity.this)));
            ResponseJSON = PostRequest.execute(URLConstants.URL_Quote, instiPostParams, null);
            Log.d("RESPONSE", ResponseJSON.toString());


            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            try {
                if(ResponseJSON.getJSONObject("data").getInt("result")==1){
                    ContentValues cv = new ContentValues();
                    cv.put("name",pname);
                    cv.put("qprice",qprice);
                    cv.put("type",type);
                    cv.put("deltype",deltype);
                    cv.put("comment",comment);
                    cv.put("quoted", 1);
                    cv.put("id",id);
                    DatabaseHelper dh = new DatabaseHelper(QuoteActivity.this);
                    dh.addNot(cv);
                    startActivity(new Intent(QuoteActivity.this,MainActivity.class));
                    finish();
                }else {
                    Toast.makeText(QuoteActivity.this,R.string.SERVER_ERROR,Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(QuoteActivity.this,R.string.SERVER_ERROR,Toast.LENGTH_LONG).show();
            }

        }
    }


}
