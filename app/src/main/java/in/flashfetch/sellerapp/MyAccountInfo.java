package in.flashfetch.sellerapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import in.flashfetch.sellerapp.Constants.URLConstants;
import in.flashfetch.sellerapp.Network.PostRequest;
import in.flashfetch.sellerapp.Objects.PostParam;
import in.flashfetch.sellerapp.Objects.UserProfile;

public class MyAccountInfo extends AppCompatActivity {

    TextView name,email,phone,pass;
    String sname,semail,sphone,spass;
    EditText ed_name,ed_email,ed_phone,ed_pass;
    Button logout,submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__acc);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("My Account");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyAccountInfo.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        name = (TextView)findViewById(R.id.name);
        email = (TextView)findViewById(R.id.email);
        phone = (TextView)findViewById(R.id.phone);

        name.setText(UserProfile.getName(MyAccountInfo.this));
        email.setText(UserProfile.getEmail(MyAccountInfo.this));
        phone.setText(UserProfile.getPhone(MyAccountInfo.this));

        logout = (Button)findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Logout
            }
        });
    }

//    private boolean validate()
//    {
//        if(ed_name.getText().length()!=0&&ed_email.getText().length()!=0&&ed_phone.getText().length()!=0&&ed_pass.getText().length()>=8)
//        {
//            return true;
//        }
//        Toast.makeText(this,"Fields cannot be empty",Toast.LENGTH_SHORT).show();
//        return  false;
//    }

//    public class SubmitTask extends AsyncTask<Void, Void, Boolean> {
//        SubmitTask(){
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            ArrayList<PostParam> PostParams = new ArrayList<PostParam>();
//            PostParams.add(new PostParam("email", UserProfile.getEmail(MyAccountInfo.this)));
//            PostParams.add(new PostParam("token",UserProfile.getToken(MyAccountInfo.this)));
//            PostParams.add(new PostParam("name",sname));
//            PostParams.add(new PostParam("mobile",sphone));
//            PostParams.add(new PostParam("shopName",UserProfile.getShopName(MyAccountInfo.this)));
//            PostParams.add(new PostParam("address1",UserProfile.getAddress1(MyAccountInfo.this)));
//            PostParams.add(new PostParam("address2",UserProfile.getAddress2(MyAccountInfo.this)));
//            PostParams.add(new PostParam("pass",spass));
//            PostParams.add(new PostParam("sid",UserProfile.getShopId(MyAccountInfo.this)));
//            PostParams.add(new PostParam("office",UserProfile.getShopPhone(MyAccountInfo.this)));
//            PostParams.add(new PostParam("sel_loc",UserProfile.getLocation(MyAccountInfo.this)));
//            JSONObject ResponseJSON = PostRequest.execute(URLConstants.URLUpdate, PostParams, null);
//            Log.d("RESPONSE", ResponseJSON.toString());
//
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean aBoolean) {
//            super.onPostExecute(aBoolean);
//
//
//        }
//    }

}
