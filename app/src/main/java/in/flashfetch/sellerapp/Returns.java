package in.flashfetch.sellerapp;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Returns extends AppCompatActivity {

    //CheckBox pol1,pol2,pol3,pol4,pol_other;
    CheckBox[] check;
    EditText other;
    Button submit;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_returns);
        check=new CheckBox[13];
        LinearLayout ll=(LinearLayout)findViewById(R.id.checklayout);
        ViewGroup.LayoutParams lp=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for(int i=0;i<13;++i){
            check[i]=new CheckBox(this);
            switch(i){
                case 0:
                    check[i].setText("Delivered package has anything missing from the package");
                    check[i].setChecked(true);
                    break;
                case 1:
                    check[i].setText("Defective/damaged products delivered");
                    check[i].setChecked(true);
                    break;
                case 2:
                    check[i].setText("Products with tampered or missing serial numbers");
                    check[i].setChecked(true);
                    break;
                case 3:
                    check[i].setText("Wrong item was sent by the Seller");
                    check[i].setChecked(true);
                    break;
                case 4:
                    check[i].setText("'Damaged' or 'Defective' condition, or it is 'Not as Described' by the Seller");
                    check[i].setChecked(true);
                    break;
                case 5:
                    check[i].setText("Size/colour are not as described");
                    check[i].setChecked(true);
                    break;
                case 6:
                    check[i].setText("The seller has to refund entire amount to FlashFetch delivery executive, if the product is returned as it is picked up within 3 working days from the point of purchase or pickup\n");
                    check[i].setChecked(true);
                    break;
                case 7:
                    check[i].setText("Product is damaged due to misuse or Incidental damage due to malfunctioning of product\n");
                    break;
                case 8:
                    check[i].setText("Any consumable item that has been used or installed\n");
                    break;
                case 9:
                    check[i].setText("Product is damaged due to misuse or Incidental damage due to malfunctioning of product");
                    break;
                case 10:
                    check[i].setText("Any consumable item that has been used or installed");
                    break;
                case 11:
                    check[i].setText("Fragile items, hygiene related items.");
                    break;
                case 12:
                    check[i].setText("Any product that is returned without all original packaging and accessories, including the box, manufacturer's packaging if any, and all other items originally included with the product/s delivered");
                    break;
                default:break;
            }
            check[i].setLayoutParams(lp);
            check[i].setTextSize(17);
            check[i].setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
            if(i<7) check[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    buttonView.setChecked(true);
                }
            });
            ll.addView(check[i],i+2);

        }
        /*
        <CheckBox
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Returns Policy number 1\nDescription"
        android:id="@+id/policy1"
        android:textAppearance="@style/Base.TextAppearance.AppCompat.Medium"
        android:padding="8dp"
        android:gravity="center_vertical"
        android:layout_gravity="center_vertical"
        android:layout_margin="8dp"
        android:checked="true"
        android:buttonTint="@color/colorPrimaryDark"/>

    <CheckBox
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Returns Policy number 2\nDescription"
        android:id="@+id/policy2"
        android:textAppearance="@style/Base.TextAppearance.AppCompat.Medium"
        android:layout_margin="8dp"
        android:padding="8dp"
        android:gravity="center_vertical"
        android:layout_gravity="center_vertical"
        android:checked="true"
        android:buttonTint="@color/colorPrimaryDark"/>

    <CheckBox
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Returns Policy number 3\nDescription"
        android:id="@+id/policy3"
        android:textAppearance="@style/Base.TextAppearance.AppCompat.Medium"
        android:layout_margin="8dp"
        android:padding="8dp"
        android:gravity="center_vertical"
        android:layout_gravity="center_vertical"
        android:buttonTint="@color/colorPrimaryDark" />

    <CheckBox
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Returns Policy number 4\nDescription"
        android:id="@+id/policy4"
        android:textAppearance="@style/Base.TextAppearance.AppCompat.Medium"
        android:layout_margin="8dp"
        android:padding="8dp"
        android:gravity="center_vertical"
        android:layout_gravity="center_vertical"
        android:buttonTint="@color/colorPrimaryDark" />

        pol1 = (CheckBox)findViewById(R.id.policy1);
        pol2 = (CheckBox)findViewById(R.id.policy2);
        pol3 = (CheckBox)findViewById(R.id.policy3);
        pol4 = (CheckBox)findViewById(R.id.policy4);
        pol_other = (CheckBox)findViewById(R.id.check_other);
        other = (EditText)findViewById(R.id.edit_other);
        submit = (Button)findViewById(R.id.button);
        CompoundButton.OnCheckedChangeListener checkedchange=new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch(buttonView.getId()){
                    case R.id.policy1: pol1.setChecked(true);
                        break;
                    case R.id.policy2: pol2.setChecked(true);
                        break;
                    default:break;
                }
            }
        };
        pol1.setOnCheckedChangeListener(checkedchange);
        pol2.setOnCheckedChangeListener(checkedchange);
        pol3.setOnCheckedChangeListener(checkedchange);
        pol4.setOnCheckedChangeListener(checkedchange);*/
        /*submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               *//* if(validate())
                {
                    //TODO:Post validation code
                }
                else
                {
                    Toast.makeText(Returns.this,"Please fill in at least one checkbox",Toast.LENGTH_SHORT).show();
                }*//*
                if(!other()) Toast.makeText(Returns.this,"Please specify the other return condition",Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    /*private boolean validate()
    {
        return (pol1.isChecked()||pol2.isChecked()||pol3.isChecked()||pol4.isChecked()||(other()));
    }
    private boolean other()
    {
        return pol_other.isChecked()&&other.getText().length()!=0;
    }*/
    }
}


