package in.flashfetch.sellerapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import in.flashfetch.sellerapp.CommonUtils.Toasts;
import in.flashfetch.sellerapp.CommonUtils.Utils;
import in.flashfetch.sellerapp.Constants.Constants;
import in.flashfetch.sellerapp.Interfaces.UIListener;
import in.flashfetch.sellerapp.Network.ServiceManager;
import in.flashfetch.sellerapp.Objects.UserProfile;
import in.flashfetch.sellerapp.Services.IE_RegistrationIntentService;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends BaseActivity implements LoaderCallbacks<Cursor> {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Button submitButton;
    private View mProgressView, mLoginFormView;
    private TextView forgotPassword, registerHere;
    private Typeface font;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            TimeUnit.MILLISECONDS.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        if (UserProfile.getEmail(LoginActivity.this) != "") {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("FROM_LOGIN", Constants.IS_FROM_LOGIN_FLOW);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.login_main);

        Utils.startPlayServices(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("FlashFetch Seller");
        toolbar.setNavigationIcon(R.drawable.toolbar_30);

        font = getTypeface();

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.progress_bar);

        progressDialog = showProgressDialog(this);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.EmailID);
        mPasswordView = (EditText) findViewById(R.id.Password);
        forgotPassword = (TextView) findViewById(R.id.forgot_pass);
        submitButton = (Button) findViewById(R.id.submit);
        registerHere = (TextView) findViewById(R.id.login_register);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        forgotPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        submitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        registerHere.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void attemptLogin() {

        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!Utils.isValidEmail(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            progressDialog.show();
            mLoginFormView.setVisibility(View.GONE);

            ServiceManager.callLoginService(LoginActivity.this, email, password, new UIListener() {
                @Override
                public void onSuccess() {
                    progressDialog.dismiss();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("FROM_LOGIN", Constants.IS_FROM_LOGIN_FLOW);
                    startActivity(intent);
                    intent = new Intent(LoginActivity.this, IE_RegistrationIntentService.class);
                    startService(intent);
                }

                @Override
                public void onFailure() {
                    progressDialog.dismiss();
                    mLoginFormView.setVisibility(View.VISIBLE);
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }

                @Override
                public void onConnectionError() {
                    progressDialog.dismiss();
                    mLoginFormView.setVisibility(View.VISIBLE);
                    Toasts.internetUnavailableToast(LoginActivity.this);
                }

                @Override
                public void onCancelled() {
                    progressDialog.dismiss();
                    mLoginFormView.setVisibility(View.VISIBLE);
                    Toasts.serviceInterrupted(LoginActivity.this);
                }
            });
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 0;
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    private boolean mayRequestContacts() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, Constants.REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, Constants.REQUEST_READ_CONTACTS);
        }
        return false;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {ContactsContract.CommonDataKinds.Email.ADDRESS, ContactsContract.CommonDataKinds.Email.IS_PRIMARY,};

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //checkPlayServices();
    }
}

