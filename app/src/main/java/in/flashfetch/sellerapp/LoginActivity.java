package in.flashfetch.sellerapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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

import in.flashfetch.sellerapp.CommonUtils.Toasts;
import in.flashfetch.sellerapp.CommonUtils.Utils;
import in.flashfetch.sellerapp.Constants.Constants;
import in.flashfetch.sellerapp.Interfaces.UIListener;
import in.flashfetch.sellerapp.Network.ServiceManager;
import in.flashfetch.sellerapp.Objects.UserProfile;
import in.flashfetch.sellerapp.Services.IE_RegistrationIntentService;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    //private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Button submitButton;
    private View mProgressView,mLoginFormView,mHideView;
    private TextView forgotPassword, registerHere;
    Typeface font;

    //TODO: Set typeface for text

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);


        if(UserProfile.getEmail(LoginActivity.this) != "") {
            Intent intent =new Intent(LoginActivity.this,MainActivity.class);
            intent.putExtra("FROM_LOGIN", Constants.IS_FROM_LOGIN_FLOW);
            startActivity(intent);
            finish();
            /*if (checkPlayServices()) {
                // Start IntentService to register this application with GCM.
                Intent intent = new Intent(LoginActivity.this, IE_RegistrationIntentService.class);
                startService(intent);
            }
            finish();*/
        }


        /*font = Typeface.createFromAsset(getAssets(),
                "fonts/Lato-Medium.ttf");*/
        setContentView(R.layout.login_main);
        // Set up the login form.

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setTitle("FlashFetch Seller");
        toolbar.setNavigationIcon(R.drawable.toolbaricon24);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.EmailID);
        populateAutoComplete();

        //checkPlayServices();

       /* if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(LoginActivity.this, IE_RegistrationIntentService.class);
            startService(intent);
        }*/

        mPasswordView = (EditText) findViewById(R.id.Password);
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

        forgotPassword = (TextView)findViewById(R.id.forgot_pass);

        forgotPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,ForgotPassword.class);
                startActivity(intent);
            }
        });

        submitButton = (Button) findViewById(R.id.submit);
        submitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        registerHere = (TextView)findViewById(R.id.login_register);

        registerHere.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.progress_bar);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
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
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
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
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mProgressView.setVisibility(View.VISIBLE);
            mLoginFormView.setVisibility(View.GONE);

            ServiceManager.callLoginService(LoginActivity.this, email, password, new UIListener() {
                @Override
                public void onSuccess() {
                    mProgressView.setVisibility(View.GONE);
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra("FROM_LOGIN", Constants.IS_FROM_LOGIN_FLOW);
                    startActivity(intent);
                    intent = new Intent(LoginActivity.this, IE_RegistrationIntentService.class);
                    startService(intent);
                }

                @Override
                public void onFailure() {
                    mProgressView.setVisibility(View.GONE);
                    mLoginFormView.setVisibility(View.VISIBLE);
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }

                @Override
                public void onConnectionError() {
                    mProgressView.setVisibility(View.GONE);
                    mLoginFormView.setVisibility(View.VISIBLE);
                    Toasts.internetUnavailableToast(LoginActivity.this);
                }

                @Override
                public void onCancelled(){
                    mProgressView.setVisibility(View.GONE);
                    mLoginFormView.setVisibility(View.VISIBLE);
                    Toasts.serviceInterrupted(LoginActivity.this);
                }
            });
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 0;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
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
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

