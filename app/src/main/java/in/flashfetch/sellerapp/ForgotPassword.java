package in.flashfetch.sellerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Created by KRANTHI on 03-07-2016.
 */
public class ForgotPassword extends BaseActivity {

    private TextInputLayout emailInputLayout;
    private EditText emailText;
    private Button button;
    private TextView registerHere;
    private ProgressDialog progressDialog;
    private LinearLayout forgotPasswordLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.forgot_password);

        Toolbar toolbar = (Toolbar)findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Forgot Password");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ForgotPassword.this,LoginActivity.class);
////                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
                onBackPressed();
            }
        });

        forgotPasswordLayout = (LinearLayout)findViewById(R.id.forgot_password_layout);

        progressDialog = getProgressDialog(ForgotPassword.this);

        registerHere = (TextView)findViewById(R.id.login_forgot);

        emailText = (EditText)findViewById(R.id.forgot_email);
        button = (Button)findViewById(R.id.send_button);

        registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPassword.this,SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.isValidEmail(emailText.getText().toString())){

                    if(Utils.isInternetAvailable(ForgotPassword.this)){

                        progressDialog.show();

                        ServiceManager.callForgotPasswordService(ForgotPassword.this, emailText.getText().toString(), new UIListener() {
                            @Override
                            public void onSuccess() {

                                Toasts.successfullySentMailToast(ForgotPassword.this);

                                progressDialog.dismiss();

                                Intent intent = new Intent(ForgotPassword.this,PasswordVerification.class);
                                intent.putExtra("EMAIL",emailText.getText().toString());
                                intent.putExtra("FROM_FORGOT_PASSWORD_FLOW", Constants.IS_FROM_FORGOT_PASSWORD);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure() {
                                Toasts.notRegisteredEmailToast(ForgotPassword.this);

                                progressDialog.dismiss();
                                registerHere.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onConnectionError() {
                                progressDialog.dismiss();
                                Toasts.serverBusyToast(ForgotPassword.this);
                            }

                            @Override
                            public void onCancelled() {
                                progressDialog.dismiss();
                            }
                        });
                    }else{
                        Toasts.internetUnavailableToast(ForgotPassword.this);
                    }

                }else{
                    Toasts.validEmailToast(ForgotPassword.this);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
