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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class CategoryActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    Boolean mobiles, laptops, tablets;
    JSONObject ResponseJSON;
    Button submit;
    CategoryAdapter categoryAdapter;
    int category=30;
    Typeface font;
    ProgressBar progressBar;
    int product = 1;
    int[] aud = {29,31,37,41,43};
    int[] book = {47,53,59,61,67,71};
    int[] compi = {73,79,83,89,97,101,103,107};
    int[] cami = {109,113,127,131};
    int[] gami = {137,139,149};
    int[] mobi = {151,157,163,167,173,179,181,191,193,197,199,211,223,227};
    int[] musi = {229,233,239,241,251,257,263,269,271,277,281,283};
    int[] spo = {293,307,311,313,317,331,337,347,349,353};
    int[] watc = {359,367,373};

    List<String> headers = Arrays.asList("Audio and Video","Books","Computers and Accessories","Cameras","Game and Accessories",
            "Mobiles and Tablets","Musical Instruments","Sports, Fitness and Outdoors","Watches");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_list);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);



        List<String> av = Arrays.asList("Headphones","Speakers","Home Entertainment Systems","MP3 & Media Players","Audio & Video Accessories");
        List<String> books = Arrays.asList("All books","Bestseller","Literature & Fiction","Children's & Young Adult","Textbooks","Exam Related books");
        List<String> comp = Arrays.asList("Laptops","Desktops & Monitors", "Pen Drives", "Hard Drives","Memory Cards",
                "Printers & Ink","Networking & Internet Devices","Computer Accessories");
        final List<String> cam = Arrays.asList("Digital SLRs","Point & Shoot Cameras","Lenses","Camera Accessories");
        List<String> game = Arrays.asList("PC Games","Consoles","Accessories");
        List<String> mob = Arrays.asList("All Mobile Phones","Smartphones","Android Mobiles","Windows Mobiles"
                ,"All Mobile Accessories","Cases & Covers","Screen Protectors","Power Banks","On the go Pendrives");
        List<String> music = Arrays.asList("Musical Instrument Accessories","Bass Guitars & Gear","DJ & VJ Equipment"
                ,"Drums & Percussion","General Music-Making Accessories ","Guitars & Gear","Karaoke Equipment","Microphones",
                "PA & Stage","Piano & Keyboard","Recording & Computer","String Instruments","Synthesizer & Sampler","Wind Instruments");
        List<String> sports = Arrays.asList("Exercise & Fitness","Camping & Hiking","Cycling","Cricket","Badminton","Swimming"
                ,"Football","Tennis","Running","Sport Shoes");
        List<String> watches = Arrays.asList("Men's watches","Women's watches","Kids Watches");

        HashMap<String,List<String>> subhead = new HashMap<>();
        subhead.put(headers.get(0),av);
        subhead.put(headers.get(1),books);
        subhead.put(headers.get(2),comp);
        subhead.put(headers.get(3),cam);
        subhead.put(headers.get(4),game);
        subhead.put(headers.get(5),mob);
        subhead.put(headers.get(6),music);
        subhead.put(headers.get(7),sports);
        subhead.put(headers.get(8),watches);


        final HashMap<String,List<Boolean>> subcheck = new HashMap<>();
        List<Boolean> avcheck = new ArrayList<>();
        List<Boolean> bookcheck = new ArrayList<>();
        List<Boolean> compcheck = new ArrayList<>();
        List<Boolean> camcheck = new ArrayList<>();
        List<Boolean> gamecheck = new ArrayList<>();
        List<Boolean> mobcheck = new ArrayList<>();
        List<Boolean> sportcheck = new ArrayList<>();
        List<Boolean> watcheck = new ArrayList<>();
        List<Boolean> musicheck = new ArrayList<>();

        avcheck = populate(av,avcheck);
        bookcheck = populate(books,bookcheck);
        compcheck = populate(comp,compcheck);
        camcheck = populate(cam,camcheck);
        gamecheck = populate(game,gamecheck);
        mobcheck = populate(mob,mobcheck);
        musicheck = populate(music,musicheck);
        sportcheck = populate(sports,sportcheck);
        watcheck = populate(watches,watcheck);

        subcheck.put(headers.get(0),avcheck);
        subcheck.put(headers.get(1),bookcheck);
        subcheck.put(headers.get(2),compcheck);
        subcheck.put(headers.get(3),camcheck);
        subcheck.put(headers.get(4),gamecheck);
        subcheck.put(headers.get(5),mobcheck);
        subcheck.put(headers.get(6),musicheck);
        subcheck.put(headers.get(7),sportcheck);
        subcheck.put(headers.get(8),watcheck);


        categoryAdapter = new CategoryAdapter(this,headers,subhead,subcheck);

        ExpandableListView expandableListView = (ExpandableListView)findViewById(R.id.lvExp);
        expandableListView.setAdapter(categoryAdapter);
        /*expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.i("Click","Child");
                Log.i("Product",product+"");

                switch (groupPosition)
                {
                    case 0:
                        product = product*aud[childPosition];
                    case 1:
                        product = product*book[childPosition];
                    case 2:
                        product = product*compi[childPosition];
                    case 3:
                        product = product*cami[childPosition];
                    case 4:
                        product = product*gami[childPosition];
                    case 5:
                        product = product*mobi[childPosition];
                    case 6:
                        product = product*musi[childPosition];
                    case 7:
                        product = product*spo[childPosition];
                    case 8:
                        product = product*watc[childPosition];
                }
                //use product
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
               if(validate(categoryAdapter.getChecks())) {
                    progressBar.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.GONE);
                    Submit submittask = new Submit();
                    submittask.execute();
                   Log.i("Product",product+"");
               }
            }
        });

    }

    private List<Boolean> populate(List<String> string, List<Boolean> bool)
    {
        for(String s:string)
        {
            bool.add(false);
        }
        return bool;
    }

    private boolean validate(HashMap<String,List<Boolean>> check)
    {
        int product =1;
        for(int i=0;i<headers.size();i++) {
            List<Boolean> bools = check.get(headers.get(i));
            for(int j=0;j<bools.size();j++)
            {
                if(bools.get(j))
                {
                    product = product*product(i,j);
                }
            }
        }
        this.product = product;
        return product>1;
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

            ArrayList<PostParam> instiPostParams = new ArrayList<PostParam>();
            instiPostParams.add(new PostParam("token",UserProfile.getToken(CategoryActivity.this)));
            instiPostParams.add(new PostParam("email",UserProfile.getEmail(CategoryActivity.this)));
            instiPostParams.add(new PostParam("category",String.valueOf(product)));


            ResponseJSON = PostRequest.execute(URLConstants.URLCategory, instiPostParams, null);
            Log.d("RESPONSE",ResponseJSON.toString());

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                if(ResponseJSON.getJSONObject("data").getInt("result")==1) {
                    super.onPostExecute(aVoid);
                    UserProfile.setCategory(category,CategoryActivity.this);
                    Intent i = new Intent(CategoryActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public int product(int groupPosition,int childPosition)
    {

        switch (groupPosition)
        {
            case 0:
                return aud[childPosition];
            case 1:
                return book[childPosition];
            case 2:
                return compi[childPosition];
            case 3:
                return cami[childPosition];
            case 4:
                return gami[childPosition];
            case 5:
                return mobi[childPosition];
            case 6:
                return musi[childPosition];
            case 7:
                return spo[childPosition];
            case 8:
                return watc[childPosition];
            default:
                return 1;
        }
    }

}
