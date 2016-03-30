package in.flashfetch.sellerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class My_Acc extends AppCompatActivity {

    TextView name,email,phone;
    EditText ed_name,ed_email,ed_phone;
    Button logout,submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__acc);

        name = (TextView)findViewById(R.id.name);
        email = (TextView)findViewById(R.id.email);
        phone = (TextView)findViewById(R.id.phone);

        ed_name = (EditText)findViewById(R.id.name_edit);
        ed_name.setText("Name");//Set default name
        ed_email = (EditText)findViewById(R.id.email_edit);
        ed_email.setText("Email ID");//Set default name
        ed_phone = (EditText)findViewById(R.id.phone_edit);
        ed_phone.setText("1234567890");//Set default name

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
                }
            }
        });
    }

    private boolean validate()
    {
        if(ed_name.getText().length()!=0&&ed_email.getText().length()!=0&&ed_phone.getText().length()!=0)
        {
            return true;
        }
        Toast.makeText(this,"Fields cannot be empty",Toast.LENGTH_SHORT).show();
        return  false;
    }
}
