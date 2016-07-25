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

import in.flashfetch.sellerapp.Adapters.RequestedDealsAdapter;
import in.flashfetch.sellerapp.Constants.Constants;
import in.flashfetch.sellerapp.Objects.Transactions;
import in.flashfetch.sellerapp.R;
import it.gmariotti.recyclerview.itemanimator.ScaleInOutItemAnimator;

public class Requested extends Fragment {

    private Context context = getActivity();
    private Typeface font;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ArrayList<Transactions> transactions;
    private RequestedDealsAdapter requestedDealsAdapter;
    private boolean isAccessed;

    public static int noOfRequests = -1;

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

        View view = inflater.inflate(R.layout.fragment_requested, container, false);

        context = getActivity();
        TextView reqText = (TextView) view.findViewById(R.id.requestedtext);
        reqText.setVisibility(View.GONE);

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh_container);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                transactions.clear();

                transactions.add(Constants.DUMMY_REQUESTED_TRANSACTION);
                transactions.add(Constants.DUMMY_REQUESTED_TRANSACTION);
                transactions.add(Constants.DUMMY_REQUESTED_TRANSACTION);

                requestedDealsAdapter.notifyDataSetChanged();

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

        recyclerView = (RecyclerView) view.findViewById(R.id.requested_transactions);
        recyclerView.setItemAnimator(new ScaleInOutItemAnimator(recyclerView));
        layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(layoutManager);

        transactions = Transactions.getAllTransactions(getActivity());

        transactions.add(Constants.DUMMY_REQUESTED_TRANSACTION);
        transactions.add(Constants.DUMMY_REQUESTED_TRANSACTION);

        requestedDealsAdapter = new RequestedDealsAdapter(context, transactions);

        recyclerView.setAdapter(requestedDealsAdapter);

        if(isAccessed){
            if (requestedDealsAdapter.getItemCount() == 0) {
                reqText.setVisibility(View.VISIBLE);
                noOfRequests = 0;
            }else if (requestedDealsAdapter.getItemCount() == 1) {
                reqText.setText("No request yet!! They are on its way!");
                reqText.setVisibility(View.VISIBLE);
                noOfRequests = 1;
            }else{
                recyclerView.setVisibility(View.VISIBLE);
            }
        }else{
            reqText.setVisibility(View.VISIBLE);
            reqText.setText("You will receive product requests after FlashFetch Buyer App is launched. Meanwhile get familiarized with the features of your App. Stay tuned!");
        }
        return view;
    }
}
