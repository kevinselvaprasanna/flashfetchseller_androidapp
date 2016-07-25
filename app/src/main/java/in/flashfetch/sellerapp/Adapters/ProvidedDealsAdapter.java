package in.flashfetch.sellerapp.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.flashfetch.sellerapp.CommonUtils.Toasts;
import in.flashfetch.sellerapp.CommonUtils.Utils;
import in.flashfetch.sellerapp.Interfaces.UIListener;
import in.flashfetch.sellerapp.Network.ServiceManager;
import in.flashfetch.sellerapp.Objects.Transactions;
import in.flashfetch.sellerapp.R;

public class ProvidedDealsAdapter extends RecyclerView.Adapter<ProvidedDealsAdapter.ViewHolder> {

    private Context mContext;
    private Typeface font;
    private ArrayList<Transactions> mItems;
    private static String LOG_TAG = "ProvidedDeals";

    private static int ITEM_QUOTED = 1;
    private static int ITEM_BARGAINED = 0;

    public ProvidedDealsAdapter(Context context, ArrayList<Transactions> items) {
        mContext = context;
        mItems = items;
    }

    public static class ViewHolder extends RequestedDealsAdapter.ViewHolder {

        public TextView itemName, timeLeft, priceText1, priceText2, price1, price2, button1, button2;
        public ImageView imageView;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.provided_card_view);

            imageview = (ImageView) itemView.findViewById(R.id.provided_image);
            itemName = (TextView) itemView.findViewById(R.id.item_name);
            timeLeft = (TextView) itemView.findViewById(R.id.time_left);
            price1 = (TextView) itemView.findViewById(R.id.price_1);
            price2 = (TextView) itemView.findViewById(R.id.price_2);
            priceText1 = (TextView) itemView.findViewById(R.id.price_text_1);
            priceText2 = (TextView) itemView.findViewById(R.id.price_text_2);
            button1 = (TextView) itemView.findViewById(R.id.button_1);
            button2 = (TextView) itemView.findViewById(R.id.button_2);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (mItems.get(position).bargained) {
            return ITEM_BARGAINED;
        } else {
            return ITEM_QUOTED;
        }
    }

    public ProvidedDealsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_provided_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProvidedDealsAdapter.ViewHolder holder, final int position) {

        if (getItemViewType(position) == ITEM_BARGAINED) {

//            Glide.with(mContext).load(mItems.get(position).imageURL).centerCrop().into(holder.imageView);
            holder.itemName.setText(mItems.get(position).productName);

            holder.priceText1.setText("Offered");
            holder.priceText1.setTypeface(font);
            holder.priceText2.setText("Bargained");
            holder.priceText2.setTypeface(font);

            holder.price1.setText(mItems.get(position).quotedPrice);
            holder.price2.setText(mItems.get(position).bargainPrice);

            if (mItems.get(position).bargainExpTime - System.currentTimeMillis() > 0) {

                CountDownTimer countDownTimer = new CountDownTimer(mItems.get(position).bargainExpTime - System.currentTimeMillis(), 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                        int hr = (int) (millisUntilFinished / 3600000);
                        int min = (int) ((millisUntilFinished / 60000) - 60 * hr);

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

            holder.button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItems.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mItems.size());
                }
            });

            holder.button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(Utils.isInternetAvailable(mContext)){

                        //TODO: Put progress dialog

                        ServiceManager.callItemAcceptService(mContext, mItems.get(position).productId, new UIListener() {
                            @Override
                            public void onSuccess() {
                                holder.decline.setVisibility(View.GONE);
                                holder.button2.setText("Accepted");
                                holder.button2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                                //TODO: Add the item to accepted deals
                            }

                            @Override
                            public void onFailure() {
                                Toasts.serverBusyToast(mContext);
                            }

                            @Override
                            public void onConnectionError() {
                                Toasts.serverBusyToast(mContext);
                            }

                            @Override
                            public void onCancelled() {
                                Toasts.serviceInterrupted(mContext);
                            }
                        });

                    }else{
                        Toasts.internetUnavailableToast(mContext);
                    }
                }
            });

        } else if(getItemViewType(position) == ITEM_QUOTED){

//            Glide.with(mContext).load(mItems.get(position).imageURL).centerCrop().into(holder.imageView);
            holder.itemName.setText(mItems.get(position).productName);

            holder.priceText1.setText("Price");
            holder.priceText1.setTypeface(font);
            holder.priceText2.setText("Provided");
            holder.priceText2.setTypeface(font);

            holder.price1.setText(mItems.get(position).quotedPrice);
            holder.price2.setText(mItems.get(position).bargainPrice);

            holder.button1.setVisibility(View.VISIBLE);
            holder.button2.setVisibility(View.VISIBLE);

            if (mItems.get(position).time - System.currentTimeMillis() > 0) {

                CountDownTimer countDownTimer = new CountDownTimer(mItems.get(position).time - System.currentTimeMillis(), 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                        int hr = (int) (millisUntilFinished / 3600000);
                        int min = (int) ((millisUntilFinished / 60000) - 60 * hr);

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
       }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
