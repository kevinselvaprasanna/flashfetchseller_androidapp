package in.flashfetch.sellerapp;

import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends BaseActivity implements LoaderCallbacks<Cursor> {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Button submitButton;
    private View mLoginFormView;
    private TextView forgotPassword, registerHere;
    private Typeface font;
    private ProgressDialog progressDialog;
    private Handler handler;
    private long lastUpdateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            TimeUnit.MILLISECONDS.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        handler = new Handler();

        /* Get Last Update Time from Preferences */
        SharedPreferences prefs = this.getPreferences(0);
        lastUpdateTime = prefs.getLong("lastUpdateTime", 0);

        /* Should Activity Check for Updates Now? */
        if ((lastUpdateTime + (24 * 60 * 60 * 1000)) < System.currentTimeMillis()) {

            Log.d("Check for update","UpdateChecked");
        /* Save current timestamp for next Check*/
            lastUpdateTime = System.currentTimeMillis();
            SharedPreferences.Editor editor = getPreferences(0).edit();
            editor.putLong("lastUpdateTime", lastUpdateTime);
            editor.commit();

        /* Start Update */
            checkUpdate.start();
        }

        if (UserProfile.getEmail(LoginActivity.this) != "") {

            if (UserProfile.getCategory(LoginActivity.this) == 1) {
                Intent i = new Intent(LoginActivity.this, CategoryActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            } else {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("FROM_LOGIN", Constants.IS_FROM_LOGIN_FLOW);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }

        setContentView(R.layout.login_main);

        Utils.startPlayServices(this);

        font = getTypeface();

        progressDialog = getProgressDialog(this);

        mLoginFormView = findViewById(R.id.login_form);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.EmailID);
        mPasswordView = (EditText) findViewById(R.id.Password);
        forgotPassword = (TextView) findViewById(R.id.forgot_pass);
        submitButton = (Button) findViewById(R.id.submit);
        registerHere = (TextView) findViewById(R.id.login_register);

        mEmailView.clearFocus();

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

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);

            if (Utils.isInternetAvailable(LoginActivity.this)) {

                progressDialog.show();
                mLoginFormView.setVisibility(View.GONE);

                ServiceManager.callLoginService(LoginActivity.this, email, password, new UIListener() {
                    @Override
                    public void onSuccess() {
                        progressDialog.dismiss();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("FROM_LOGIN", Constants.IS_FROM_LOGIN_FLOW);
                        startActivity(intent);
                        finish();
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
                        Toast.makeText(LoginActivity.this, "Check your username and password", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled() {
                        progressDialog.dismiss();
                        mLoginFormView.setVisibility(View.VISIBLE);
                        Toasts.serviceInterrupted(LoginActivity.this);
                    }
                });
            } else {
                Toasts.internetUnavailableToast(LoginActivity.this);
            }
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8;
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(LoginActivity.this, android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {ContactsContract.CommonDataKinds.Email.ADDRESS, ContactsContract.CommonDataKinds.Email.IS_PRIMARY,};

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /* This Thread checks for Updates in the Background */
    private Thread checkUpdate = new Thread() {
        public void run() {
            try {
                //TODO: Change the App update URL
                URL updateURL = new URL(Constants.PLAY_STORE_URL);
                URLConnection conn = updateURL.openConnection();
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                FileOutputStream baf = new FileOutputStream(new File(Environment.getExternalStorageDirectory() + File.separator + "test.txt"));

                int current = 0;
                while ((current = bis.read()) != -1) {
                    baf.write((byte) current);
                }
                baf.close();

            /* Convert the Bytes read to a String. */
                final String s = new String(baf.toString());

            /* Get current Version Number */
                int curVersion = getPackageManager().getPackageInfo("in.flashfetch.sellerapp", 0).versionCode;
                int newVersion = Integer.valueOf(s);

            /* Is a higher version than the current already out? */
                if (newVersion > curVersion) {
                /* Post a Handler for the UI to pick up and open the Dialog */
                    handler.post(showUpdate);
                }
            } catch (Exception e) {
            }
        }
    };

    /* This Runnable creates a Dialog and asks the user to open the Market */
    private Runnable showUpdate = new Runnable() {
        public void run() {
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("Update Available")
                    .setMessage("Mandatory Update for the App")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.PLAY_STORE_URL));
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            finish();
                        }
                    })
                    .show();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        //checkPlayServices();
    }
}

