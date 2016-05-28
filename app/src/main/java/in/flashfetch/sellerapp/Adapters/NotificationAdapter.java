package in.flashfetch.sellerapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import in.flashfetch.sellerapp.Constants.URLConstants;
import in.flashfetch.sellerapp.Helper.DatabaseHelper;
import in.flashfetch.sellerapp.Network.Connectivity;
import in.flashfetch.sellerapp.Network.PostRequest;
import in.flashfetch.sellerapp.Objects.Notification;
import in.flashfetch.sellerapp.Objects.PostParam;
import in.flashfetch.sellerapp.Objects.UserProfile;
import in.flashfetch.sellerapp.QuoteActivity;
import in.flashfetch.sellerapp.R;


import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    Context mContext;
    Typeface font;
    ArrayList<Notification> mItems;
    //TimeHelper th;
    private static String LOG_TAG = "EventDetails";
    int LayoutSelector;



    public NotificationAdapter(Context context, int LayoutSelect, ArrayList<Notification> items) {
        mContext = context;
        mItems = items;
        LayoutSelector = LayoutSelect;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,price,time,decline,quote;
        LinearLayout notsfeed;
        ImageView imageview;
        CardView cv;

        public ViewHolder(View itemView) {
            super(itemView);
            imageview = (ImageView)itemView.findViewById(R.id.image);
            name =(TextView)itemView.findViewById(R.id.name);
            price = (TextView)itemView.findViewById(R.id.price);
            time = (TextView)itemView.findViewById(R.id.time);
            decline = (TextView)itemView.findViewById(R.id.decline);
            quote = (TextView)itemView.findViewById(R.id.quote);
            notsfeed = (LinearLayout)itemView.findViewById(R.id.notsfeed);
            cv = (CardView)itemView.findViewById(R.id.card_view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout;


       /* font = Typeface.createFromAsset(mContext.getAssets(),
                "fonts/Lato-Medium.ttf");*/

                layout = R.layout.item_notifications;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);

                return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NotificationAdapter.ViewHolder holder, final int position) {


        //th = new TimeHelper();
        holder.name.setText(mItems.get(position).name);
        /*holder.name.setTypeface(font);
        holder.price.setTypeface(font);
        holder.time.setTypeface(font);*/
        holder.price.setText("Rs. " + String.valueOf(mItems.get(position).price));
        if(mItems.get(position).time - System.currentTimeMillis() > 0) {
            CountDownTimer countDownTimer = new CountDownTimer( mItems.get(position).time - System.currentTimeMillis(), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    holder.quote.setVisibility(View.VISIBLE);
                    holder.time.setBackgroundColor(Color.parseColor("#0BC6A0"));
                    int hr = (int) (millisUntilFinished / 3600000);
                    int min = (int) ((millisUntilFinished / 60000) - 60 * hr);
                    int sec = (int) ((millisUntilFinished / 1000) - 60 * min - 3600 * hr);
                    holder.time.setText(hr + ":" + min + ":" + sec);
                }

                @Override
                public void onFinish() {
                    holder.time.setText("Time Up");
                }
            };
            countDownTimer.start();
        }else
        {
            holder.time.setText("Time Up");
            holder.quote.setVisibility(View.GONE);
        }
     /*   holder.quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, QuoteActivity.class);
                //TODO: Populate the intent with required data
                mContext.startActivity(intent);
            }
        });*/
        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Connectivity.isNetworkAvailable(mContext)){
                    Toast.makeText(mContext,"NETWORK NOT AVAILABLE",Toast.LENGTH_SHORT);
                }
                DeclineTask dt = new DeclineTask(mItems.get(position).id);
                dt.execute();
                mItems.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mItems.size());
            }
        });
        Glide
                .with(mContext)
                .load(mItems.get(position).imgurl)
                .centerCrop()
                .into(holder.imageview);
        //mItems.get(position).email + " wants " + mItems.get(position).category + " at price Rs." + mItems.get(position).price);
        holder.quote.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, QuoteActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", mItems.get(position).id);
                i.putExtras(bundle);
                mContext.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class DeclineTask extends AsyncTask<Void, Void, Boolean> {
        String id;

        public DeclineTask(String id) {
            this.id = id;
        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            JSONObject ResponseJSON;
            ArrayList<PostParam> iPostParams = new ArrayList<PostParam>();
            PostParam postemail = new PostParam("email", UserProfile.getEmail(mContext));
            PostParam posttoken = new PostParam("token",UserProfile.getToken(mContext));
            iPostParams.add(postemail);
            iPostParams.add(posttoken);
            iPostParams.add(new PostParam("decid",id));
            DatabaseHelper dh = new DatabaseHelper(mContext);
            dh.deleteNot(id);
            //ResponseJSON = PostRequest.execute("http://192.168.43.66/login_buyer.php", iPostParams, null);
            ResponseJSON = PostRequest.execute(URLConstants.URLDecline, iPostParams, null);
            Log.d("RESPONSE", ResponseJSON.toString());
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
        }



    }
