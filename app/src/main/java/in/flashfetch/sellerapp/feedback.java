package in.flashfetch.sellerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class feedback extends AppCompatActivity {

    EditText feedback_text;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        feedback_text = (EditText)findViewById(R.id.feedback);
        submit = (Button)findViewById(R.id.submit);
    }
}
