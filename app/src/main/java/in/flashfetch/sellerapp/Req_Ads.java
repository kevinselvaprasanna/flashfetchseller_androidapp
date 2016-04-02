package in.flashfetch.sellerapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import in.flashfetch.sellerapp.Constants.URLConstants;
import in.flashfetch.sellerapp.Network.PostRequest;
import in.flashfetch.sellerapp.Objects.PostParam;
import in.flashfetch.sellerapp.Objects.UserProfile;

public class Req_Ads extends AppCompatActivity {

    EditText ad;
    TextView date_start,date_end,start,end;
    Button submit;
    Calendar myCalendar;
    int datepick = 0;
    String text,startdate,enddate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_req__ads);
        ad = (EditText)findViewById(R.id.ad);
        start=(TextView)findViewById(R.id.datestart);
        end=(TextView)findViewById(R.id.dateend);
        date_start = (TextView)findViewById(R.id.date_start);
        date_end = (TextView)findViewById(R.id.date_end);
        submit = (Button)findViewById(R.id.submit);

        myCalendar = Calendar.getInstance();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:
                //Create DatePicker Dialog
                //Populate date_start with date
                datepick = 1;
                new DatePickerDialog(Req_Ads.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:
                //Create DatePicker Dialog
                //Populate date_start with date
                datepick = 2;
                new DatePickerDialog(Req_Ads.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:
                //Do something

                text = ad.getText().toString();
                SubmitTask st = new SubmitTask();
                st.execute();
                Toast.makeText(Req_Ads.this,"We will get back to you soon",Toast.LENGTH_SHORT).show();
            }
        });

    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if(datepick==1)
            {
                startdate = DateFormat.getDateInstance().format(myCalendar.getTime());
                date_start.setText(startdate);
            }
            else if(datepick==2)
            {
                enddate = DateFormat.getDateInstance().format(myCalendar.getTime());
                date_end.setText(enddate);
            }
            //
        }

    };

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
            PostParams.add(new PostParam("email", UserProfile.getEmail(Req_Ads.this)));
            PostParams.add(new PostParam("token", UserProfile.getToken(Req_Ads.this)));
            PostParams.add(new PostParam("adv",text));
            PostParams.add(new PostParam("sdate",startdate));
            PostParams.add(new PostParam("edate",enddate));
            JSONObject ResponseJSON = PostRequest.execute(URLConstants.URLAd, PostParams, null);
            Log.d("RESPONSE", ResponseJSON.toString());


            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

}



