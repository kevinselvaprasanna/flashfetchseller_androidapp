package in.flashfetch.sellerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.flashfetch.sellerapp.CommonUtils.Toasts;
import in.flashfetch.sellerapp.CommonUtils.Utils;
import in.flashfetch.sellerapp.Constants.Constants;
import in.flashfetch.sellerapp.Constants.URLConstants;
import in.flashfetch.sellerapp.Interfaces.UIListener;
import in.flashfetch.sellerapp.Network.PostRequest;
import in.flashfetch.sellerapp.Network.ServiceManager;
import in.flashfetch.sellerapp.Objects.PostParam;

import static in.flashfetch.sellerapp.Constants.Constants.IS_FROM_FORGOT_PASSWORD;

/**
 * Created by KRANTHI on 03-07-2016.
 */
public class PasswordVerification extends BaseActivity {

    private String email;
    private EditText verificationEditText;
    private Button submitButton;
    private ProgressDialog progressDialog;
    private LinearLayout passwordVerificationLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.password_verification);

        Bundle bundle =getIntent().getExtras();
        if(bundle != null && bundle.getBoolean("FROM_FORGOT_PASSWORD_FLOW")) {
            email = bundle.getString("EMAIL");
        }

        Toolbar toolbar = (Toolbar)findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Password Verification");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(PasswordVerification.this,ForgotPassword.class);
//                startActivity(intent);
                onBackPressed();
            }
        });

        passwordVerificationLayout = (LinearLayout)findViewById(R.id.password_verification_layout);
        progressDialog = getProgressDialog(PasswordVerification.this);
        verificationEditText = (EditText)findViewById(R.id.verification_edit_text);
        submitButton = (Button)findViewById(R.id.submit_verification_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!TextUtils.isEmpty(verificationEditText.getText().toString())){

                    if(Utils.isInternetAvailable(PasswordVerification.this)) {

                        progressDialog.show();

                        ServiceManager.callPasswordVerificationService(PasswordVerification.this, email, verificationEditText.getText().toString(), new UIListener() {
                            @Override
                            public void onSuccess() {
                                Toasts.verificationCodeSuccessfullyVerified(PasswordVerification.this);
                                progressDialog.dismiss();

                                Intent intent = new Intent(PasswordVerification.this, ChangePassword.class);
                                intent.putExtra("EMAIL", email);
                                intent.putExtra("FROM_PASSWORD_VERIFICATION_FLOW", Constants.IS_FROM_PASSWORD_VERIFICATION);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure() {
                                Toasts.enterValidVerificationCode(PasswordVerification.this);
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onConnectionError() {
                                Toasts.serverBusyToast(PasswordVerification.this);
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onCancelled() {
                                progressDialog.dismiss();
                            }
                        });
                    }else{
                        Toasts.internetUnavailableToast(PasswordVerification.this);
                    }
                }else{
                    Toasts.enterVerificationCode(PasswordVerification.this);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
