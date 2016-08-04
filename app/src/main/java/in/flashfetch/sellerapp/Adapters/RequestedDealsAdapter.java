package in.flashfetch.sellerapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import in.flashfetch.sellerapp.CommonUtils.Toasts;
import in.flashfetch.sellerapp.CommonUtils.Utils;
import in.flashfetch.sellerapp.Interfaces.UIListener;
import in.flashfetch.sellerapp.Network.ServiceManager;
import in.flashfetch.sellerapp.Objects.Transactions;
import in.flashfetch.sellerapp.QuoteActivity;
import in.flashfetch.sellerapp.R;

public class RequestedDealsAdapter extends RecyclerView.Adapter<RequestedDealsAdapter.ViewHolder> {

    private Context context;
    private Typeface font;
    private ArrayList<Transactions> transactions;
    private static String LOG_TAG = "RequestedDealsAdapter";

    public RequestedDealsAdapter(Context context, ArrayList<Transactions> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName, itemPrice, time, decline, quote;
        public ImageView imageview;

        public ViewHolder(View itemView) {
            super(itemView);

            imageview = (ImageView) itemView.findViewById(R.id.requested_image);
            itemName = (TextView) itemView.findViewById(R.id.requested_item_name);
            itemPrice = (TextView) itemView.findViewById(R.id.requested_price);
            time = (TextView) itemView.findViewById(R.id.requested_time);
            decline = (TextView) itemView.findViewById(R.id.requested_decline);
            quote = (TextView) itemView.findViewById(R.id.requested_quote);
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
        font = Typeface.createFromAsset(context.getAssets(),"fonts/Roboto_Medium.ttf");

        holder.itemName.setTypeface(font);
        holder.itemPrice.setTypeface(font);
        holder.time.setTypeface(font);
        holder.decline.setTypeface(font);
        holder.quote.setTypeface(font);

        holder.itemName.setText(transactions.get(position).productName);
        holder.itemPrice.setText("Rs. " + String.valueOf(transactions.get(position).productPrice));

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
            holder.decline.setVisibility(View.VISIBLE);
            holder.decline.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        Glide.with(context).load(transactions.get(position).imageURL).centerCrop().into(holder.imageview);

        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isInternetAvailable(context)) {

                    ServiceManager.callItemDeclineService(context, transactions.get(position).productId, new UIListener() {
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
                bundle.putString("productId", transactions.get(position).productId);
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

