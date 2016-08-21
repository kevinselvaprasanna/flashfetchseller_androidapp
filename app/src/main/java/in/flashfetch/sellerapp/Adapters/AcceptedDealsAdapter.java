package in.flashfetch.sellerapp.Adapters;

/**
 * Created by SAM10795 on 02-03-2016.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
    private AlertDialog alertDialog;
    private Typeface font;
    private ArrayList<Transactions> mItems;
    private static String LOG_TAG = "EventDetails";

    public AcceptedDealsAdapter(Context context, ArrayList<Transactions> items) {
        mContext = context;
        mItems = items;
    }

    public static class ViewHolder extends RequestedDealsAdapter.ViewHolder{

        TextView name,price_final,caller,pickup,buyer_name,header;
        LinearLayout pickup_accept;
        ImageView imageview;

        public ViewHolder(View itemView) {
            super(itemView);

            pickup_accept = (LinearLayout) itemView.findViewById(R.id.pickup_accept);

            imageview = (ImageView)itemView.findViewById(R.id.image);
            name =(TextView)itemView.findViewById(R.id.name);
            header = (TextView)itemView.findViewById(R.id.header);
            buyer_name = (TextView) itemView.findViewById(R.id.buyer_name);
            price_final = (TextView)itemView.findViewById(R.id.price_final);
            caller = (TextView) itemView.findViewById(R.id.caller);
            pickup = (TextView) itemView.findViewById(R.id.pickup);
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

        font = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto_Medium.ttf");

        holder.name.setTypeface(font);
        holder.header.setTypeface(font);
        holder.buyer_name.setTypeface(font);
        holder.price_final.setTypeface(font);
        holder.caller.setTypeface(font);
        holder.pickup.setTypeface(font);

        holder.name.setText(mItems.get(position).productName);
        holder.price_final.setText("Final price : " + mItems.get(position).quotedPrice);
        holder.buyer_name.setText("Buyer Name : "+mItems.get(position).buyerName);

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

        switch(mItems.get(position).deliveryType)
        {
            case 1:         //When no delivery
            {
                holder.caller.setVisibility(View.VISIBLE);
                holder.pickup_accept.setVisibility(View.VISIBLE);
                holder.pickup.setText("Picked Up");
                holder.header.setText("Buyer Pickup");
                holder.header.setBackgroundColor(ContextCompat.getColor(mContext,R.color.ff_black));
            }
            case 2:         //flash fetch delivery
            {
                holder.caller.setVisibility(View.GONE);
                holder.pickup_accept.setVisibility(View.VISIBLE);
                holder.header.setText("Flashfetch Delivery");
                holder.pickup.setText("Picked Up");
                holder.header.setBackgroundColor(ContextCompat.getColor(mContext,R.color.ff_green));

            }
            case 3:         //seller has to deliver
            {
                holder.caller.setVisibility(View.VISIBLE);
                holder.pickup.setText("Delivered");
                holder.pickup_accept.setVisibility(View.VISIBLE);
                holder.header.setText("Deliver to buyer "+mItems.get(position).buyerName);
                holder.header.setBackgroundColor(ContextCompat.getColor(mContext,R.color.ff_red));
            }
            case 4:
            {
                holder.caller.setVisibility(View.VISIBLE);
                holder.pickup.setText("Returned");

                holder.pickup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("Choose Returns").setSingleChoiceItems(R.array.returns, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == 0){
                                    Toast.makeText(mContext,"Exchange Selected",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(mContext,"Returns Selected",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO: Add methods when returns is selected
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog = builder.create();
                        alertDialog.show();
                    }
                });

                holder.pickup_accept.setVisibility(View.VISIBLE);
                holder.header.setText("Return Requested");
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

