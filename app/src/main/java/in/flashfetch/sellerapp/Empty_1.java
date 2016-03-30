package in.flashfetch.sellerapp;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import in.flashfetch.sellerapp.Network.Connectivity;
import in.flashfetch.sellerapp.Objects.UserProfile;

public class Empty_1 extends AppCompatActivity {

    TextView box;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);

        webView = (WebView)findViewById(R.id.webView);

        webView.getSettings().setAppCacheMaxSize(5 * 1024 * 1024); // 5MB
        webView.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        if(Connectivity.isNetworkAvailable(Empty_1.this)) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); // load online by default
        }
        else { // loading offline
            webView.getSettings().setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK );
        }
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://www.flashfetch.in");
       /* box = (TextView)findViewById(R.id.box);
        box.setText(UserProfile.getText(Empty_1.this));*/
    }
    /*private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService( CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.empty_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.empty_contact)
        {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "8754556606"));
            startActivity(intent);
            //Contact
        }
        return super.onOptionsItemSelected(item);
    }
}
