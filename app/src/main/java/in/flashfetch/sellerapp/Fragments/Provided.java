package in.flashfetch.sellerapp.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.flashfetch.sellerapp.Adapters.ProvidedDealsAdapter;
import in.flashfetch.sellerapp.Helper.DatabaseHelper;
import in.flashfetch.sellerapp.Objects.Notification;
import in.flashfetch.sellerapp.R;

public class Provided extends Fragment {

    private Context mContext = getActivity();
    private Typeface font;
    private RecyclerView recyclerView;
    private boolean isAccessed;
    private ProvidedDealsAdapter providedDealsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle != null) {
            isAccessed = bundle.getBoolean("ACCESS");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto_Medium.ttf");

        View view = inflater.inflate(R.layout.fragment_provided, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.rvNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));

        TextView provtext=(TextView)view.findViewById(R.id.providedtext);
        provtext.setVisibility(View.GONE);
        mContext= getActivity();

        // Load events from Database
        // events = Event.getAllRelevantEvents(getActivity());
        //nots = NotificationsActivity.getAllNotifications(mContext);

        //initialize events feed adapter

        //put conidition to switch layout
        providedDealsAdapter = new ProvidedDealsAdapter(mContext, 2, Notification.getQNotifications(getActivity()));

        recyclerView.setAdapter(providedDealsAdapter);

//        if(Requested.requestnumber==0) provtext.setVisibility(View.VISIBLE);
//        if(Requested.requestnumber==1) {
//            provtext.setText("Respond to the request in time to make a deal.");
//            provtext.setVisibility(View.VISIBLE);
//        }

        if(isAccessed){
            if (Requested.noOfRequests == 0) {
                provtext.setVisibility(View.VISIBLE);
            }else if (Requested.noOfRequests == 1) {
                provtext.setText("No request yet!! They are on its way!");
                provtext.setVisibility(View.VISIBLE);
            }else{
                recyclerView.setVisibility(View.VISIBLE);
            }
        }else{
            provtext.setVisibility(View.VISIBLE);
            provtext.setText("You will receive product requests after FlashFetch Buyer App is launched. Meanwhile get familiarized with the features of your App. Stay tuned!");
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
