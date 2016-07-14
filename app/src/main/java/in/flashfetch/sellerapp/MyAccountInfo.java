package in.flashfetch.sellerapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Line;

import org.json.JSONObject;

import java.util.ArrayList;

import in.flashfetch.sellerapp.CommonUtils.Toasts;
import in.flashfetch.sellerapp.CommonUtils.Utils;
import in.flashfetch.sellerapp.Constants.URLConstants;
import in.flashfetch.sellerapp.Interfaces.UIListener;
import in.flashfetch.sellerapp.Network.PostRequest;
import in.flashfetch.sellerapp.Network.ServiceManager;
import in.flashfetch.sellerapp.Objects.PostParam;
import in.flashfetch.sellerapp.Objects.UserProfile;

public class MyAccountInfo extends AppCompatActivity {

    TextView name,email,phone;
    EditText edit_phone;
    Button logout,edit;
    private boolean isEdited = false;
    private String existingPhoneNumber, newPhoneNumber;
    private ProgressBar progressBar;
    private LinearLayout accountForm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__acc);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("My Account");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyAccountInfo.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        existingPhoneNumber = UserProfile.getPhone(MyAccountInfo.this);

        accountForm = (LinearLayout)findViewById(R.id.account_form);

        name = (TextView)findViewById(R.id.name);
        email = (TextView)findViewById(R.id.email);
        phone = (TextView)findViewById(R.id.phone);
        edit_phone = (EditText)findViewById(R.id.edit_phone);

        name.setText(UserProfile.getName(MyAccountInfo.this));
        email.setText(UserProfile.getEmail(MyAccountInfo.this));
        phone.setText(UserProfile.getPhone(MyAccountInfo.this));

        progressBar = (ProgressBar)findViewById(R.id.progress_bar);

        edit = (Button)findViewById(R.id.edit);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEdited){
                    edit.setText("Submit");
                    phone.setVisibility(View.GONE);
                    edit_phone.setVisibility(View.VISIBLE);
                    isEdited = true;
                }else{
                    newPhoneNumber = edit_phone.getText().toString();

                    if(TextUtils.isEmpty(newPhoneNumber)){
                        Toast.makeText(MyAccountInfo.this,"Enter your new phone number",Toast.LENGTH_SHORT).show();
                        edit.setText("Edit");
                        phone.setVisibility(View.VISIBLE);
                        edit_phone.setVisibility(View.GONE);
                        phone.setText(existingPhoneNumber);
                        isEdited = false;
                    }else{
                        if(Utils.validatePhoneNumberUpdate(existingPhoneNumber,newPhoneNumber)){
                            if(Utils.checkPhoneNumberLength(newPhoneNumber)){
                                edit.setText("Edit");
                                phone.setVisibility(View.VISIBLE);
                                edit_phone.setVisibility(View.GONE);
                                isEdited = false;

                                if(Utils.isInternetAvailable(MyAccountInfo.this)){

                                    progressBar.setVisibility(View.VISIBLE);
                                    accountForm.setVisibility(View.GONE);

                                    ServiceManager.callUpdateService(MyAccountInfo.this, newPhoneNumber, new UIListener() {
                                        @Override
                                        public void onSuccess() {
                                            progressBar.setVisibility(View.GONE);
                                            accountForm.setVisibility(View.VISIBLE);
                                            phone.setText(newPhoneNumber);
                                            UserProfile.setPhone(newPhoneNumber,MyAccountInfo.this);
                                            Toast.makeText(MyAccountInfo.this, "Your information has been successfully saved", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure() {
                                            progressBar.setVisibility(View.GONE);
                                            accountForm.setVisibility(View.VISIBLE);
                                            phone.setText(existingPhoneNumber);
                                            Toasts.serverBusyToast(MyAccountInfo.this);
                                        }

                                        @Override
                                        public void onConnectionError() {
                                            progressBar.setVisibility(View.GONE);
                                            phone.setText(existingPhoneNumber);
                                            Toasts.serverBusyToast(MyAccountInfo.this);
                                        }

                                        @Override
                                        public void onCancelled() {
                                            progressBar.setVisibility(View.GONE);
                                            phone.setText(existingPhoneNumber);
                                        }
                                    });
                                }else{
                                    Toasts.internetUnavailableToast(MyAccountInfo.this);
                                }
                            }else{
                                edit_phone.setVisibility(View.VISIBLE);
                                phone.setVisibility(View.GONE);
                                edit.setText("Submit");
                                Toast.makeText(MyAccountInfo.this, "Enter correct phone phone number", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            edit_phone.setVisibility(View.VISIBLE);
                            phone.setVisibility(View.GONE);
                            edit.setText("Submit");
                            Toast.makeText(MyAccountInfo.this, "Enter new phone phone number", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        logout = (Button)findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.doLogout(MyAccountInfo.this);
            }
        });
    }
}
