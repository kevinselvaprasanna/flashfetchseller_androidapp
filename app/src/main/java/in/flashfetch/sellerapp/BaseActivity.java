package in.flashfetch.sellerapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by kranthikumar_b on 7/14/2016.
 */
public class BaseActivity extends AppCompatActivity {

    public ProgressDialog progressDialog;
    public Typeface font;

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

    protected final void setUpToolbar(String title){
        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle(title);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
