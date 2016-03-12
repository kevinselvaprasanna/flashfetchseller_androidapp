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
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import in.flashfetch.sellerapp.Adapters.CategoryAdapter;
import in.flashfetch.sellerapp.Constants.URLConstants;
import in.flashfetch.sellerapp.Network.PostRequest;
import in.flashfetch.sellerapp.Objects.PostParam;
import in.flashfetch.sellerapp.Objects.UserProfile;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CategoryActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    CheckBox cmobiles,claptops, ctablets;
    Boolean mobiles, laptops, tablets;
    Button submit;
    CategoryAdapter categoryAdapter;
    Typeface font;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_list);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        final List<String> headers = Arrays.asList("Books","Computers and Accessories","Cameras","Game and Accessories","Mobiles and Tablets","Sports, Fitness and Outdoors","Watches");

        List<String> books;
        List<String> comp;
        List<String> cam;
        List<String> game;
        List<String> mob;
        List<String> sports;
        List<String> watches;

        HashMap<String,List<String>> subhead = new HashMap<>();
        subhead.put("Mobiles",mobiles);
        subhead.put("Laptops",laptops);
        subhead.put("Tablets",tablets);


        final HashMap<String,List<Boolean>> subcheck = new HashMap<>();
        subcheck.put("Mobiles",mobcheck);
        subcheck.put("Laptops",lapcheck);
        subcheck.put("Tablets",tabcheck);

        categoryAdapter = new CategoryAdapter(this,headers,subhead,subcheck);

        ExpandableListView expandableListView = (ExpandableListView)findViewById(R.id.lvExp);
        expandableListView.setAdapter(categoryAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                subcheck.get(headers.get(groupPosition)).set(childPosition,!subcheck.get(headers.get(groupPosition)).get(childPosition));
                categoryAdapter.notifyDataSetChanged();
                return true;
            }
        });


      /*  font = Typeface.createFromAsset(getAssets(),
                "fonts/Lato-Medium.ttf");
*/
        //TODO: Set typeface for text
        /*

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
        */

        submit = (Button)findViewById(R.id.Submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                submit.setVisibility(View.GONE);
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
            finish();
            super.onPostExecute(aVoid);
        }
    }

}
