package in.flashfetch.sellerapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import in.flashfetch.sellerapp.Constants.URLConstants;
import in.flashfetch.sellerapp.Network.PostRequest;
import in.flashfetch.sellerapp.Objects.PostParam;
import in.flashfetch.sellerapp.Objects.UserProfile;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {
    EditText name, shop_name, email, phone,password, confpassword, shop_id, shop_telephone, address1, address2; //city, postal_code, country, state,
    Button Submit,Next1,Back;
    TextView loc_gps;
    ViewFlipper viewFlipper;
    Typeface font;
    int PLACE_PICKER_REQUEST = 1;
    ProgressBar signupprogress;
    String toastMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //TODO: Set typeface for text

       /* font = Typeface.createFromAsset(getAssets(),
                "fonts/Lato-Medium.ttf");*/

        Next1 = (Button)findViewById(R.id.Next);
        Back = (Button)findViewById(R.id.back);

        signupprogress = (ProgressBar)findViewById(R.id.signup_progress);
        //set signupprogress visible and viewflipper invisible when showing progress

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
            public void onClick(View view) {
                if(validate2()) {
                    signupprogress.setVisibility(View.VISIBLE);
                    viewFlipper.setVisibility(View.GONE);
                    Signup signuptask = new Signup();
                    signuptask.execute();
                   /* Intent intent = new Intent(SignUpActivity.this, CategoryActivity.class);
                    startActivity(intent);*/
                }
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean validate1()
    {

        if(isempty(name))
        {
            Toast.makeText(this,"Name cannot be empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(isempty(email))
        {
            Toast.makeText(this,"Email cannot be empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(isempty(phone))
        {
            Toast.makeText(this,"Phone cannot be empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(isempty(password))
        {
            Toast.makeText(this,"Password cannot be empty",Toast.LENGTH_SHORT).show();
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
        return true;
    }


    private boolean isempty(EditText editText)
    {
        return editText.getText().toString().isEmpty();
    }

    class Signup extends AsyncTask<String, Void, Void> {
        //JSONObject data = new JSONObject();
        JSONObject ResponseJSON;
        String tname = name.getText().toString();
        String temail = email.getText().toString();
        String phone1 = phone.getText().toString();
        String tsname = shop_name.getText().toString();
        String tsid = shop_id.getText().toString();
        String tstelph = shop_telephone.getText().toString();
        String tshadd1 = address1.getText().toString();
        String tshadd2 = address2.getText().toString();
        String tpassword = password.getText().toString();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
                ArrayList<PostParam> instiPostParams = new ArrayList<PostParam>();
                PostParam postUser = new PostParam("name", tname);
               /* if(tname.equals("ksp")){
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                }*/
                PostParam postEmail = new PostParam("email", temail);
                PostParam postPass = new PostParam("password", tpassword);
                instiPostParams.add(postUser);
                instiPostParams.add(postEmail);
                instiPostParams.add(postPass);
                instiPostParams.add(new PostParam("mobile",phone1));
                instiPostParams.add(new PostParam("pass",tpassword));
            instiPostParams.add(new PostParam("shopname",tsname));
            instiPostParams.add(new PostParam("address1",tshadd1));
            instiPostParams.add(new PostParam("address2",tshadd2));
            instiPostParams.add(new PostParam("sid",tsid));
            instiPostParams.add(new PostParam("office",tstelph));
            instiPostParams.add(new PostParam("sel_loc",toastMsg));


                ResponseJSON = PostRequest.execute(URLConstants.URLSignup, instiPostParams, null);
                Log.d("RESPONSE", ResponseJSON.toString());


            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            //tvv.setText(ResponseJSON.toString());
            super.onPostExecute(aVoid);
            try {
                if(ResponseJSON.getJSONObject("data").getInt("result")==1){
                    UserProfile.setName(tname,SignUpActivity.this);
                    UserProfile.setEmail(temail, SignUpActivity.this);
                    UserProfile.setToken(ResponseJSON.getJSONObject("data").getString("token"), SignUpActivity.this);
                    Intent intent = new Intent(SignUpActivity.this,StartActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("EXIT", true);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(SignUpActivity.this,"Server is not working",Toast.LENGTH_LONG);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }

}
