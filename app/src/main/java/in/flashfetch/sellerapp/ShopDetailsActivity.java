package in.flashfetch.sellerapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import in.flashfetch.sellerapp.CommonUtils.Toasts;
import in.flashfetch.sellerapp.CommonUtils.Utils;
import in.flashfetch.sellerapp.Constants.URLConstants;
import in.flashfetch.sellerapp.Interfaces.UIListener;
import in.flashfetch.sellerapp.Network.PostRequest;
import in.flashfetch.sellerapp.Network.ServiceManager;
import in.flashfetch.sellerapp.Objects.PostParam;
import in.flashfetch.sellerapp.Objects.ShopInfoObject;
import in.flashfetch.sellerapp.Objects.UserProfile;

public class ShopDetailsActivity extends BaseActivity {

    private Context context;
    private TextView shopname,shopid,shopad1,shopad2,shopphone;
    private Button editcat,editInfo;
    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = ShopDetailsActivity.this;

        setContentView(R.layout.activity_shop__det);

        Toolbar toolbar = (Toolbar)findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Shop Details");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        progressDialog = getProgressDialog(context);

        shopname = (TextView) findViewById(R.id.shop_name);
        shopid = (TextView) findViewById(R.id.shop_id);
        shopad1 = (TextView) findViewById(R.id.add_1);
        shopad2 = (TextView) findViewById(R.id.add_2);
        shopphone = (TextView) findViewById(R.id.telephone);

        editInfo = (Button)findViewById(R.id.submit);
        editcat = (Button)findViewById(R.id.editCategories);

        shopname.setText("ShopName " + UserProfile.getShopName(context));
        shopid.setText("Shop ID: " + UserProfile.getShopId(context));
        shopad1.setText("Address Line 1: " + UserProfile.getAddress1(context));
        shopad2.setText("Address Line 2: " +UserProfile.getAddress2(context));
        shopphone.setText("Shop Name: " +UserProfile.getShopPhone(context));

        editcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Contact us for changing your product categories, we will update it after validating");
                builder.setPositiveButton("OK",null);

                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Contact us for changing your shop information, we will update it after validating");
                builder.setPositiveButton("OK",null);

                alertDialog = builder.create();
                alertDialog.show();

            }
        });

        /*submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {

                    ShopInfoObject shopInfoObject = new ShopInfoObject();

                    shopInfoObject.setShopId(shopid.getText().toString());
                    shopInfoObject.setShopName(shopname.getText().toString());
                    shopInfoObject.setShopAddress1(shopad1.getText().toString());
                    shopInfoObject.setShopAddress2(shopad2.getText().toString());
                    shopInfoObject.setShopTelephone(shopphone.getText().toString());

                    if(Utils.isInternetAvailable(context)){

                        progressDialog.show();

                        ServiceManager.callUpdateShopInfoService(context, shopInfoObject, new UIListener() {
                            @Override
                            public void onSuccess() {
                                progressDialog.dismiss();
                                Toast.makeText(context,"Your shop details have been successfully saved",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure() {
                                progressDialog.dismiss();
                                Toasts.serverBusyToast(context);
                            }

                            @Override
                            public void onConnectionError() {

                            }

                            @Override
                            public void onCancelled() {
                                progressDialog.dismiss();
                            }
                        });
                    }else{
                        Toasts.internetUnavailableToast(context);
                    }
                }
            }
        });*/
    }

   /* private boolean validate()
    {
        if(shopname.getText().length()==0||shopid.getText().length()==0||shopphone.getText().length()==0||shopad1.getText().length()==0||shopad2.getText().length()==0) {
            Toast.makeText(this,"One or more fields is empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
