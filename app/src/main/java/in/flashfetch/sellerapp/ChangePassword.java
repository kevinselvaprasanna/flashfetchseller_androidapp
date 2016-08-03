package in.flashfetch.sellerapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import in.flashfetch.sellerapp.CommonUtils.Toasts;
import in.flashfetch.sellerapp.CommonUtils.Utils;
import in.flashfetch.sellerapp.Interfaces.UIListener;
import in.flashfetch.sellerapp.Network.ServiceManager;
import in.flashfetch.sellerapp.Objects.UserProfile;

/**
 * Created by KRANTHI on 03-07-2016.
 */

public class ChangePassword extends AppCompatActivity {

    private EditText newPassword, confirmPassword;
    private Button changePassword;
    private String email;
    private ProgressBar progressBar;
    private LinearLayout changePasswordLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.change_password);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getBoolean("FROM_PASSWORD_VERIFICATION_FLOW")) {
            email = bundle.getString("EMAIL");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ChangePassword.this, PasswordVerification.class);
//                  startActivity(intent);
                onBackPressed();
            }
        });

        changePasswordLayout = (LinearLayout) findViewById(R.id.change_password_layout);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        newPassword = (EditText) findViewById(R.id.forgot_new_password);
        confirmPassword = (EditText) findViewById(R.id.forgot_confirm_password);
        changePassword = (Button) findViewById(R.id.change_password);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(newPassword.getText().toString()) || TextUtils.isEmpty(confirmPassword.getText().toString())) {

                    Toasts.emptyPasswordToast(ChangePassword.this);

                } else {
                    if (Utils.checkSamePassword(newPassword.getText().toString(), confirmPassword.getText().toString())) {

                        if (Utils.isInternetAvailable(ChangePassword.this)) {

                            progressBar.setVisibility(View.VISIBLE);
                            changePasswordLayout.setVisibility(View.GONE);

                            ServiceManager.callPasswordChangeService(ChangePassword.this, email, newPassword.getText().toString(), new UIListener() {

                                @Override
                                public void onSuccess() {
                                    Toasts.successfulPasswordChangeToast(ChangePassword.this);

                                    if (UserProfile.getCategory(ChangePassword.this) == 1) {
                                        Intent i = new Intent(ChangePassword.this, CategoryActivity.class);
//                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                    } else {
                                        Intent i = new Intent(ChangePassword.this, MainActivity.class);
//                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        finish();
                                    }
                                }

                                @Override
                                public void onFailure() {
                                    progressBar.setVisibility(View.GONE);
                                    changePasswordLayout.setVisibility(View.VISIBLE);
                                    Toasts.serverBusyToast(ChangePassword.this);
                                }

                                @Override
                                public void onConnectionError() {
                                    progressBar.setVisibility(View.GONE);
                                    changePasswordLayout.setVisibility(View.VISIBLE);
                                    Toasts.serverBusyToast(ChangePassword.this);
                                }

                                @Override
                                public void onCancelled() {
                                    progressBar.setVisibility(View.GONE);
                                    changePasswordLayout.setVisibility(View.VISIBLE);
                                }
                            });
                        } else {
                            Toasts.internetUnavailableToast(ChangePassword.this);
                        }
                    } else {
                        Toasts.passwordNotSameToast(ChangePassword.this);
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
