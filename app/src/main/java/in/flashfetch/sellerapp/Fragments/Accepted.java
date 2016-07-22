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
import in.flashfetch.sellerapp.Helper.DatabaseHelper;
import in.flashfetch.sellerapp.Objects.Notification;
import in.flashfetch.sellerapp.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Accepted.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Accepted#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Accepted extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    //private String mParam1;
    //private String mParam2;

    private Context mContext;
    Typeface font;
    public static TextView noAccessText;
    private boolean isAccessed;
    private RecyclerView recyclerView;
    private ArrayList<Notification> transactions;
    private AcceptedDealsAdapter acceptedDealsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext=getActivity();

        Bundle bundle = getArguments();
        if(bundle != null) {
            isAccessed = bundle.getBoolean("ACCESS");
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

        // Load events from Database
        // events = Event.getAllRelevantEvents(getActivity());
        //nots = NotificationsActivity.getAllNotifications(mContext);

        //initialize events feed adapter
        acceptedDealsAdapter = new AcceptedDealsAdapter(mContext,3, Notification.getANotifications(mContext));

        recyclerView.setAdapter(acceptedDealsAdapter);

//        if(Requested.requestnumber==0||Requested.requestnumber==1) acctext.setVisibility(View.VISIBLE);

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
