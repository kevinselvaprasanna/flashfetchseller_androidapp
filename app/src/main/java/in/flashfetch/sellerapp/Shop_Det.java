package in.flashfetch.sellerapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.flashfetch.sellerapp.Constants.URLConstants;
import in.flashfetch.sellerapp.Network.PostRequest;
import in.flashfetch.sellerapp.Objects.PostParam;
import in.flashfetch.sellerapp.Objects.UserProfile;

public class Shop_Det extends AppCompatActivity {

    EditText shopname,shopid,shopad1,shopad2,shopphone;
    String tsname,tsid,tshad1,tshad2,tphone;
    Button editcat,submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop__det);

        shopname = (EditText)findViewById(R.id.shop_name);
        shopid = (EditText)findViewById(R.id.shop_id);
        shopad1 = (EditText)findViewById(R.id.add_1);
        shopad2 = (EditText)findViewById(R.id.add_2);
        shopphone = (EditText)findViewById(R.id.telephone);

        submit = (Button)findViewById(R.id.submit);
        editcat = (Button)findViewById(R.id.editcat);

        editcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Shop_Det.this,CategoryActivity.class);
                startActivity(intent);
                //Intent to edit categories
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate())
                {
                    //TODO: Submit
                    tsname = shopname.getText().toString();
                    tsid = shopid.getText().toString();
                    tphone = shopphone.getText().toString();
                    tshad1 = shopad1.getText().toString();
                    tshad2 = shopad2.getText().toString();
                    SubmitTask st = new SubmitTask();
                    st.execute();
                }
            }
        });
    }

    private boolean validate()
    {
        if(shopname.getText().length()==0||shopid.getText().length()==0||shopphone.getText().length()==0||shopad1.getText().length()==0||shopad2.getText().length()==0)
        {
            Toast.makeText(this,"One or more fields is empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public class SubmitTask extends AsyncTask<Void, Void, Boolean> {
        JSONObject ResponseJSON;
        SubmitTask(){

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ArrayList<PostParam> PostParams = new ArrayList<PostParam>();
            PostParams.add(new PostParam("email", UserProfile.getEmail(Shop_Det.this)));
            PostParams.add(new PostParam("token",UserProfile.getToken(Shop_Det.this)));
            PostParams.add(new PostParam("name",UserProfile.getName(Shop_Det.this)));
            PostParams.add(new PostParam("mobile",UserProfile.getPhone(Shop_Det.this)));
            PostParams.add(new PostParam("shopname",tsname));
            PostParams.add(new PostParam("address1",tshad1));
            PostParams.add(new PostParam("address2",tshad2));
            PostParams.add(new PostParam("pass",UserProfile.getPassword(Shop_Det.this)));
            PostParams.add(new PostParam("sid",tsid));
            PostParams.add(new PostParam("office",tphone));
            PostParams.add(new PostParam("sel_loc",UserProfile.getLocation(Shop_Det.this)));
            ResponseJSON = PostRequest.execute(URLConstants.URLUpdate, PostParams, null);
            Log.d("RESPONSE", ResponseJSON.toString());


            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            try {
                if(ResponseJSON.getJSONObject("data").getInt("result")==1){
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
