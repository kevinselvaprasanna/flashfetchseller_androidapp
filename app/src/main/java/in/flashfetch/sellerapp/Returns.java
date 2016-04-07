package in.flashfetch.sellerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Returns extends AppCompatActivity {

    CheckBox pol1,pol2,pol3,pol4,pol_other;
    EditText other;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_returns);

        pol1 = (CheckBox)findViewById(R.id.policy1);
        pol2 = (CheckBox)findViewById(R.id.policy2);
        pol3 = (CheckBox)findViewById(R.id.policy3);
        pol4 = (CheckBox)findViewById(R.id.policy4);
        pol_other = (CheckBox)findViewById(R.id.check_other);
        other = (EditText)findViewById(R.id.edit_other);
        submit = (Button)findViewById(R.id.button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate())
                {
                    //TODO:Post validation code
                }
                else
                {
                    Toast.makeText(Returns.this,"Please fill in at least one checkbox",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validate()
    {
        return (pol1.isChecked()||pol2.isChecked()||pol3.isChecked()||pol4.isChecked()||(other()));
    }
    private boolean other()
    {
        return pol_other.isChecked()&&other.getText().length()!=0;
    }
}
