package in.flashfetch.sellerapp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;

import java.util.concurrent.TimeUnit;

import in.flashfetch.sellerapp.Adapters.DealsPagerAdapter;
import in.flashfetch.sellerapp.Animations.ZoomOutPageTransformer;
import in.flashfetch.sellerapp.CommonUtils.Toasts;
import in.flashfetch.sellerapp.CommonUtils.Utils;
import in.flashfetch.sellerapp.Constants.Constants;
import in.flashfetch.sellerapp.Interfaces.UIListener;
import in.flashfetch.sellerapp.Network.ServiceManager;
import in.flashfetch.sellerapp.Objects.UserProfile;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Typeface font;
    private ImageView nav_img;
    private LinearLayout nav_bg;
    private TextView shopName, sellerEmail;
    private ViewPager pager;
    private PagerSlidingTabStrip pagerSlidingTabStrip;
    private EditText accessCodeText;
    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;
    private boolean isAccessAllowed;
    private boolean isFromRegistrationFlow,isFromLoginFlow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            TimeUnit.MILLISECONDS.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Utils.startPlayServices(this);

        Bundle bundle = getIntent().getExtras();

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("FlashFetch Seller");
        }

        font = getTypeface();

        progressDialog = getProgressDialog(this);

        pager = (ViewPager) findViewById(R.id.pager);
        pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id. drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.setDrawerListener(toggle);
        }
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }

        View header = navigationView.getHeaderView(0);

        shopName = (TextView) header.findViewById(R.id.shop_name);
        sellerEmail = (TextView) header.findViewById(R.id.seller_name);

        shopName.setText(UserProfile.getShopName(MainActivity.this));
        sellerEmail.setText(UserProfile.getName(MainActivity.this));

        nav_img = (ImageView) header.findViewById(R.id.nav_img);
        nav_bg = (LinearLayout) header.findViewById(R.id.head_layout);

        setTypeface();

        if(bundle != null){
            if(bundle.getBoolean("FROM_LOGIN",false)){
                isFromLoginFlow = true;
                isAccessAllowed = UserProfile.getAccess(MainActivity.this);
            }else if(bundle.getBoolean("FROM_REGISTRATION",false)){
                isFromRegistrationFlow = true;
            }else{
                isAccessAllowed = UserProfile.getAccess(MainActivity.this);
            }
        }

        if(isFromRegistrationFlow){
            showAccessDialog();
        }else if(isFromLoginFlow){
            if(!isAccessAllowed){
                showAccessDialog();
            }
        }

        pager.setAdapter(new DealsPagerAdapter(getSupportFragmentManager(), this, UserProfile.getAccess(this)));
        pager.setPageTransformer(false,new ZoomOutPageTransformer());
        pager.setOffscreenPageLimit(3);
        pagerSlidingTabStrip.setViewPager(pager);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void showAccessDialog() {

        View view = getLayoutInflater().inflate(R.layout.alert_edit_text,null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Enter Access Code");
        builder.setView(view);

        accessCodeText = (EditText)view.findViewById(R.id.alert_edit_text);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!TextUtils.isEmpty(accessCodeText.getText())) {

                    if(Utils.isInternetAvailable(MainActivity.this)){

                        progressDialog.show();

                        ServiceManager.callAccessCodeVerificationService(MainActivity.this,UserProfile.getToken(MainActivity.this), UserProfile.getEmail(MainActivity.this), accessCodeText.getText().toString(), new UIListener() {
                            @Override
                            public void onSuccess() {
                                UserProfile.setAccess(true,MainActivity.this);
                                progressDialog.dismiss();
                                alertDialog.dismiss();
                                Toast.makeText(MainActivity.this,"Hurray! You are ready to receive your orders",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure() {
                                UserProfile.setAccess(false,MainActivity.this);
                                progressDialog.dismiss();
                                alertDialog.show();
                                Toast.makeText(MainActivity.this,"Enter correct access code",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onConnectionError() {
                                UserProfile.setAccess(false,MainActivity.this);
                                progressDialog.dismiss();
                                alertDialog.show();
                                Toast.makeText(MainActivity.this,"Server is currently busy please try again after sometime",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled() {
                                UserProfile.setAccess(false,MainActivity.this);
                                progressDialog.dismiss();
                                alertDialog.show();
                            }
                        });
                    }else{
                        Toasts.internetUnavailableToast(MainActivity.this);
                    }

                }else{
                    Toast.makeText(MainActivity.this,"Enter your access code",Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                UserProfile.setAccess(false,MainActivity.this);
            }
        });

        alertDialog = builder.create();
        alertDialog.show();
    }

    private void setTypeface() {
        shopName.setTypeface(font);
        sellerEmail.setTypeface(font);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.checkPlayServices(MainActivity.this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.contact) {

            String phone = Constants.CONTACT_NUMBER;
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            startActivity(intent);

        } else if (id == R.id.connect) {

            Dialog dialog = new Dialog(this);
            dialog.setTitle("Connect with Us");
            dialog.setContentView(R.layout.dialog_connect);

            LinearLayout fb = (LinearLayout) dialog.findViewById(R.id.fb);
            LinearLayout twitter = (LinearLayout) dialog.findViewById(R.id.twitter);
            LinearLayout whatsapp = (LinearLayout) dialog.findViewById(R.id.whatsapp);

            fb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.FACEBOOK_URL));
                    startActivity(browserIntent);
                }
            });
            twitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.TWITTER_URL));
                    startActivity(browserIntent);
                }
            });
            whatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                    intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                    intent.putExtra(ContactsContract.Intents.Insert.PHONE, Constants.CONTACT_NUMBER)
                            .putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                            .putExtra(ContactsContract.Intents.Insert.NAME, Constants.APP_NAME);
                    startActivity(intent);
                }
            });
            dialog.show();

        } else if (id == R.id.rateUs) {
            Intent playStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.PLAY_STORE_URL));
            startActivity(playStoreIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.account) {
            startActivity(new Intent(this, MyAccountInfo.class));
        } else if (id == R.id.helpimp) {
            startActivity(new Intent(this, FeedBackActivity.class));
        } else if (id == R.id.notif) {
            startActivity(new Intent(this, NotificationsActivity.class));
        } else if (id == R.id.reqad) {
            startActivity(new Intent(this, RequestAds.class));
        } else if (id == R.id.shopdet) {
            startActivity(new Intent(this, ShopDetailsActivity.class));
        }else if(id == R.id.referAndEarn) {
            startActivity(new Intent(this,ReferAndEarn.class));
        }else if(id == R.id.rewardPoints) {
            startActivity(new Intent(this,RewardActivity.class));
        }else if(id == R.id.demo) {
            startActivity(new Intent(this,DemoActivity.class));
        }else if (id == R.id.logout) {

            progressDialog.setMessage("Logging out...");

            Utils.doLogout(MainActivity.this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
