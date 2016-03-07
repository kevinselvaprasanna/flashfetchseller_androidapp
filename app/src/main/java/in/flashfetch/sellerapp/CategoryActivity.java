package in.flashfetch.sellerapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import in.flashfetch.sellerapp.Constants.URLConstants;
import in.flashfetch.sellerapp.Network.PostRequest;
import in.flashfetch.sellerapp.Objects.PostParam;
import in.flashfetch.sellerapp.Objects.UserProfile;

import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    CheckBox cmobiles,claptops, ctablets;
    Boolean mobiles, laptops, tablets;
    Button submit;
    Typeface font;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


      /*  font = Typeface.createFromAsset(getAssets(),
                "fonts/Lato-Medium.ttf");
*/
        //TODO: Set typeface for text

        mobiles =false;
        laptops=false;
        tablets=false;
        cmobiles = (CheckBox)findViewById(R.id.mobiles);
        claptops = (CheckBox)findViewById(R.id.laptops);
        ctablets = (CheckBox)findViewById(R.id.tablets);

        submit = (Button)findViewById(R.id.submit2);

        cmobiles.setOnCheckedChangeListener(this);
        claptops.setOnCheckedChangeListener(this);
        ctablets.setOnCheckedChangeListener(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Submit submittask = new Submit();
                submittask.execute();
            }
        });

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch(buttonView.getId()){
            case R.id.mobiles:
                if(isChecked)
                mobiles = true;
                else
                mobiles=false;
                break;
            case R.id.laptops:
                if(isChecked)
                laptops = true;
                else
                laptops=false;
                break;
            case R.id.tablets:
                if(isChecked)
                tablets = true;
                else
                tablets=false;
                break;
        }
    }
    class Submit extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {

            ArrayList<PostParam> PostParams = new ArrayList<PostParam>();
            PostParam postemail = new PostParam("email", UserProfile.getEmail(CategoryActivity.this));
            PostParam postmobiles = new PostParam("mobiles", mobiles.toString());
            PostParam postlaptops = new PostParam("laptops", laptops.toString());
            PostParam posttablets = new PostParam("tablets", tablets.toString());
            PostParams.add(postemail);
            PostParams.add(postmobiles);
            PostParams.add(postlaptops);
            PostParams.add(posttablets);

            JSONObject ResponseJSON = PostRequest.execute(URLConstants.URLCategory, PostParams, null);
            Log.d("RESPONSE",ResponseJSON.toString());

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            Intent i = new Intent(CategoryActivity.this, MainActivity.class);
            startActivity(i);
            super.onPostExecute(aVoid);
        }
    }

}
