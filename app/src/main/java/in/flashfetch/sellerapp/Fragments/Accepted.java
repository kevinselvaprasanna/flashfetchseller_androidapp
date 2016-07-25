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

        recyclerView = (RecyclerView)view.findViewById(R.id.rvNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new ScaleInOutItemAnimator(recyclerView));

        // Load events from Database
        // events = Event.getAllRelevantEvents(getActivity());
        //nots = NotificationsActivity.getAllTransactions(mContext);

        //initialize events feed adapter
        acceptedDealsAdapter = new AcceptedDealsAdapter(mContext, Transactions.getAcceptedTransactions(mContext));

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
    /*

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *//*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
