package in.flashfetch.sellerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import in.flashfetch.sellerapp.CommonUtils.Toasts;
import in.flashfetch.sellerapp.CommonUtils.Utils;
import in.flashfetch.sellerapp.Constants.Constants;
import in.flashfetch.sellerapp.Interfaces.UIListener;
import in.flashfetch.sellerapp.Network.ServiceManager;
import in.flashfetch.sellerapp.Objects.SignUpObject;
import in.flashfetch.sellerapp.Objects.UserProfile;
import in.flashfetch.sellerapp.Services.IE_RegistrationIntentService;

public class SignUpActivity extends BaseActivity {

    private EditText name, shopName, email, phone,password, confirmPassword, shopId, shopTelephone, address1, address2, referralCode;
    private Button submitButton, nextButton, backButton;
    private TextView loc_gps,shopimg;
    private ViewFlipper viewFlipper;
    private String selectedImagePath;
    private View firstView;
    private Typeface font;
    private Uri selectedImageUri;
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int SELECT_PHOTO = 2;
    private ProgressDialog progressDialog;
    private String shopLocation = null;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private static final LatLngBounds BOUNDS_CHENNAI = new LatLngBounds(new LatLng(13.080680,80.260718),new LatLng(13.082680, 80.270718) );
    private SignUpObject signUpObject = new SignUpObject();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Registration");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewFlipper.getCurrentView() == firstView){
//                    Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
//                    startActivity(intent);
//                    finish();
                    onBackPressed();
                }else{
                    viewFlipper.showPrevious();
                }
            }
        });

        progressDialog = getProgressDialog(SignUpActivity.this);

        viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper2);

        firstView = (View)findViewById(R.id.first_view);

        name = (EditText)findViewById(R.id.Name);
        email = (EditText)findViewById(R.id.EmailID);
        phone = (EditText)findViewById(R.id.MobileNum);
        password = (EditText)findViewById(R.id.Password);
        confirmPassword = (EditText)findViewById(R.id.confPass);
        referralCode = (EditText)findViewById(R.id.referral_code);

        shopName = (EditText)findViewById(R.id.shop_name);
        shopId = (EditText)findViewById(R.id.shop_id);
        shopTelephone = (EditText)findViewById(R.id.telephone);
        address1 = (EditText)findViewById(R.id.add_1);
        address2 = (EditText)findViewById(R.id.add_2);

        loc_gps = (TextView)findViewById(R.id.location);
        shopimg = (TextView)findViewById(R.id.shop_img);

        nextButton = (Button)findViewById(R.id.Next);
        backButton = (Button)findViewById(R.id.back);
        submitButton = (Button)findViewById(R.id.next);

        loc_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                builder.setLatLngBounds(BOUNDS_CHENNAI);

                try {
                    startActivityForResult(builder.build(SignUpActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        shopimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate1()) {
                    viewFlipper.showNext();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewFlipper.showPrevious();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(validate2()) {

                    signUpObject.setName(name.getText().toString());
                    signUpObject.setEmail(email.getText().toString());
                    signUpObject.setPassword(password.getText().toString());
                    signUpObject.setPhone(phone.getText().toString());
                    signUpObject.setShopName(shopName.getText().toString());
                    signUpObject.setShopId(shopId.getText().toString());
                    signUpObject.setShopTelephone(shopTelephone.getText().toString());
                    signUpObject.setShopAddress1(address1.getText().toString());
                    signUpObject.setShopAddress2(address2.getText().toString());
                    signUpObject.setShopLocation(shopLocation);
                    signUpObject.setReferralCode(referralCode.getText().toString());

                    if(Utils.isInternetAvailable(SignUpActivity.this)){

                        progressDialog.show();
                        viewFlipper.setVisibility(View.GONE);

                        ServiceManager.callSignUpService(SignUpActivity.this, signUpObject, new UIListener() {
                            @Override
                            public void onSuccess() {

                                progressDialog.dismiss();

                                UserProfile.setName(name.getText().toString(), SignUpActivity.this);
                                UserProfile.setEmail(email.getText().toString(), SignUpActivity.this);
                                UserProfile.setPhone(phone.getText().toString(), SignUpActivity.this);
                                UserProfile.setPassword(password.getText().toString(),SignUpActivity.this);
                                UserProfile.setShopId(shopId.getText().toString(), SignUpActivity.this);
                                UserProfile.setShopPhone(shopTelephone.getText().toString(), SignUpActivity.this);
                                UserProfile.setAddress1(address1.getText().toString(), SignUpActivity.this);
                                UserProfile.setAddress2(address2.getText().toString(), SignUpActivity.this);
                                UserProfile.setShopName(shopName.getText().toString(), SignUpActivity.this);
                                UserProfile.setLocation(shopLocation,SignUpActivity.this);

                                Intent intent = new Intent(SignUpActivity.this,CategoryActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                                intent = new Intent(SignUpActivity.this, IE_RegistrationIntentService.class);
                                startService(intent);
                                finish();
                            }

                            @Override
                            public void onFailure() {
                                progressDialog.dismiss();
                                viewFlipper.setVisibility(View.VISIBLE);

                                //TODO: set the viewflipper to first registration page

                                Toast.makeText(SignUpActivity.this,"Email is already registered",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onConnectionError() {
                                progressDialog.dismiss();
                                viewFlipper.setVisibility(View.VISIBLE);

                                Toast.makeText(SignUpActivity.this,"Server is currently busy",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled() {
                                progressDialog.dismiss();
                                viewFlipper.setVisibility(View.VISIBLE);
                            }
                        });
                    }else{
                        Toasts.internetUnavailableToast(SignUpActivity.this);
                    }
                }
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    selectedImageUri = data.getData();
                    selectedImagePath = getPath(selectedImageUri);
                    Toast.makeText(this, selectedImagePath, Toast.LENGTH_LONG).show();
                    Log.d("imagepath",selectedImageUri.toString());
                    break;
                }
            case PLACE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(this, data);
                    shopLocation = String.format("%s", place.getName());
                    break;
                }
        }

    }


    public String getPath(Uri uri) {
        if( uri == null ) {
            // TODO perform some logging or show user FeedBackActivity
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    private boolean validate1()
    {
        if(TextUtils.isEmpty(name.getText().toString()))
        {
            Toast.makeText(this,"Name cannot be empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(email.getText().toString()))
        {
            Toast.makeText(this,"Email cannot be empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!(email.getText().toString().contains(".")&&email.getText().toString().contains(".")))
        {
            Toast.makeText(this,"Invalid Email",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(isempty(phone))
        {
            Toast.makeText(this,"Phone cannot be empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(isempty(password)||password.getText().length()<8)
        {
            Toast.makeText(this,"Password cannot be empty or less than 8 letters",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(isempty(confirmPassword))
        {
            Toast.makeText(this,"Password cannot be empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!password.getText().toString().equals(confirmPassword.getText().toString()))
        {
            Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean validate2()
    {
        if(isempty(shopName))
        {
            Toast.makeText(this,"Shop Name cannot be empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(isempty(shopId))
        {
            Toast.makeText(this,"Shop ID cannot be empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(isempty(shopTelephone))
        {
            Toast.makeText(this,"Shop Telephone cannot be empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(isempty(address1))
        {
            Toast.makeText(this,"Address 1 cannot be empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(isempty(address2))
        {
            Toast.makeText(this,"Address 2 cannot be empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(shopLocation.isEmpty())
        {
            Toast.makeText(this,"Select your location",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private boolean isempty(EditText editText)
    {
        return editText.getText().toString().isEmpty();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