//    /**
//     * Represents an asynchronous login/registration task used to authenticate
//     * the user.
//     */
//    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
//
//        private final String mEmail;
//        JSONObject ResponseJSON;
//        private final String mPassword;
//
//        UserLoginTask(String email, String password) {
//            mEmail = email;
//            mPassword = password;
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            // TODO: attempt authentication against a network service.
//
//
//          /*  for (String credential : DUMMY_CREDENTIALS) {
//                String[] pieces = credential.split("check@check.com:hello");
//                if (pieces[0].equals(mEmail)) {
//                    // Account exists, return true if the password matches.
//                    return pieces[1].equals(mPassword);
//                }
//            }*/
//
//            ArrayList<PostParam> iPostParams = new ArrayList<PostParam>();
//            PostParam postemail = new PostParam("email", mEmail);
//            PostParam postpassword = new PostParam("pass",mPassword);
//            iPostParams.add(postemail);
//            iPostParams.add(postpassword);
//            //ResponseJSON = PostRequest.execute("http://192.168.43.66/login_buyer.php", iPostParams, null);
//            ResponseJSON = PostRequest.execute(URLConstants.URLLogin, iPostParams, null);
//            Log.d("RESPONSE", ResponseJSON.toString());
//            try {
//                if(ResponseJSON.getJSONObject("data").getInt("result")==1) {
//                    JSONObject trans = new JSONObject();
//                    JSONObject translist = ResponseJSON.getJSONObject("data").getJSONObject("Transactions");
//                    NotificationsActivity not;
//                    for (int i=0; i<ResponseJSON.getJSONObject("data").getInt("length"); i++){
//                        trans = translist.getJSONObject(String.valueOf(i));
//                        not = new NotificationsActivity(trans);
////                        Log.d(LOG_TAG, "Category of event is " + event.getCate)
//                        not.saveNot(LoginActivity.this);
//                    }
//                    UserProfile.setEmail(mEmail, LoginActivity.this);
//                    UserProfile.setCategory(ResponseJSON.getJSONObject("data").getInt("cat"), LoginActivity.this);
//                    UserProfile.setToken(ResponseJSON.getJSONObject("data").getString("token"), LoginActivity.this);
//                    UserProfile.setName(ResponseJSON.getJSONObject("data").getString("user"), LoginActivity.this);
//                    UserProfile.setPhone(ResponseJSON.getJSONObject("data").getString("mobile"), LoginActivity.this);
//                    UserProfile.setPassword(ResponseJSON.getJSONObject("data").getString("password"), LoginActivity.this);
//                    UserProfile.setShopId(ResponseJSON.getJSONObject("data").getString("shopid"), LoginActivity.this);
//                    UserProfile.setShopPhone(ResponseJSON.getJSONObject("data").getString("office_no"), LoginActivity.this);
//                    UserProfile.setAddress1(ResponseJSON.getJSONObject("data").getString("Address1"), LoginActivity.this);
//                    UserProfile.setAddress2(ResponseJSON.getJSONObject("data").getString("Address2"), LoginActivity.this);
//                    UserProfile.setShopName(ResponseJSON.getJSONObject("data").getString("shopName"), LoginActivity.this);
//                    UserProfile.setLocation(ResponseJSON.getJSONObject("data").getString("sel_loc"), LoginActivity.this);
//                    return true;
//                }
//                else if(ResponseJSON.getJSONObject("data").getInt("result")==0) {
//                    return false;
//                }
//              /*  else if (mEmail.equals("abc@def")&&mPassword.equals("123456"))
//                {
//                    return true;
//                }*/
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            return false;
//            // TODO: register the new account here.
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean success) {
//            mAuthTask = null;
//            showProgress(false);
//
//            try {
//                if (ResponseJSON.getJSONObject("data").getInt("result")==1) {
//                   /* Intent i = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(i);*/
//                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
////                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                    intent.putExtra("LOGIN", true);
//                    startActivity(intent);
//                    intent = new Intent(LoginActivity.this, IE_RegistrationIntentService.class);
//                    startService(intent);
//                    finish();
//                } else if(ResponseJSON.getJSONObject("data").getInt("result")==0){
//                    mPasswordView.setError(getString(R.string.error_incorrect_password));
//                    mPasswordView.requestFocus();
//                }else {
//                    Toast.makeText(LoginActivity.this,"Network connection unavailable",Toast.LENGTH_LONG).show();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        //checkPlayServices();
    }
}

