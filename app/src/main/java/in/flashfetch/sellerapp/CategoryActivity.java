package in.flashfetch.sellerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import in.flashfetch.sellerapp.Adapters.CategoryAdapter;
import in.flashfetch.sellerapp.CommonUtils.Toasts;
import in.flashfetch.sellerapp.CommonUtils.Utils;
import in.flashfetch.sellerapp.Interfaces.UIListener;
import in.flashfetch.sellerapp.Network.ServiceManager;

public class CategoryActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private Boolean mobiles, laptops, tablets;
    private Button submit;
    private CategoryAdapter categoryAdapter;
    private Typeface font;
    private ProgressDialog progressDialog;
    private int product = 1;

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
    int[] cat_head = {23,5,7,2,11,13,3,17,19};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.category_list);

        progressDialog = getProgressDialog(CategoryActivity.this);

        Toolbar toolbar = (Toolbar)findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Categories");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(CategoryActivity.this,LoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
                onBackPressed();
            }
        });


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

        submit = (Button)findViewById(R.id.Submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if(validate(categoryAdapter.getChecks())) {

                   if(Utils.isInternetAvailable(CategoryActivity.this)){

                       progressDialog.show();

                       ServiceManager.callCategoryService(CategoryActivity.this, product, new UIListener() {
                           @Override
                           public void onSuccess() {
                               progressDialog.dismiss();
                               Toast.makeText(CategoryActivity.this,"Categories have been successfully saved",Toast.LENGTH_SHORT).show();

                               Intent intent = new Intent(CategoryActivity.this,Returns.class);
                               startActivity(intent);
                           }

                           @Override
                           public void onFailure() {
                               progressDialog.dismiss();
                               Toasts.serverBusyToast(CategoryActivity.this);
                           }

                           @Override
                           public void onConnectionError() {
                               progressDialog.dismiss();
                               Toasts.serverBusyToast(CategoryActivity.this);
                           }

                           @Override
                           public void onCancelled() {
                                progressDialog.dismiss();
                           }
                       });

                   }else{
                        Toasts.internetUnavailableToast(CategoryActivity.this);
                   }
               }
            }
        });

    }

    private List<Boolean> populate(List<String> string, List<Boolean> bool)
    {
        for(String s:string) {
            bool.add(false);
        }
        return bool;
    }

    private boolean validate(HashMap<String,List<Boolean>> check)
    {
        for(int i=0;i<headers.size();i++) {

            List<Boolean> bools = check.get(headers.get(i));

            product = product*cat_head[i];

            for(int j=0;j<bools.size();j++)
            {
                if(bools.get(j)) {
                    product = product*product(i,j);
                }
            }
        }
        Log.i("Product",product+"");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
