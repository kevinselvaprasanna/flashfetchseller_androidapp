package in.flashfetch.sellerapp;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import in.flashfetch.sellerapp.CommonUtils.Toasts;
import in.flashfetch.sellerapp.CommonUtils.Utils;
import in.flashfetch.sellerapp.Constants.Constants;
import in.flashfetch.sellerapp.Interfaces.UIListener;
import in.flashfetch.sellerapp.Network.ServiceManager;
import in.flashfetch.sellerapp.Objects.UserProfile;

public class ReturnsActivity extends BaseActivity {

    private CheckBox[] check;
    private EditText other;
    private Button submit;
    private CheckBox acceptReturns;
    private int[] primeNumbersList;
    private int product = 1;
    private ProgressDialog progressDialog;
    private Context context;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_returns);

        Toolbar toolbar = (Toolbar)findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Returns Policy");

//        toolbar.setBackgroundColor(ContextCompat.getColor(Returns.this,R.color.primary_text));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Returns.this,CategoryActivity.class);
//                startActivity(intent);
                onBackPressed();
            }
        });

        progressDialog = getProgressDialog(context);

        primeNumbersList = Utils.generatePrimeNumbers(11);
        submit = (Button) findViewById(R.id.return_submit_button);
        acceptReturns = (CheckBox)findViewById(R.id.accept_return_checkbox);

        check = new CheckBox[11];
        LinearLayout ll = (LinearLayout) findViewById(R.id.checklayout);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < 11; ++i) {
            check[i] = new CheckBox(this);
            switch (i) {
                case 0:
                    check[i].setText("Delivered package has anything missing from the package");
                    check[i].setChecked(true);
                    check[i].setId(primeNumbersList[i]);
                    break;
                case 1:
                    check[i].setText("Defective/damaged products delivered");
                    check[i].setChecked(true);
                    check[i].setId(primeNumbersList[i]);
                    break;
                case 2:
                    check[i].setText("Products with tampered or missing serial numbers");
                    check[i].setChecked(true);
                    check[i].setId(primeNumbersList[i]);
                    break;
                case 3:
                    check[i].setText("Wrong item was sent by the Seller");
                    check[i].setChecked(true);
                    check[i].setId(primeNumbersList[i]);
                    break;
                case 4:
                    check[i].setText("'Damaged' or 'Defective' condition, or it is 'Not as Described' by the Seller");
                    check[i].setChecked(true);
                    check[i].setId(primeNumbersList[i]);
                    break;
                case 5:
                    check[i].setText("Size/colour are not as described");
                    check[i].setChecked(true);
                    check[i].setId(primeNumbersList[i]);
                    break;
                case 6:
                    check[i].setText("The seller has to refund entire amount to FlashFetch delivery executive, if the product is returned as it is picked up within 3 working days from the point of purchase or pickup\n");
                    check[i].setChecked(true);
                    check[i].setId(primeNumbersList[i]);
                    break;
                case 7:
                    check[i].setText("Product is damaged due to misuse or Incidental damage due to malfunctioning of product");
                    check[i].setId(primeNumbersList[i]);
                    break;
                case 8:
                    check[i].setText("Any consumable item that has been used or installed");
                    check[i].setId(primeNumbersList[i]);
                    break;
                case 9:
                    check[i].setText("Fragile items, hygiene related items.");
                    check[i].setId(primeNumbersList[i]);
                    break;
                case 10:
                    check[i].setText("Any product that is returned without all original packaging and accessories, including the box, manufacturer's packaging if any, and all other items originally included with the product/s delivered");
                    check[i].setId(primeNumbersList[i]);
                    break;
                default:
                    break;
            }
            check[i].setLayoutParams(lp);
            check[i].setTextSize(14);
            check[i].setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.ff_green)));
            if (i < 7)
                check[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        buttonView.setChecked(true);
                    }
                });
            ll.addView(check[i], i + 2);

        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(acceptReturns.isChecked()){

                    for (int i = 0; i < 11; i++) {
                        if (check[i].isChecked()) {
                            product = product * (check[i].getId());
                        }
                    }

                    if(Utils.isInternetAvailable(ReturnsActivity.this)){

                        progressDialog.show();

                        ServiceManager.callReturnPolicyService(ReturnsActivity.this, product, new UIListener() {
                            @Override
                            public void onSuccess() {
                                progressDialog.dismiss();

                                Toast.makeText(Returns.this, "Yours returns policy has been saved", Toast.LENGTH_LONG).show();

                                UserProfile.setReturns(product,Returns.this);

                                Intent intent = new Intent(Returns.this, MainActivity.class);
                                intent.putExtra("FROM_REGISTRATION", Constants.IS_FROM_REGISTRATION_FLOW);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onFailure() {
                                progressDialog.dismiss();
                                Toast.makeText(ReturnsActivity.this, "Server is currently busy", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onConnectionError() {
                                progressDialog.dismiss();
                                Toast.makeText(ReturnsActivity.this, "Server is currently busy", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled() {
                                progressDialog.dismiss();
                            }
                        });
                    }else{
                        Toasts.internetUnavailableToast(ReturnsActivity.this);
                    }
                }else{
                    Toast.makeText(ReturnsActivity.this,"Please accept the terms and conditions",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}


