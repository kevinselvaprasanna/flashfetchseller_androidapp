package in.flashfetch.sellerapp.Fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import in.flashfetch.sellerapp.R;

/**
 * Created by Sharath on 26-06-2016.
 */
public class DialogFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_access, container, false);

        final EditText accesscode_text=(EditText)view.findViewById(R.id.access_text);

        Button accesscode_submit = (Button) view.findViewById(R.id.access_submit);

        accesscode_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accesscode_text.getText().toString().equals("get code")) {
                    fragl.accessAllowed();
                } else {
                    accesscode_text.setError("Incorrect access code");
                }
            }
        });
        return view;
    }

    public interface dialogInterface{
        public void accessAllowed();
    }
    dialogInterface fragl;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragl= (dialogInterface)context;
    }
}
