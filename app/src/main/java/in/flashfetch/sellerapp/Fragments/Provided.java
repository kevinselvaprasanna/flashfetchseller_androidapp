package in.flashfetch.sellerapp.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.flashfetch.sellerapp.Adapters.ProvidedDealsAdapter;
import in.flashfetch.sellerapp.Constants.Constants;
import in.flashfetch.sellerapp.Objects.Transactions;
import in.flashfetch.sellerapp.R;
import it.gmariotti.recyclerview.itemanimator.ScaleInOutItemAnimator;

public class Provided extends Fragment {

    private Context mContext = getActivity();
    private Typeface font;
    private RecyclerView recyclerView;
    private boolean isAccessed;
    private ProvidedDealsAdapter providedDealsAdapter;
    private ArrayList<Transactions> transactions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle != null) {
            isAccessed = bundle.getBoolean(Constants.IS_ACCESS_ALLOWED);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto_Medium.ttf");

        mContext= getActivity();

        transactions = Transactions.getQuotedTransactions(mContext);

        //TODO: remove this
        transactions.add(Constants.DUMMY_PROVIDED_TRANSACTION);
        transactions.add(Constants.DUMMY_PROVIDED_TRANSACTION);

        View view = inflater.inflate(R.layout.fragment_provided, container, false);

        TextView notifyText=(TextView)view.findViewById(R.id.providedtext);
        notifyText.setVisibility(View.GONE);

        recyclerView = (RecyclerView)view.findViewById(R.id.rvNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new ScaleInOutItemAnimator(recyclerView));

        providedDealsAdapter = new ProvidedDealsAdapter(mContext, transactions);

        recyclerView.setAdapter(providedDealsAdapter);

        if(isAccessed){
            if (Requested.noOfRequests == 0) {
                notifyText.setVisibility(View.VISIBLE);
            }else if (Requested.noOfRequests == 1) {
                notifyText.setText("No request yet!! They are on its way!");
                notifyText.setVisibility(View.VISIBLE);
            }else{
                recyclerView.setVisibility(View.VISIBLE);
            }
        }else{
            notifyText.setVisibility(View.VISIBLE);
            notifyText.setText("You will receive product requests after FlashFetch Buyer App is launched. Meanwhile get familiarized with the features of your App. Stay tuned!");
        }

        return view;
    }
}
