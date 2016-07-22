package in.flashfetch.sellerapp.Adapters;

/**
 * Created by SAM10795 on 02-03-2016.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import in.flashfetch.sellerapp.Constants.URLConstants;
import in.flashfetch.sellerapp.Helper.DatabaseHelper;
import in.flashfetch.sellerapp.Network.PostRequest;
import in.flashfetch.sellerapp.Objects.Notification;
import in.flashfetch.sellerapp.Objects.PostParam;
import in.flashfetch.sellerapp.Objects.UserProfile;
import in.flashfetch.sellerapp.R;


import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class ProvidedDealsAdapter extends RecyclerView.Adapter<ProvidedDealsAdapter.ViewHolder> {

    private Context mContext;
    private Typeface font;
    private ArrayList<Notification> mItems;
    private boolean check = false;
    private static String LOG_TAG = "EventDetails";
    int LayoutSelector;

    private static int ITEM_ACCEPTED = 1;
    private static int ITEM_BARGAINED = 0;

    /*0 -> item_notification
     1 -> list_item_provided_2
     2 -> list_item_provided_2
     3-> list_item_accepted
    */


    public ProvidedDealsAdapter(Context context, int LayoutSelect, ArrayList<Notification> items) {
        mContext = context;
        mItems = items;
        LayoutSelector = LayoutSelect;
    }

    public static class ViewHolder extends RequestedDealsAdapter.ViewHolder {

        TextView itemName, timeLeft, priceText1, priceText2, price1, price2, button1, button2;
        ImageView imageView;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view);

            imageview = (ImageView) itemView.findViewById(R.id.image);
            itemName = (TextView) itemView.findViewById(R.id.name);
            timeLeft = (TextView) itemView.findViewById(R.id.time_left);
            price1 = (TextView) itemView.findViewById(R.id.price_1);
            price2 = (TextView) itemView.findViewById(R.id.price_2);
            priceText1 = (TextView) itemView.findViewById(R.id.price_text_1);
            priceText2 = (TextView) itemView.findViewById(R.id.price_text_2);
            button1 = (Button) itemView.findViewById(R.id.button_1);
            button2 = (Button) itemView.findViewById(R.id.button_2);
//            notsfeed = (LinearLayout)itemView.findViewById(R.id.notsfeed);
        }
    }

    @Override
    public int getItemViewType(int position) {

        //return super.getItemViewType(position);
        if (mItems.get(position).bargained) {
            return ITEM_BARGAINED;
        } else {
            return ITEM_ACCEPTED;
        }
    }

    public ProvidedDealsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_provided_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProvidedDealsAdapter.ViewHolder holder, final int position) {

        Glide.with(mContext).load(mItems.get(position).imgurl).centerCrop().into(holder.imageView);
//        holder.name.setText(mItems.get(position).name);

        if (getItemViewType(position) == ITEM_BARGAINED) {

            holder.priceText1.setText("Offered");
            holder.priceText1.setTypeface(font);
            holder.priceText2.setText("Bargained");
            holder.priceText2.setTypeface(font);

            if (mItems.get(position).bgexptime - System.currentTimeMillis() > 0) {

                CountDownTimer countDownTimer = new CountDownTimer(mItems.get(position).bgexptime - System.currentTimeMillis(), 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        int hr = (int) (millisUntilFinished / 3600000);
                        int min = (int) ((millisUntilFinished / 60000) - 60 * hr);
                        int sec = (int) ((millisUntilFinished / 1000) - 60 * min - 3600 * hr);
                        holder.timeLeft.setText(hr + " h : " + min + " m");
                    }

                    @Override
                    public void onFinish() {
                        holder.timeLeft.setText("Time Up");
                    }
                };
                countDownTimer.start();
            } else {
                holder.timeLeft.setText("Time Up");
            }

            holder.decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItems.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mItems.size());
                }
            });
//            holder.accept.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    AcceptTask at = new AcceptTask(mItems.get(position).id);
//                    at.execute();
//                    try {
//                        sleep(2000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    if (check) {
//                        holder.decline.setVisibility(View.INVISIBLE);
//                        holder.accept.setText("Accepted");
//                    } else {
//                        Toast.makeText(mContext, "Network not available", Toast.LENGTH_LONG);
//                    }
//                }
//            });
        } else if(getItemViewType(position) == ITEM_ACCEPTED){

//            holder.price_amount.setText(mItems.get(position).price);
//            holder.price_amount.setTypeface(font);
//            holder.price_quoted.setText(String.valueOf(mItems.get(position).qprice));
//            holder.price_quoted.setTypeface(font);
//            if (mItems.get(position).time - System.currentTimeMillis() > 0) {
//                CountDownTimer countDownTimer = new CountDownTimer(mItems.get(position).time - System.currentTimeMillis(), 1000) {
//                    @Override
//                    public void onTick(long millisUntilFinished) {
//                        int hr = (int) (millisUntilFinished / 3600000);
//                        int min = (int) ((millisUntilFinished / 60000) - 60 * hr);
//                        int sec = (int) ((millisUntilFinished / 1000) - 60 * min - 3600 * hr);
//                        holder.time_left.setText(hr + ":" + min + ":" + sec);
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        holder.time_left.setText("Time Up");
//                    }
//                };
//                countDownTimer.start();
//            } else {
//                holder.time_left.setText("Time Up");
//            }
//            holder.quoted.setTypeface(font);
//            holder.price_amount.setTypeface(font);
//            holder.quoted.setText("Quoted");
//            holder.price.setText("Amount");
//            //holder.imageview.setImageResource(R.drawable.ic_media_play);
//            Glide
//                    .with(mContext)
//                    .load(mItems.get(position).imgurl)
//                    .centerCrop()
//                    .into(holder.imageview);
//            holder.decline.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mItems.remove(position);
//                    notifyItemRemoved(position);
//                    notifyItemRangeChanged(position, mItems.size());
//                }
//            });
       }


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class AcceptTask extends AsyncTask<Void, Void, Boolean> {
        String id;

        public AcceptTask(String id) {
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
            PostParam posttoken = new PostParam("token", UserProfile.getToken(mContext));
            iPostParams.add(postemail);
            iPostParams.add(posttoken);
            iPostParams.add(new PostParam("Cus_id", id));
            DatabaseHelper dh = new DatabaseHelper(mContext);
            //ResponseJSON = PostRequest.execute("http://192.168.43.66/login_buyer.php", iPostParams, null);
            ResponseJSON = PostRequest.execute(URLConstants.URLAccept, iPostParams, null);
            Log.d("RESPONSE", ResponseJSON.toString());
            try {
                if (ResponseJSON.getInt("status") == 200) {
                    check = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

        }
    }

}
