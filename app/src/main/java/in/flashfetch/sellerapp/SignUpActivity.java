package in.flashfetch.sellerapp;

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

import in.flashfetch.sellerapp.CommonUtils.Toasts;
import in.flashfetch.sellerapp.CommonUtils.Utils;
import in.flashfetch.sellerapp.Interfaces.UIListener;
import in.flashfetch.sellerapp.Network.ServiceManager;
import in.flashfetch.sellerapp.Objects.SignUpObject;
import in.flashfetch.sellerapp.Objects.UserProfile;
import in.flashfetch.sellerapp.Services.IE_RegistrationIntentService;

public class SignUpActivity extends AppCompatActivity {
    EditText name, shop_name, email, phone,password, confpassword, shop_id, shop_telephone, address1, address2; //city, postal_code, country, state,
    Button Submit,Next1,Back;
    TextView loc_gps,shopimg;
    ViewFlipper viewFlipper;
    private String selectedImagePath;
    private View firstView, secondView;
    Typeface font;
    Uri selectedImageUri;
    private static final int PLACE_PICKER_REQUEST = 1;
    //private static final int SELECT_PICTURE = 2;
    private static final int SELECT_PHOTO = 2;
    private ProgressBar signUpProgress;
    String shopLocation = "";

    private SignUpObject signUpObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstView = (View)findViewById(R.id.first_view);
        secondView = (View)findViewById(R.id.second_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Registration");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewFlipper.getCurrentView() == firstView){
                    Intent intent = new Intent(SignUpActivity.this,StartActivity.class);
                    startActivity(intent);
                }else{
                    viewFlipper.showPrevious();
                }
            }
        });

        //TODO: Set typeface for text

       /* font = Typeface.createFromAsset(getAssets(),
                "fonts/Lato-Medium.ttf");*/

        Next1 = (Button)findViewById(R.id.Next);
        Back = (Button)findViewById(R.id.back);

        signUpProgress = (ProgressBar)findViewById(R.id.signup_progress);
        //set signUpProgress visible and viewflipper invisible when showing progress

        viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper2);


        name = (EditText)findViewById(R.id.Name);
        email = (EditText)findViewById(R.id.EmailID);
        phone = (EditText)findViewById(R.id.MobileNum);
        password = (EditText)findViewById(R.id.Password);
        confpassword = (EditText)findViewById(R.id.confPass);

        shop_name = (EditText)findViewById(R.id.shop_name);
        shop_id = (EditText)findViewById(R.id.shop_id);
        shop_telephone = (EditText)findViewById(R.id.telephone);
        address1 = (EditText)findViewById(R.id.add_1);
        address2 = (EditText)findViewById(R.id.add_2);
        /*
        city = (EditText)findViewById(R.id.city);
        postal_code = (EditText)findViewById(R.id.postal_code);
        country = (EditText)findViewById(R.id.country);
        state = (EditText)findViewById(R.id.state);
        */
        Submit = (Button)findViewById(R.id.next);

        loc_gps = (TextView)findViewById(R.id.location);
        shopimg = (TextView)findViewById(R.id.shop_img);

        loc_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO : Populate

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                //Context context = getApplicationContext();
                try {
                    startActivityForResult(builder.build(SignUpActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(SignUpActivity.this, "Shop location", Toast.LENGTH_SHORT).show();
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

        Next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate1()) {
                    viewFlipper.showNext();
                }
            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewFlipper.showPrevious();
            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(validate2()) {

                    signUpProgress.setVisibility(View.VISIBLE);
                    viewFlipper.setVisibility(View.GONE);

//                    signUpObject.setName(name.getText().toString());
//                    signUpObject.setEmail(email.getText().toString());
//                    signUpObject.setPassword(password.getText().toString());
//                    signUpObject.setPhone(phone.getText().toString());
//                    signUpObject.setShopName(shop_name.getText().toString());
//                    signUpObject.setShopId(shop_id.getText().toString());
//                    signUpObject.setShopTelephone(shop_telephone.getText().toString());
//                    signUpObject.setShopAddress1(address1.getText().toString());
//                    signUpObject.setShopAddress2(address2.getText().toString());
//                    signUpObject.setShopLocation(shopLocation);

                    if(Utils.isInternetAvailable(SignUpActivity.this)){
                        ServiceManager.callSignUpService(SignUpActivity.this, signUpObject, new UIListener() {
                            @Override
                            public void onSuccess() {

                                signUpProgress.setVisibility(View.GONE);

                                UserProfile.setName(name.getText().toString(), SignUpActivity.this);
                                UserProfile.setEmail(email.getText().toString(), SignUpActivity.this);
                                UserProfile.setPhone(phone.getText().toString(), SignUpActivity.this);
                                UserProfile.setPassword(password.getText().toString(),SignUpActivity.this);
                                UserProfile.setShopId(shop_id.getText().toString(), SignUpActivity.this);
                                UserProfile.setShopPhone(shop_telephone.getText().toString(), SignUpActivity.this);
                                UserProfile.setAddress1(address1.getText().toString(), SignUpActivity.this);
                                UserProfile.setAddress2(address2.getText().toString(), SignUpActivity.this);
                                UserProfile.setShopName(shop_name.getText().toString(), SignUpActivity.this);
                                UserProfile.setLocation(shopLocation,SignUpActivity.this);

                                Intent intent = new Intent(SignUpActivity.this,CategoryActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("EXIT", true);
                                startActivity(intent);

                                intent = new Intent(SignUpActivity.this, IE_RegistrationIntentService.class);
                                startService(intent);
                                finish();
                            }

                            @Override
                            public void onFailure() {
                                signUpProgress.setVisibility(View.GONE);
                                viewFlipper.setVisibility(View.VISIBLE);

                                //TODO: set the viewflipper to first registration page

                                Toast.makeText(SignUpActivity.this,"Email is already registered",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onConnectionError() {
                                signUpProgress.setVisibility(View.GONE);
                                viewFlipper.setVisibility(View.VISIBLE);

                                Toast.makeText(SignUpActivity.this,"Server is currently busy",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled() {
                                signUpProgress.setVisibility(View.GONE);
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
        //super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    selectedImageUri = data.getData();
                    selectedImagePath = getPath(selectedImageUri);
                    Toast.makeText(this, selectedImagePath, Toast.LENGTH_LONG).show();
                    Log.d("imagepath",selectedImageUri.toString());
                    break;
                    /*InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);*/
                }
            case PLACE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(this, data);
                    shopLocation = String.format("%s", place.getName());
                    Toast.makeText(this, shopLocation, Toast.LENGTH_LONG).show();
                    break;
                }
        }

    }


    public String getPath(Uri uri) {
        // just some safety built in
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
        // this is our fallback here
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
        if(isempty(confpassword))
        {
            Toast.makeText(this,"Password cannot be empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!password.getText().toString().equals(confpassword.getText().toString()))
        {
            Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean validate2()
    {
        if(isempty(shop_name))
        {
            Toast.makeText(this,"Shop Name cannot be empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(isempty(shop_id))
        {
            Toast.makeText(this,"Shop ID cannot be empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(isempty(shop_telephone))
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
}
