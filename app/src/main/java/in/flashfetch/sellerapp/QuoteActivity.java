package in.flashfetch.sellerapp;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.flashfetch.sellerapp.CommonUtils.Toasts;
import in.flashfetch.sellerapp.CommonUtils.Utils;
import in.flashfetch.sellerapp.Constants.Constants;
import in.flashfetch.sellerapp.Helper.DatabaseHelper;
import in.flashfetch.sellerapp.Interfaces.UIListener;
import in.flashfetch.sellerapp.Network.ServiceManager;
import in.flashfetch.sellerapp.Objects.QuoteObject;
import in.flashfetch.sellerapp.Objects.Transactions;

public class QuoteActivity extends BaseActivity {

    private ImageView upimg;
    private ProgressDialog progressDialog;
    private int deliveryType,productType;
    private Typeface font;
    private ArrayList<Transactions> transactions;
    private EditText editComments,quotePrice;
    private String comments,quotedPrice,location,productId,productPrice,productName,buyerName;
    private TextView productNameText,productPriceText,buyerNameText,buyerLocationText,timer,sameProduct,similarProduct,homeDelivery,shopVisit,moreDeals,quoteNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_quote);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            productId = bundle.getString("id");
        }

//        transactions =  Notification.getTransaction(this, productId);
        transactions = new ArrayList<>();
        transactions.add(Constants.DUMMY_REQUESTED_TRANSACTION);

        productName = transactions.get(0).productName;
        productPrice = transactions.get(0).productPrice;
        buyerName = transactions.get(0).buyerName;
        location = transactions.get(0).buyerLocation;

        progressDialog = getProgressDialog(this);

        upimg = (ImageView)findViewById(R.id.picture1);

        productNameText = (TextView)findViewById(R.id.name);
        productPriceText = (TextView)findViewById(R.id.price_product);
        buyerNameText = (TextView)findViewById(R.id.buyer_name);
        buyerLocationText = (TextView)findViewById(R.id.buyer_location);
        timer = (TextView)findViewById(R.id.timer);

        quotePrice = (EditText)findViewById(R.id.price);
        editComments = (EditText)findViewById(R.id.Comments);


        sameProduct = (Button)findViewById(R.id.same);
        similarProduct = (Button)findViewById(R.id.similar);

        homeDelivery = (Button)findViewById(R.id.home_del);
        shopVisit = (Button)findViewById(R.id.shop_vis);

        quoteNow = (Button)findViewById(R.id.quote_now);
        moreDeals = (Button)findViewById(R.id.deals_more);


        productNameText.setText(productName);
        productPriceText.setText("Rs. " + productPrice);
        buyerNameText.setText(buyerName);
        buyerLocationText.setText(location);

        Glide.with(QuoteActivity.this).load(transactions.get(0).imageURL).centerCrop().into(upimg);

        if(transactions.get(0).time - System.currentTimeMillis() > 0) {
            CountDownTimer countDownTimer = new CountDownTimer( transactions.get(0).time - System.currentTimeMillis(), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                    int hr = (int) (millisUntilFinished / 3600000);
                    int min = (int) ((millisUntilFinished / 60000) - 60 * hr);

                    timer.setText(hr + " h : " + min + " m");
                    //TODO: Set timer for red before few mins
                }

                @Override
                public void onFinish() {
                    timer.setTextColor(Color.RED);
                    timer.setText("Time Up");
                }
            };
            countDownTimer.start();
        }else {
            timer.setTextColor(Color.RED);
            timer.setText("Time Up");
        }

        homeDelivery.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.ff_gray));
        homeDelivery.setTextColor(ContextCompat.getColor(QuoteActivity.this,R.color.black));

        shopVisit.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.ff_green));
        shopVisit.setTextColor(ContextCompat.getColor(QuoteActivity.this,R.color.icons));

        homeDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeDelivery.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.ff_green));
                homeDelivery.setTextColor(ContextCompat.getColor(QuoteActivity.this,R.color.icons));

                shopVisit.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.ff_gray));
                shopVisit.setTextColor(ContextCompat.getColor(QuoteActivity.this,R.color.black));

                deliveryType = Constants.HOME_DELIVERY;
            }
        });

        shopVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeDelivery.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.ff_gray));
                homeDelivery.setTextColor(ContextCompat.getColor(QuoteActivity.this,R.color.black));

                shopVisit.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.ff_green));
                shopVisit.setTextColor(ContextCompat.getColor(QuoteActivity.this,R.color.icons));

                deliveryType = Constants.SHOP_VISIT;
            }
        });


        sameProduct.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.ff_green));
        sameProduct.setTextColor(ContextCompat.getColor(QuoteActivity.this,R.color.icons));

        similarProduct.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.ff_gray));
        similarProduct.setTextColor(ContextCompat.getColor(QuoteActivity.this,R.color.black));

        sameProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sameProduct.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.ff_green));
                sameProduct.setTextColor(ContextCompat.getColor(QuoteActivity.this,R.color.icons));

                similarProduct.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.ff_gray));
                similarProduct.setTextColor(ContextCompat.getColor(QuoteActivity.this,R.color.black));

                productType = Constants.SAME_PRODUCT;
            }
        });

        similarProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sameProduct.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.ff_gray));
                sameProduct.setTextColor(ContextCompat.getColor(QuoteActivity.this,R.color.black));

                similarProduct.setBackgroundColor(ContextCompat.getColor(QuoteActivity.this,R.color.ff_green));
                similarProduct.setTextColor(ContextCompat.getColor(QuoteActivity.this,R.color.icons));

                productType = Constants.SIMILAR_PRODUCT;
            }
        });

        font = Typeface.createFromAsset(getAssets(),"fonts/Roboto_Medium.ttf");

        setTypeface();

