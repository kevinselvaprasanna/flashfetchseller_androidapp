package in.flashfetch.sellerapp.Adapters;

/**
 * Created by SAM10795 on 02-03-2016.
 */


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.CountDownTimer;
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
import in.flashfetch.sellerapp.R;


import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class NotificationAdapter1 extends RecyclerView.Adapter<NotificationAdapter1.ViewHolder> {

    Context mContext;
    Typeface font;
    ArrayList<Notification> mItems;
    Boolean check = false;
    //TimeHelper th;
    private static String LOG_TAG = "EventDetails";
    int LayoutSelector;

    /*0 -> item_notification
     1 -> list_item_provided_2
     2 -> list_item_provided_2
     3-> list_item_accepted
    */


    public NotificationAdapter1(Context context, int LayoutSelect, ArrayList<Notification> items) {
        mContext = context;
        mItems = items;
        LayoutSelector = LayoutSelect;
    }
    public static class ViewHolder extends NotificationAdapter.ViewHolder{

        TextView name,time_left,price,price_quoted,price_quoted1,price_bargained,quoted,price_amount,decline,name1,time_left1,accept;
        ImageView imageview,imageview1;
        CardView cv;

        public ViewHolder(View itemView) {
            super(itemView);
            imageview = (ImageView)itemView.findViewById(R.id.image);
            imageview1 = (ImageView)itemView.findViewById(R.id.image1);
            decline = (TextView)itemView.findViewById(R.id.decline);
            name =(TextView)itemView.findViewById(R.id.name);
            price = (TextView)itemView.findViewById(R.id.price);
            time_left = (TextView)itemView.findViewById(R.id.time_left);
            name1 =(TextView)itemView.findViewById(R.id.name1);
            time_left1 = (TextView)itemView.findViewById(R.id.time_left1);
            price_amount=(TextView)itemView.findViewById(R.id.price_amount);
            price_quoted=(TextView)itemView.findViewById(R.id.price_quoted);
            price_quoted1=(TextView)itemView.findViewById(R.id.price_quoted1);
            price_bargained=(TextView)itemView.findViewById(R.id.price_bargain);
            quoted = (TextView)itemView.findViewById(R.id.quoted);
            notsfeed = (LinearLayout)itemView.findViewById(R.id.notsfeed);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            accept = (TextView)itemView.findViewById(R.id.accept);
        }
    }

    @Override
    public int getItemViewType(int position) {

        //return super.getItemViewType(position);
        if(mItems.get(position).bargained){
            return 1;
        }else {
            return 0;
        }
    }

    public NotificationAdapter1.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout;

      /*  font = Typeface.createFromAsset(mContext.getAssets(),
                "fonts/Lato-Medium.ttf");*/

        if(viewType ==1) {
            layout = R.layout.list_item_provided_2;
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(layout, parent, false);
            return new ViewHolder(view);
        }
        else {
            layout = R.layout.list_item_provided_1;
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(layout, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final NotificationAdapter1.ViewHolder holder, final int position) {

        //TODO: Populate items depending on the holder returned via LayoutSelect
        //TODO: Set typeface for text

        if(getItemViewType(position)==1) {
            holder.name1.setText(mItems.get(position).name);
            holder.name1.setTypeface(font);
            holder.price_bargained.setText(mItems.get(position).bgprice);
            holder.price_bargained.setTypeface(font);
            holder.price_quoted1.setText(mItems.get(position).qprice);
            holder.price_quoted1.setTypeface(font);
            if(mItems.get(position).bgexptime - System.currentTimeMillis() > 0) {
            CountDownTimer countDownTimer = new CountDownTimer(mItems.get(position).bgexptime - System.currentTimeMillis(),1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int hr = (int) (millisUntilFinished / 3600000);
                    int min = (int) ((millisUntilFinished / 60000) - 60 * hr);
                    int sec = (int) ((millisUntilFinished / 1000) - 60 * min - 3600 * hr);
                    holder.time_left1.setText(hr + ":" + min + ":" + sec);
                }

                @Override
                public void onFinish() {
                    holder.time_left1.setText("Time Up");
                }
            };
            countDownTimer.start();
            }else
            {
                holder.time_left1.setText("Time Up");
            }
            //holder.quoted.setTypeface(font);
            //holder.price_amount.setTypeface(font);
            //holder.quoted.setText("Quoted");
            //holder.price.setText("Amount");
            //holder.imageview.setImageResource(R.drawable.ic_media_play);
            Glide
                    .with(mContext)
                    .load(mItems.get(position).imgurl)
                    .centerCrop()
                    .into(holder.imageview1);
            holder.decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItems.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mItems.size());
                }
            });
            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO ACCEPT TASK TO BE DONE
                        AcceptTask at = new AcceptTask(mItems.get(position).id);
                        at.execute();
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(check){
                        holder.decline.setVisibility(View.INVISIBLE);
                        holder.accept.setText("Accepted");
                    }else {
                        Toast.makeText(mContext, "Network not available",Toast.LENGTH_LONG);
                    }
                }
            });
        }
            else {
            holder.name.setText(mItems.get(position).name);
            holder.name.setTypeface(font);
            holder.price_amount.setText(mItems.get(position).price);
            holder.price_amount.setTypeface(font);
            holder.price_quoted.setText(String.valueOf(mItems.get(position).qprice));
            holder.price_quoted.setTypeface(font);
            if(mItems.get(position).time - System.currentTimeMillis() > 0) {
                CountDownTimer countDownTimer = new CountDownTimer( mItems.get(position).time - System.currentTimeMillis(), 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        int hr = (int) (millisUntilFinished / 3600000);
                        int min = (int) ((millisUntilFinished / 60000) - 60 * hr);
                        int sec = (int) ((millisUntilFinished / 1000) - 60 * min - 3600 * hr);
                        holder.time_left.setText(hr + ":" + min + ":" + sec);
                    }

                    @Override
                    public void onFinish() {
                        holder.time_left.setText("Time Up");
                    }
                };
                countDownTimer.start();
            }else
            {
                holder.time_left.setText("Time Up");
            }
            holder.quoted.setTypeface(font);
            holder.price_amount.setTypeface(font);
            holder.quoted.setText("Quoted");
            holder.price.setText("Amount");
            //holder.imageview.setImageResource(R.drawable.ic_media_play);
            Glide
                    .with(mContext)
                    .load(mItems.get(position).imgurl)
                    .centerCrop()
                    .into(holder.imageview);
            holder.decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItems.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mItems.size());
                }
            });
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
            PostParam posttoken = new PostParam("token",UserProfile.getToken(mContext));
            iPostParams.add(postemail);
            iPostParams.add(posttoken);
            iPostParams.add(new PostParam("Cus_id",id));
            DatabaseHelper dh = new DatabaseHelper(mContext);
            //ResponseJSON = PostRequest.execute("http://192.168.43.66/login_buyer.php", iPostParams, null);
            ResponseJSON = PostRequest.execute(URLConstants.URLAccept, iPostParams, null);
            Log.d("RESPONSE", ResponseJSON.toString());
            try {
                if(ResponseJSON.getInt("status")==200){
                    check =true;
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
