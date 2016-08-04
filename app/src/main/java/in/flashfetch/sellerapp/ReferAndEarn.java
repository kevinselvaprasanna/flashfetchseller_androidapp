package in.flashfetch.sellerapp;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import in.flashfetch.sellerapp.CommonUtils.Toasts;
import in.flashfetch.sellerapp.CommonUtils.Utils;
import in.flashfetch.sellerapp.Interfaces.UIListener;
import in.flashfetch.sellerapp.Network.ServiceManager;
import in.flashfetch.sellerapp.Objects.UserProfile;

/**
 * Created by KRANTHI on 10-07-2016.
 */
public class ReferAndEarn extends BaseActivity implements View.OnClickListener{

    private ImageView whatsApp,gMail,facebook,more;
    private TextView code;
    private ProgressDialog progressDialog;
    private String referralCode = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.refer_earn);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Refer and Earn");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(ReferAndEarn.this,MainActivity.class);
//                startActivity(intent);
//                finish();
                onBackPressed();
            }
        });

        code = (TextView)findViewById(R.id.code);

        progressDialog = getProgressDialog(ReferAndEarn.this);

        whatsApp = (ImageView)findViewById(R.id.whatsApp);
        facebook = (ImageView) findViewById(R.id.facebook);
        gMail = (ImageView) findViewById(R.id.gMail);
        more = (ImageView) findViewById(R.id.more);

        whatsApp.setOnClickListener(this);
        facebook.setOnClickListener(this);
        gMail.setOnClickListener(this);
        more.setOnClickListener(this);

        if(Utils.isInternetAvailable(ReferAndEarn.this)){

            progressDialog.show();

            ServiceManager.callReferralCodeService(ReferAndEarn.this, UserProfile.getShopId(ReferAndEarn.this), new UIListener() {
                @Override
                public void onSuccess() {
                    progressDialog.dismiss();
                    code.setText("Your code : " + UserProfile.getReferralCode(ReferAndEarn.this));
                }

                @Override
                public void onFailure() {
                    progressDialog.dismiss();
                    Toasts.serverBusyToast(ReferAndEarn.this);
                    code.setText("Your code : XXXX");
                }

                @Override
                public void onConnectionError() {
                    progressDialog.dismiss();
                    Toasts.serverBusyToast(ReferAndEarn.this);
                    code.setText("Your code : XXXX");
                }

                @Override
                public void onCancelled() {
                    progressDialog.dismiss();
                    code.setText("Your code : XXXX");
                }
            });
        }else{
            Toasts.internetUnavailableToast(ReferAndEarn.this);
        }
    }

    @Override
    public void onClick(View v) {

        Intent intent;
        intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Your Code : " + referralCode);

        switch (v.getId()){
            case R.id.whatsapp:
                intent.setPackage("com.whatsapp");
                try {
                    startActivity(intent);
                } catch (Exception ex) {
                    Toast.makeText(this, "WhatsApp Not Installed", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.gMail:
                intent.setPackage("com.google.android.gm");
                try {
                    intent.putExtra(Intent.EXTRA_SUBJECT,"Download FlashFetch App");
                    startActivity(intent);
                } catch (Exception ex) {
                    Toast.makeText(this, "Gmail Not Installed", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.facebook:
                intent.setPackage("com.facebook.android");
                try {
                    startActivity(intent);
                } catch (Exception ex) {
                    Toast.makeText(this, "Facebook not Installed", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.more:
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
