package in.flashfetch.sellerapp;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.flashfetch.sellerapp.Adapters.NotificationAdapter;
import in.flashfetch.sellerapp.Adapters.NotificationAdapter1;
import in.flashfetch.sellerapp.Objects.Notification;
/*
*//*
*//**//**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Provided.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Provided#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Provided extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    //private String mParam1;
    //private String mParam2;

    private Context mContext;
    Typeface font;

    // private OnFragmentInteractionListener mListener;

    public Provided(Context context) {
        mContext = context;
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Requested.
     */
    // TODO: Rename and change types and number of parameters
    /*public static Requested newInstance(String param1, String param2) {
        Requested fragment = new Requested(mContext);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


      /*  font = Typeface.createFromAsset(mContext.getAssets(),
                "fonts/Lato-Medium.ttf");*/

        //TODO: Set typeface for text
        View view = inflater.inflate(R.layout.fragment_provided, container, false);

        LinearLayoutManager layoutManager;
        RecyclerView rvNot;
        //ArrayList<Notification> nots;


        rvNot = (RecyclerView)view.findViewById(R.id.rvNotifications);
        layoutManager = new LinearLayoutManager(mContext);

        //set the recycler view to use the linear layout manager
        rvNot.setLayoutManager(layoutManager);

        // Load events from Database
        // events = Event.getAllRelevantEvents(getActivity());
        //nots = Notification.getAllNotifications(mContext);

        //initialize events feed adapter

        //put conidition to switch layout
        NotificationAdapter1 notsAdapter = new NotificationAdapter1(mContext, 2,Notification.getQNotifications(mContext));

        //Use the events feed adapter
        rvNot.setAdapter(notsAdapter);
        return view;

    }/*

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
