package com.kevinselvaprasanna.flashfetch_seller;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.kevinselvaprasanna.flashfetch_seller.Constants.URLConstants;
import com.kevinselvaprasanna.flashfetch_seller.Network.PostRequest;
import com.kevinselvaprasanna.flashfetch_seller.Objects.PostParam;
import com.kevinselvaprasanna.flashfetch_seller.Objects.UserProfile;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {
    EditText firstname, lastname ,email, phone, fax, company, company_id, address1, address2, city, postal_code, country, state, password;
    Button Submit;
    TextView tvv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        firstname = (EditText)findViewById(R.id.firstname);
        lastname = (EditText)findViewById(R.id.lastname);
        email = (EditText)findViewById(R.id.email);
        phone = (EditText)findViewById(R.id.phone);
        fax = (EditText)findViewById(R.id.fax);
        company = (EditText)findViewById(R.id.company);
        company_id = (EditText)findViewById(R.id.company_id);
        address1 = (EditText)findViewById(R.id.address1);
        address2 = (EditText)findViewById(R.id.address2);
        city = (EditText)findViewById(R.id.city);
        postal_code = (EditText)findViewById(R.id.postal_code);
        country = (EditText)findViewById(R.id.country);
        state = (EditText)findViewById(R.id.state);
        password = (EditText)findViewById(R.id.password1);
        Submit = (Button)findViewById(R.id.submit);
        tvv = (TextView)findViewById(R.id.tvv);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Signup signuptask = new Signup();
                signuptask.execute();
            }
        });
    }
    class Signup extends AsyncTask<String, Void, Void> {
        JSONObject data = new JSONObject();
        JSONObject ResponseJSON;
        String tname = firstname.getText().toString();
        String temail = email.getText().toString();
        String tpassword = password.getText().toString();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
                ArrayList<PostParam> instiPostParams = new ArrayList<PostParam>();
                PostParam postUser = new PostParam("name", tname);
                PostParam postEmail = new PostParam("email", temail);
                PostParam postPass = new PostParam("password", tpassword);
                instiPostParams.add(postUser);
                instiPostParams.add(postEmail);
                instiPostParams.add(postPass);

                ResponseJSON = PostRequest.execute(URLConstants.URLSignup, instiPostParams, null);
                Log.d("RESPONSE",ResponseJSON.toString());


            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            //tvv.setText(ResponseJSON.toString());
            try {
                if(ResponseJSON.getInt("status")/100==2){
                    UserProfile.setName(tname,SignUpActivity.this);
                    UserProfile.setEmail(temail,SignUpActivity.this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent i = new Intent(SignUpActivity.this, CategoryActivity.class);
            startActivity(i);
            super.onPostExecute(aVoid);
        }
    }

}
