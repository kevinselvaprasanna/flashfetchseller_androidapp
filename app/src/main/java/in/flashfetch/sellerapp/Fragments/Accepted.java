package in.flashfetch.sellerapp.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
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
    private boolean isAccessed;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
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

        View view = inflater.inflate(R.layout.fragment_accepted, container, false);

        TextView acctext=(TextView)view.findViewById(R.id.acceptedtext);
        acctext.setVisibility(View.GONE);

        transactions = Transactions.getAcceptedTransactions(mContext);

        //TODO; remove this
        transactions.add(Constants.DUMMY_ACCEPTED_TRANSACTION);
        transactions.add(Constants.DUMMY_ACCEPTED_TRANSACTION);

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh_container);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                transactions.add(Constants.DUMMY_ACCEPTED_TRANSACTION);
                transactions.add(Constants.DUMMY_ACCEPTED_TRANSACTION);
                transactions.add(Constants.DUMMY_ACCEPTED_TRANSACTION);

                acceptedDealsAdapter.notifyDataSetChanged();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(swipeRefreshLayout.isRefreshing()){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                },3000);
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.ff_green,R.color.ff_black);

        TypedValue typedValue = new TypedValue();
        int actionBarHeight = 0;
        if (getActivity().getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());
        }

        swipeRefreshLayout.setProgressViewEndTarget(true, actionBarHeight);

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
