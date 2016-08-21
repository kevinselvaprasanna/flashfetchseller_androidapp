package in.flashfetch.sellerapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CompletionService;

import in.flashfetch.sellerapp.CommonUtils.Toasts;
import in.flashfetch.sellerapp.CommonUtils.Utils;
import in.flashfetch.sellerapp.Constants.Constants;
import in.flashfetch.sellerapp.Constants.URLConstants;
import in.flashfetch.sellerapp.Interfaces.UIListener;
import in.flashfetch.sellerapp.Network.PostRequest;
import in.flashfetch.sellerapp.Network.ServiceManager;
import in.flashfetch.sellerapp.Objects.PostParam;
import in.flashfetch.sellerapp.Objects.UserProfile;

public class RequestAds extends AppCompatActivity {

    EditText ad;
    TextView startDateText, endDateText;
    ImageView startDatePicker, endDatePicker;
    Button submit;
    Calendar myCalendar;
    String datePick;
    String text,startDate,endDate;
    private Date currentDate,dateStarted, dateEnded;
    private ProgressBar progressBar;
    private LinearLayout requestLayout;
    private int nextDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_req__ads);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Request Ads");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(RequestAds.this, MainActivity.class);
//                startActivity(intent);
//                finish();
                onBackPressed();
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        requestLayout = (LinearLayout) findViewById(R.id.request_layout);

        ad = (EditText) findViewById(R.id.ad);

        startDatePicker = (ImageView) findViewById(R.id.startDatePicker);
        endDatePicker = (ImageView) findViewById(R.id.endDatePicker);
        startDateText = (TextView) findViewById(R.id.date_start_text);
        endDateText = (TextView) findViewById(R.id.date_end_text);
        submit = (Button) findViewById(R.id.submit);

        myCalendar = Calendar.getInstance();
        currentDate = myCalendar.getTime();
        nextDate = myCalendar.get(Calendar.DAY_OF_MONTH) + 1;

        text = ad.getText().toString();

        startDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePick = Constants.SELECT_START_DATE;

                new DatePickerDialog(RequestAds.this, datePickerListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH) + 1).show();
            }
        });

        endDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePick = Constants.SELECT_END_DATE;

                new DatePickerDialog(RequestAds.this, datePickerListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH) + 1).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ad.getText().toString().trim().length() == 0) {
                    Toast.makeText(RequestAds.this, "Tell us what you are offering", Toast.LENGTH_SHORT).show();
                } else {
                    if (Utils.isSelectedDateGreater(currentDate, dateStarted)
                            && Utils.isSelectedDateGreater(currentDate, dateEnded)
                            && Utils.isSelectedDateGreater(dateStarted, dateEnded)) {

                        if(!Utils.isInternetAvailable(RequestAds.this)){
                            Toasts.internetUnavailableToast(RequestAds.this);
                        }else{

                            progressBar.setVisibility(View.VISIBLE);
                            requestLayout.setVisibility(View.GONE);

                            ServiceManager.callRequestAdsService(RequestAds.this, text, startDate, endDate, new UIListener() {
                                @Override
                                public void onSuccess() {

                                    Toast.makeText(RequestAds.this, "Thank You! Your request has been received we will get back to you soon", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    Intent intent = new Intent(RequestAds.this, MainActivity.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void onFailure() {
                                    progressBar.setVisibility(View.GONE);
                                    requestLayout.setVisibility(View.VISIBLE);
                                    Toasts.serverBusyToast(RequestAds.this);
                                }

                                @Override
                                public void onConnectionError() {
                                    progressBar.setVisibility(View.GONE);
                                    requestLayout.setVisibility(View.VISIBLE);
                                    Toasts.serverBusyToast(RequestAds.this);
                                }

                                @Override
                                public void onCancelled() {
                                    requestLayout.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        }
                    } else {
                        Toast.makeText(RequestAds.this, "Choose your dates correctly", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            if (datePick.equals(Constants.SELECT_START_DATE)) {
                dateStarted = myCalendar.getTime();
                startDate = DateFormat.getDateInstance().format(dateStarted);
                startDateText.setText(startDate);
            } else if (datePick.equals(Constants.SELECT_END_DATE)) {
                dateEnded = myCalendar.getTime();
                endDate = DateFormat.getDateInstance().format(dateEnded);
                endDateText.setText(endDate);
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}



