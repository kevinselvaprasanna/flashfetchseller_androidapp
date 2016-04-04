package in.flashfetch.sellerapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import in.flashfetch.sellerapp.Adapters.NotificationAdapter;
import in.flashfetch.sellerapp.Adapters.PagerAdapter;
import in.flashfetch.sellerapp.Objects.Notification;
import in.flashfetch.sellerapp.Objects.UserProfile;
import in.flashfetch.sellerapp.Services.IE_RegistrationIntentService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
       private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    Typeface font;

    ImageView nav_img;
    LinearLayout nav_bg;
    TextView shopname,sellername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* font = Typeface.createFromAsset(getAssets(),
                "fonts/Lato-Medium.ttf");*/

        //TODO: Set typeface for text

        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        PagerSlidingTabStrip pagerSlidingTabStrip = (PagerSlidingTabStrip)findViewById(R.id.tabs);
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager(),this));
        pagerSlidingTabStrip.setViewPager(pager);


      if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(MainActivity.this, IE_RegistrationIntentService.class);
            startService(intent);
        }
        else {
          finish();
      }

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
       /* View header = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        navigationView.addHeaderView(header);*/
        View header = navigationView.getHeaderView(0);

        shopname = (TextView)header.findViewById(R.id.shop_name);
        sellername = (TextView)header.findViewById(R.id.seller_name);
        shopname.setText(UserProfile.getShopName(MainActivity.this));
        sellername.setText(UserProfile.getName(MainActivity.this));

        nav_img = (ImageView)header.findViewById(R.id.nav_img);
        nav_bg = (LinearLayout)header.findViewById(R.id.head_layout);

        //TextView email = (TextView)findViewById(R.id.textView3);
        //email.setText(UserProfile.getEmail(MainActivity.this));


    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();

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
           Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",phone, null));
           startActivity(intent);
        }else if (id == R.id.connect) {
           Dialog dialog = new Dialog(this);
           dialog.setTitle("Connect with Us");
           dialog.setContentView(R.layout.dialog_connect);
           LinearLayout fb = (LinearLayout)dialog.findViewById(R.id.fb);
           LinearLayout twitter = (LinearLayout)dialog.findViewById(R.id.twitter);
           LinearLayout whatsapp = (LinearLayout)dialog.findViewById(R.id.whatsapp);
           fb.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   //connect to fb
               }
           });
           twitter.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   //connect to twitter
               }
           });
           whatsapp.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   //connect to whatsapp
               }
           });
           dialog.show();
       }
        else if (id == R.id.share) {
           Intent intent = new Intent(Intent.ACTION_SEND);
           intent.setType("text/plain");
           intent.putExtra(Intent.EXTRA_TEXT,"I\'m using the app FlashFetch which connected me to online shoppers and increased my sales. You can get it here: https://goo.gl/1EnBro");//https://play.google.com/store/apps/details?id=in.flashfetch.sellerapp&hl=en
           startActivity(intent);
       } else if (id == R.id.update) {
           Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=in.flashfetch.sellerapp&hl=en"));
           startActivity(browserIntent);
       } else if (id == R.id.notifalert) {

       } else if (id==R.id.mlogout){
           logout();
       }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.account) {
            startActivity(new Intent(this,My_Acc.class));
        } else if (id == R.id.helpimp) {
            startActivity(new Intent(this,feedback.class));
        } else if (id == R.id.homepg) {

        } else if (id == R.id.notif) {
            startActivity(new Intent(this,notif.class));

        } else if (id == R.id.reqad) {

            startActivity(new Intent(this,Req_Ads.class));

        } else if (id == R.id.shopdet) {
            startActivity(new Intent(this,Shop_Det.class));

        } else if (id == R.id.logout) {
            logout();
            //TODO: Logout

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("FlashFetch");
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
