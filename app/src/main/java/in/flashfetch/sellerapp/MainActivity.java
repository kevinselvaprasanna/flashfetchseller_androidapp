package in.flashfetch.sellerapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.File;
import java.io.IOException;

import in.flashfetch.sellerapp.Adapters.PagerAdapter;
import in.flashfetch.sellerapp.CommonUtils.Utils;
import in.flashfetch.sellerapp.Interfaces.UIListener;
import in.flashfetch.sellerapp.Network.ServiceManager;
import in.flashfetch.sellerapp.Objects.UserProfile;
import in.flashfetch.sellerapp.Services.IE_RegistrationIntentService;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static int reqnumber = -1;

    Typeface robotoMedium;
    ImageView nav_img;
    LinearLayout nav_bg;
    TextView shopName, sellerEmail;
    ViewPager pager;
    PagerSlidingTabStrip pagerSlidingTabStrip;
    EditText accessCodeText;
    private ProgressBar progressBar;
    private AlertDialog alertDialog;
    private boolean isAccessAllowed;
    private boolean isFromRegistrationFlow,isFromLoginFlow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("FlashFetch Seller");

//        robotoMedium = Typeface.createFromAsset(getAssets(),"fonts/Roboto_Medium.ttf");

        progressBar = (ProgressBar)findViewById(R.id.progress_bar);

        accessCodeText = (EditText) findViewById(R.id.access_text);

        pager = (ViewPager) findViewById(R.id.pager);
        pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);


        if (Utils.checkPlayServices(MainActivity.this)) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(MainActivity.this, IE_RegistrationIntentService.class);
            startService(intent);
        } else {
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        shopName = (TextView) header.findViewById(R.id.shop_name);
        sellerEmail = (TextView) header.findViewById(R.id.seller_name);

        shopName.setText(UserProfile.getShopName(MainActivity.this));
        sellerEmail.setText(UserProfile.getName(MainActivity.this));

        nav_img = (ImageView) header.findViewById(R.id.nav_img);
        nav_bg = (LinearLayout) header.findViewById(R.id.head_layout);

        setTypeface();

        if(isFromRegistrationFlow){
            showAccessDialog();
        }else if(isFromLoginFlow){
            if(!isAccessAllowed){
                showAccessDialog();
            }
        }

        pager.setAdapter(new PagerAdapter(getSupportFragmentManager(), this));
        pagerSlidingTabStrip.setViewPager(pager);
    }

    private void checkAccessCode() {

        progressBar.setVisibility(View.VISIBLE);

        ServiceManager.callAccessCodeVerificationService(MainActivity.this,UserProfile.getToken(MainActivity.this), UserProfile.getEmail(MainActivity.this), accessCodeText.getText().toString(), new UIListener() {
            @Override
            public void onSuccess() {
                UserProfile.setAccess(true,MainActivity.this);
                progressBar.setVisibility(View.GONE);
                alertDialog.dismiss();
                Toast.makeText(MainActivity.this,"Hurray! You are ready to receive your orders",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure() {
                UserProfile.setAccess(false,MainActivity.this);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,"Enter correct access code",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onConnectionError() {
                UserProfile.setAccess(false,MainActivity.this);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,"Server is currently busy please try again after sometime",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled() {
                UserProfile.setAccess(false,MainActivity.this);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @SuppressLint("NewApi")
    private void showAccessDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Enter Access Code");
        builder.setView(R.layout.alert_edit_text);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkAccessCode();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                UserProfile.setAccess(false,MainActivity.this);

//                pager.getChildAt(0).findViewById(R.id.rvNotifications).setVisibility(View.GONE);
//                pager.getChildAt(1).findViewById(R.id.rvNotifications).setVisibility(View.GONE);
//                pager.getChildAt(2).findViewById(R.id.rvNotifications).setVisibility(View.GONE);
//
//                pager.getChildAt(0).findViewById(R.id.no_access_text).setVisibility(View.VISIBLE);
//                pager.getChildAt(1).findViewById(R.id.no_access_text).setVisibility(View.VISIBLE);
//                pager.getChildAt(2).findViewById(R.id.no_access_text).setVisibility(View.VISIBLE);

            }
        });

        alertDialog = builder.create();
        alertDialog.show();
    }

    private void setTypeface() {
        shopName.setTypeface(robotoMedium);
        sellerEmail.setTypeface(robotoMedium);
    }

    public void accessAllowed() {
        pager.setOffscreenPageLimit(2);
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager(), this));
        pagerSlidingTabStrip.setViewPager(pager);
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
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.contact) {
            String phone = "+919940126973";//Insert number here
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
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Flashfetch-140606842997095/"));//Insert FB page link
                    startActivity(browserIntent);
                }
            });
            twitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/flashfetch"));//Insert twitter link
                    startActivity(browserIntent);
                }
            });
            whatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                    intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                    intent.putExtra(ContactsContract.Intents.Insert.PHONE, "+919940126973")
                            .putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                            .putExtra(ContactsContract.Intents.Insert.NAME, "FlashFetch");
                    startActivity(intent);
                }
            });
            dialog.show();
        } else if (id == R.id.rateUs) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=in.flashfetch.sellerapp&hl=en"));
            startActivity(browserIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.account) {
            startActivity(new Intent(this, MyAccountInfo.class));
        } else if (id == R.id.helpimp) {
            startActivity(new Intent(this, FeedBackActivity.class));
        } else if (id == R.id.notif) {
            startActivity(new Intent(this, notif.class));

        } else if (id == R.id.reqad) {

            startActivity(new Intent(this, RequestAds.class));

        } else if (id == R.id.shopdet) {
            startActivity(new Intent(this, ShopDetails.class));

        }else if(id == R.id.referAndEarn) {
            startActivity(new Intent(this,ReferAndEarn.class));
        }else if(id == R.id.rewardPoints) {
            startActivity(new Intent(this,RewardActivity.class));
        }else if(id == R.id.demo) {
            startActivity(new Intent(this,DemoActivity.class));
        }else if (id == R.id.logout) {
            logout();
            //TODO: Logout

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        //Creating ok button with listener
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("Alert", " Ok");
                clearApplicationData();
//                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences prefs = getSharedPreferences("sharedPreferences", 0);
                prefs.edit().putString("delete", "hellothisisacheck").apply();
                Log.d("delete", prefs.getString("delete", "nope"));
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.commit();
                UserProfile.clear(MainActivity.this);
                Log.d("delete", prefs.getString("delete", "nope"));
                GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(getBaseContext());
                try {
                    gcm.unregister();
                } catch (IOException e) {
                    System.out.println("Error Message: " + e.getMessage());
                }
                Intent i = new Intent(MainActivity.this, StartActivity.class);
                startActivity(i);
                finish();

            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "File /data/data/APP_PACKAGE/" + s + " DELETED");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }
}