//        AppBarLayout appBar = (AppBarLayout)findViewById(R.id.appbar);
//        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBar.getLayoutParams();
//        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
//        behavior.setTopAndBottomOffset(-500);
//        params.setBehavior(behavior);
//        appBar.setLayoutParams(params);

        Toolbar toolbar = (Toolbar)findViewById(R.id.quote_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Quote your Products");

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitleEnabled(false);

        moreDeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(transactions.get(0).productURL));
                startActivity(browserIntent);
            }
        });

        quoteNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                quotedPrice = quotePrice.getText().toString();
                comments = editComments.getText().toString();

                if(!TextUtils.isEmpty(quotedPrice)) {

                    if(Utils.isInternetAvailable(QuoteActivity.this)){

                        QuoteObject quoteObject  = new QuoteObject();

                        quoteObject.setProductId(productId);
                        quoteObject.setQuotedPrice(quotedPrice);
                        quoteObject.setComments(comments);
                        quoteObject.setProductType(productType);
                        quoteObject.setDeliveryType(deliveryType);

                        progressDialog.show();

                        ServiceManager.callQuoteService(QuoteActivity.this, quoteObject, new UIListener() {

                            @Override
                            public void onSuccess() {
                                progressDialog.dismiss();

                                ContentValues cv = new ContentValues();

                                cv.put("name",productName);
                                cv.put("qprice",quotedPrice);
                                cv.put("type",productType);
                                cv.put("deltype",deliveryType);
                                cv.put("comment",comments);
                                cv.put("quoted", true);
                                cv.put("id",productId);

                                DatabaseHelper dh = new DatabaseHelper(QuoteActivity.this);
                                dh.addTransaction(cv);

                                Intent intent = new Intent(QuoteActivity.this,MainActivity.class);
                                intent.putExtra("FROM_QUOTE_FLOW",Constants.IS_FROM_QUOTE_FLOW);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onFailure() {
                                progressDialog.dismiss();
                                Toast.makeText(QuoteActivity.this,"Server is currently busy",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onConnectionError() {
                                progressDialog.dismiss();
                                Toast.makeText(QuoteActivity.this,"Server is currently busy",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled() {
                                progressDialog.dismiss();
                            }
                        });

                    }else{
                        Toasts.internetUnavailableToast(QuoteActivity.this);
                    }
                }else{
                    Toast.makeText(QuoteActivity.this,"Enter your price",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setTypeface() {

        productNameText.setTypeface(font);
        productPriceText.setTypeface(font);
        buyerNameText.setTypeface(font);
        timer.setTypeface(font);
        sameProduct.setTypeface(font);
        similarProduct.setTypeface(font);
        homeDelivery.setTypeface(font);
        shopVisit.setTypeface(font);
        moreDeals.setTypeface(font);
        quoteNow.setTypeface(font);
    }
}
