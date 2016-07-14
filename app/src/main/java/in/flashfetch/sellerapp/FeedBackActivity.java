package in.flashfetch.sellerapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.vision.text.Line;

import org.json.JSONObject;

import java.util.ArrayList;

import in.flashfetch.sellerapp.CommonUtils.Toasts;
import in.flashfetch.sellerapp.CommonUtils.Utils;
import in.flashfetch.sellerapp.Constants.URLConstants;
import in.flashfetch.sellerapp.Interfaces.UIListener;
import in.flashfetch.sellerapp.Network.PostRequest;
import in.flashfetch.sellerapp.Network.ServiceManager;
import in.flashfetch.sellerapp.Objects.PostParam;
import in.flashfetch.sellerapp.Objects.UserProfile;

public class FeedBackActivity extends AppCompatActivity {

    EditText feedback_text;
    Button submit;
    String text;

    private ProgressBar progressBar;
    private LinearLayout feedBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("FeedBack");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FeedBackActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        feedBackLayout = (LinearLayout)findViewById(R.id.feedBackLayout);

        feedback_text = (EditText)findViewById(R.id.feedback);

        submit = (Button)findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = feedback_text.getText().toString();

                if(TextUtils.isEmpty(text)){
                    Toast.makeText(FeedBackActivity.this,"Please enter your feedback",Toast.LENGTH_LONG).show();
                }else{
                    if(!Utils.isInternetAvailable(FeedBackActivity.this)){
                        Toasts.internetUnavailableToast(FeedBackActivity.this);
                    }else{
                        progressBar.setVisibility(View.VISIBLE);
                        feedBackLayout.setVisibility(View.GONE);

                        ServiceManager.callFeedBackService(FeedBackActivity.this, text, new UIListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(FeedBackActivity.this, "Thank You For Your Feedback", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(FeedBackActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onFailure() {
                                progressBar.setVisibility(View.GONE);
                                feedBackLayout.setVisibility(View.VISIBLE);
                                Toasts.serverBusyToast(FeedBackActivity.this);
                            }

                            @Override
                            public void onConnectionError() {
                                progressBar.setVisibility(View.GONE);
                                feedBackLayout.setVisibility(View.VISIBLE);
                                Toasts.serverBusyToast(FeedBackActivity.this);
                            }

                            @Override
                            public void onCancelled() {
                                feedBackLayout.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            }
        });
    }

}
