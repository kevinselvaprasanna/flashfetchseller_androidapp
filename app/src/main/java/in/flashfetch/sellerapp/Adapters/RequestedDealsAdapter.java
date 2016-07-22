package in.flashfetch.sellerapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import org.json.JSONObject;

import in.flashfetch.sellerapp.CommonUtils.Toasts;
import in.flashfetch.sellerapp.CommonUtils.Utils;
import in.flashfetch.sellerapp.Constants.URLConstants;
import in.flashfetch.sellerapp.Helper.DatabaseHelper;
import in.flashfetch.sellerapp.Interfaces.UIListener;
import in.flashfetch.sellerapp.Network.Connectivity;
import in.flashfetch.sellerapp.Network.PostRequest;
import in.flashfetch.sellerapp.Network.ServiceManager;
import in.flashfetch.sellerapp.Objects.Notification;
import in.flashfetch.sellerapp.Objects.PostParam;
import in.flashfetch.sellerapp.Objects.UserProfile;
import in.flashfetch.sellerapp.QuoteActivity;
import in.flashfetch.sellerapp.R;


import java.util.ArrayList;

public class RequestedDealsAdapter extends RecyclerView.Adapter<RequestedDealsAdapter.ViewHolder> {

    private Context context;
    private Typeface font;
    private ArrayList<Notification> transactions;
    private static String LOG_TAG = "EventDetails";

    public RequestedDealsAdapter(Context context, ArrayList<Notification> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName, itemPrice, time, decline, quote;
        public ImageView imageview;

        public ViewHolder(View itemView) {
            super(itemView);

            imageview = (ImageView) itemView.findViewById(R.id.image);
            itemName = (TextView) itemView.findViewById(R.id.name);
            itemPrice = (TextView) itemView.findViewById(R.id.price);
            time = (TextView) itemView.findViewById(R.id.time);
            decline = (TextView) itemView.findViewById(R.id.decline);
            quote = (TextView) itemView.findViewById(R.id.quote);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public RequestedDealsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notifications, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RequestedDealsAdapter.ViewHolder holder, final int position) {
//        font = Typeface.createFromAsset(context.getAssets(),"fonts/Roboto_Medium.ttf");

        holder.itemName.setText(transactions.get(position).name);
        holder.itemPrice.setText("Rs. " + String.valueOf(transactions.get(position).price));

        if (transactions.get(position).time - System.currentTimeMillis() > 0) {
            CountDownTimer countDownTimer = new CountDownTimer(transactions.get(position).time - System.currentTimeMillis(), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                    holder.quote.setVisibility(View.VISIBLE);
                    holder.decline.setVisibility(View.VISIBLE);
                    holder.time.setBackgroundColor(Color.parseColor("#0BC6A0"));

                    int hr = (int) (millisUntilFinished / 3600000);
                    int min = (int) ((millisUntilFinished / 60000) - 60 * hr);

                    holder.time.setText(hr + " h : " + min + " m");
                    //TODO: Set Time Limit
                    if(min <= 1){
                        holder.time.setBackgroundColor(ContextCompat.getColor(context,R.color.ff_red));
                    }
                }

                @Override
                public void onFinish() {
                    holder.time.setText("Time Up");
                    holder.quote.setVisibility(View.GONE);
                    holder.decline.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
            };
            countDownTimer.start();
        } else {
            holder.time.setText("Time Up");
            holder.quote.setVisibility(View.GONE);
            holder.decline.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        Glide.with(context)
                .load(transactions.get(position).imgurl)
                .centerCrop()
                .into(holder.imageview);

        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isInternetAvailable(context)) {

                    ServiceManager.callItemDeclineService(context, transactions.get(position).id, new UIListener() {
                        @Override
                        public void onSuccess() {
                            transactions.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, transactions.size());

                            Toast.makeText(context,"Request has been deleted Successfully",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure() {

                        }

                        @Override
                        public void onConnectionError() {

                        }

                        @Override
                        public void onCancelled() {

                        }
                    });
                } else {
                    Toasts.internetUnavailableToast(context);
                }
            }
        });

        holder.quote.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, QuoteActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("productId", transactions.get(position).id);
                i.putExtras(bundle);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }
}

