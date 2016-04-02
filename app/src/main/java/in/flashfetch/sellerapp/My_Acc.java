package in.flashfetch.sellerapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class My_Acc extends AppCompatActivity {

    TextView name,email,phone,pass;
    String sname,semail,sphone,spass;
    EditText ed_name,ed_email,ed_phone,ed_pass;
    Button logout,submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__acc);

        name = (TextView)findViewById(R.id.name);
        email = (TextView)findViewById(R.id.email);
        phone = (TextView)findViewById(R.id.phone);
        pass = (TextView)findViewById(R.id.pass);

        ed_name = (EditText)findViewById(R.id.name_edit);
        ed_name.setText(UserProfile.getName(My_Acc.this));//Set default name
        ed_email = (EditText)findViewById(R.id.email_edit);
        ed_email.setText(UserProfile.getEmail(My_Acc.this));//Set default name
        ed_phone = (EditText)findViewById(R.id.phone_edit);
        ed_phone.setText(UserProfile.getPhone(My_Acc.this));//Set default name
        ed_pass = (EditText)findViewById(R.id.pass_edit);
        ed_pass.setText(UserProfile.getPassword(My_Acc.this));

        logout = (Button)findViewById(R.id.logout);
        submit = (Button)findViewById(R.id.submit);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Logout
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    //TODO: Submit
                    sname = ed_name.getText().toString();
                    semail = ed_email.getText().toString();
                    sphone = ed_phone.getText().toString();
                    spass = ed_pass.getText().toString();
                    SubmitTask st = new SubmitTask();
                    st.execute();
                }
            }
        });
    }

    private boolean validate()
    {
        if(ed_name.getText().length()!=0&&ed_email.getText().length()!=0&&ed_phone.getText().length()!=0&&ed_pass.getText().length()>=8)
        {
            return true;
        }
        Toast.makeText(this,"Fields cannot be empty",Toast.LENGTH_SHORT).show();
        return  false;
    }

    public class SubmitTask extends AsyncTask<Void, Void, Boolean> {
        SubmitTask(){

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ArrayList<PostParam> PostParams = new ArrayList<PostParam>();
            PostParams.add(new PostParam("email", UserProfile.getEmail(My_Acc.this)));
            PostParams.add(new PostParam("token",UserProfile.getToken(My_Acc.this)));
            PostParams.add(new PostParam("name",sname));
            PostParams.add(new PostParam("mobile",sphone));
            PostParams.add(new PostParam("shopname",UserProfile.getShopName(My_Acc.this)));
            PostParams.add(new PostParam("address1",UserProfile.getAddress1(My_Acc.this)));
            PostParams.add(new PostParam("address2",UserProfile.getAddress2(My_Acc.this)));
            PostParams.add(new PostParam("pass",spass));
            PostParams.add(new PostParam("sid",UserProfile.getShopId(My_Acc.this)));
            PostParams.add(new PostParam("office",UserProfile.getShopPhone(My_Acc.this)));
            PostParams.add(new PostParam("sel_loc",UserProfile.getLocation(My_Acc.this)));
            JSONObject ResponseJSON = PostRequest.execute(URLConstants.URLUpdate, PostParams, null);
            Log.d("RESPONSE", ResponseJSON.toString());


            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

}
