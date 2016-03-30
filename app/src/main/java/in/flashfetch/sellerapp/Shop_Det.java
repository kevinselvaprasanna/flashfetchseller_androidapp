package in.flashfetch.sellerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Shop_Det extends AppCompatActivity {

    EditText shopname,shopid,shopad1,shopad2,shopphone;
    Button editcat,submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop__det);

        shopname = (EditText)findViewById(R.id.shop_name);
        shopid = (EditText)findViewById(R.id.shop_id);
        shopad1 = (EditText)findViewById(R.id.add_1);
        shopad2 = (EditText)findViewById(R.id.add_2);
        shopphone = (EditText)findViewById(R.id.telephone);

        submit = (Button)findViewById(R.id.submit);
        editcat = (Button)findViewById(R.id.editcat);

        editcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Shop_Det.this,CategoryActivity.class);
                startActivity(intent);
                //Intent to edit categories
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate())
                {
                    //TODO: Submit
                }
            }
        });
    }

    private boolean validate()
    {
        if(shopname.getText().length()==0||shopid.getText().length()==0||shopphone.getText().length()==0||shopad1.getText().length()==0||shopad2.getText().length()==0)
        {
            Toast.makeText(this,"One or more fields is empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
