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

import in.flashfetch.sellerapp.Adapters.AcceptedDealsAdapter;
import in.flashfetch.sellerapp.Constants.Constants;
import in.flashfetch.sellerapp.Objects.Transactions;
import in.flashfetch.sellerapp.R;
import it.gmariotti.recyclerview.itemanimator.ScaleInOutItemAnimator;

public class Accepted extends Fragment {

    private Context mContext;
    private Typeface font;
    private TextView noAccessText;
    private boolean isAccessed;
    private RecyclerView recyclerView;
    private ArrayList<Transactions> transactions;
    private AcceptedDealsAdapter acceptedDealsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext=getActivity();

        Bundle bundle = getArguments();
        if(bundle != null) {
            isAccessed = bundle.getBoolean(Constants.IS_ACCESS_ALLOWED);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto_Medium.ttf");

        View view = inflater.inflate(R.layout.fragment_accepted, container, false);

        TextView acctext=(TextView)view.findViewById(R.id.acceptedtext);
        acctext.setVisibility(View.GONE);

        transactions = Transactions.getAcceptedTransactions(mContext);

        //TODO; remove this

        transactions.add(Constants.DUMMY_ACCEPTED_TRANSACTION);
        transactions.add(Constants.DUMMY_ACCEPTED_TRANSACTION);

        recyclerView = (RecyclerView)view.findViewById(R.id.rvNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new ScaleInOutItemAnimator(recyclerView));

        acceptedDealsAdapter = new AcceptedDealsAdapter(mContext, transactions);

        recyclerView.setAdapter(acceptedDealsAdapter);

        if(isAccessed){
            if (Requested.noOfRequests == 0) {
                acctext.setVisibility(View.VISIBLE);
            }else if (Requested.noOfRequests == 1) {
                acctext.setText("No request yet!! They are on its way!");
                acctext.setVisibility(View.VISIBLE);
            }else{
                recyclerView.setVisibility(View.VISIBLE);
            }
        }else{
            acctext.setVisibility(View.VISIBLE);
            acctext.setText("You will receive product requests after FlashFetch Buyer App is launched. Meanwhile get familiarized with the features of your App. Stay tuned!");
        }
        return view;
    }
}
