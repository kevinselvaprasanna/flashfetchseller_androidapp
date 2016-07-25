package in.flashfetch.sellerapp.Adapters;

/**
 * Created by SAM10795 on 02-03-2016.
 */

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.flashfetch.sellerapp.Objects.Transactions;
import in.flashfetch.sellerapp.R;

public class AcceptedDealsAdapter extends RecyclerView.Adapter<AcceptedDealsAdapter.ViewHolder> {

    private Context mContext;
    private Typeface font;
    private ArrayList<Transactions> mItems;
    private static String LOG_TAG = "EventDetails";

    public AcceptedDealsAdapter(Context context, ArrayList<Transactions> items) {
        mContext = context;
        mItems = items;
    }


    public static class ViewHolder extends RequestedDealsAdapter.ViewHolder{

        TextView name,price_final,caller,pickup,buyer_name,header;
        LinearLayout notsfeed,pickup_accept;
        ImageView imageview;
        CardView cv;

        public ViewHolder(View itemView) {
            super(itemView);

            imageview = (ImageView)itemView.findViewById(R.id.image);
            name =(TextView)itemView.findViewById(R.id.name);
            header = (TextView)itemView.findViewById(R.id.header);
            buyer_name = (TextView) itemView.findViewById(R.id.buyer_name);
            price_final = (TextView)itemView.findViewById(R.id.price_final);
            notsfeed = (LinearLayout)itemView.findViewById(R.id.notsfeed);
            caller = (TextView) itemView.findViewById(R.id.caller);
            pickup = (TextView) itemView.findViewById(R.id.pickup);
            pickup_accept = (LinearLayout) itemView.findViewById(R.id.pickup_accept);
            cv = (CardView)itemView.findViewById(R.id.card_view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public AcceptedDealsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.list_item_accepted, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AcceptedDealsAdapter.ViewHolder holder, final int position) {

        //TODO: Populate items depending on the holder returned via LayoutSelect
        //TODO: Set typeface for text

        font = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto_Medium.ttf");

        holder.name.setText(mItems.get(position).productName);
        holder.name.setTypeface(font);
        holder.price_final.setTypeface(font);
        holder.price_final.setText("Final price" + mItems.get(position).quotedPrice);

        holder.caller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Call", Toast.LENGTH_SHORT).show();
            }
        });

        holder.pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Picked Up", Toast.LENGTH_SHORT).show();
                holder.pickup_accept.setVisibility(View.INVISIBLE);
            }
        });

        holder.buyer_name.setText("Buyer Name: "+mItems.get(position).buyerName);

        switch(mItems.get(position).deliveryType)
        {
            case 1:         //When no delivery
            {
                holder.caller.setVisibility(View.VISIBLE);
                holder.pickup_accept.setVisibility(View.VISIBLE);
                holder.pickup.setText("Picked Up");
                holder.header.setText("Buyer Pickup:");
                holder.header.setBackgroundColor(ContextCompat.getColor(mContext,R.color.ff_black));
            }
            case 2:         //flash fetch delivery
            {
                holder.caller.setVisibility(View.GONE);
                holder.pickup_accept.setVisibility(View.VISIBLE);
                holder.header.setText("Flashfetch Delivery:");
                holder.pickup.setText("Picked Up");
                holder.header.setBackgroundColor(ContextCompat.getColor(mContext,R.color.ff_green));

            }
            case 3:         //seller has to deliver
            {
                holder.caller.setVisibility(View.VISIBLE);
                holder.pickup.setText("Delivered");
                holder.pickup_accept.setVisibility(View.VISIBLE);
                holder.header.setText("Deliver to buyer: "+mItems.get(position).buyerName);
                holder.header.setBackgroundColor(ContextCompat.getColor(mContext,R.color.ff_red));
            }
            case 4:
            {
                holder.caller.setVisibility(View.VISIBLE);
                holder.pickup.setText("Returned");

                holder.pickup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final Dialog dialog = new Dialog(mContext);
                        dialog.setContentView(R.layout.dialog_returns);

                        final RadioButton ret = (RadioButton)dialog.findViewById(R.id.ret);
                        final RadioButton exc = (RadioButton)dialog.findViewById(R.id.exc);

                        Button button = (Button)dialog.findViewById(R.id.button);

                        ret.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                exc.setChecked(false);
                            }
                        });

                        exc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ret.setChecked(false);
                            }
                        });

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(ret.isChecked()||exc.isChecked()) {
                                    //TODO: Add methods
                                    dialog.cancel();
                                }
                            }
                        });
                        dialog.show();
                    }
                });

                holder.pickup_accept.setVisibility(View.VISIBLE);
                holder.header.setText("Return Requested:");
                holder.header.setBackgroundColor(ContextCompat.getColor(mContext,R.color.ff_red));
            }
        }
        Glide.with(mContext).load(mItems.get(position).imageURL).centerCrop().into(holder.imageview);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}

