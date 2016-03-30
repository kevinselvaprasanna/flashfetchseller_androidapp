package in.flashfetch.sellerapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class Req_Ads extends AppCompatActivity {

    EditText ad;
    TextView date_start,date_end,start,end;
    Button submit;
    Calendar myCalendar;
    int datepick = 0;

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
                date_start.setText(DateFormat.getDateInstance().format(myCalendar.getTime()));
            }
            else if(datepick==2)
            {
                date_end.setText(DateFormat.getDateInstance().format(myCalendar.getTime()));
            }
            //
        }

    };

}
