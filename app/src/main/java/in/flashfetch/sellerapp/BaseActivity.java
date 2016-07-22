package in.flashfetch.sellerapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import static in.flashfetch.sellerapp.R.style.ToolBarTheme;

/**
 * Created by kranthikumar_b on 7/14/2016.
 */
public class BaseActivity extends AppCompatActivity {

    public ProgressDialog progressDialog;
    public Typeface font;
    private Toolbar toolbar;

    public ProgressDialog getProgressDialog(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return progressDialog;
    }

    public ProgressDialog getProgressDialog(Context context, boolean isCancelable){
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(isCancelable);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return progressDialog;
    }

    public Typeface getTypeface(){
        font = Typeface.createFromAsset(getAssets(),"fonts/Roboto_Medium.ttf");
        return font;
    }

    public Toolbar setUpToolbar(Context context, String title, int navigationDrawableIcon){
        toolbar = new Toolbar(context,null,R.style.ToolBarTheme);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(navigationDrawableIcon);
        toolbar.setMinimumHeight(android.R.attr.actionBarSize);
        toolbar.setBackgroundResource(R.color.ff_green);
        toolbar.setTitleTextColor(Color.WHITE);
        return toolbar;
    }
}
