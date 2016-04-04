package in.flashfetch.sellerapp.Adapters;

/**
 * Created by SAM10795 on 02-03-2016.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import in.flashfetch.sellerapp.Objects.Notification;
import in.flashfetch.sellerapp.R;


import java.util.ArrayList;

public class NotificationAdapter3 extends RecyclerView.Adapter<NotificationAdapter3.ViewHolder> {

    Context mContext;
    Typeface font;
    int condition = 0;
    ArrayList<Notification> mItems;
    //TimeHelper th;
    private static String LOG_TAG = "EventDetails";
    int LayoutSelector;

    /*0 -> item_notification
     1 -> list_item_provided_2
     2 -> list_item_provided_2
     3-> list_item_accepted
    */


    public NotificationAdapter3(Context context, int LayoutSelect, ArrayList<Notification> items) {
        mContext = context;
        mItems = items;
        LayoutSelector = LayoutSelect;
    }


    public static class ViewHolder extends NotificationAdapter.ViewHolder{
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

    public NotificationAdapter3.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout;


       /* font = Typeface.createFromAsset(mContext.getAssets(),
                "fonts/Lato-Medium.ttf");*/
                layout = R.layout.list_item_accepted;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
                return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationAdapter3.ViewHolder holder, final int position) {

        //TODO: Populate items depending on the holder returned via LayoutSelect
        //TODO: Set typeface for text
        //th = new TimeHelper();

        final NotificationAdapter3.ViewHolder holder1 = holder;

        holder.name.setText(mItems.get(position).name);
        holder.name.setTypeface(font);
        holder.price_final.setTypeface(font);
        holder.price_final.setText("Final price" + mItems.get(position).qprice);
        holder.caller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Call", Toast.LENGTH_SHORT).show();
            }
        });
        holder.pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext,"Picked Up",Toast.LENGTH_SHORT).show();
                holder1.pickup_accept.setVisibility(View.INVISIBLE);
            }
        });
        holder.buyer_name.setText("Buyer Name: "+mItems.get(position).buyer_name);

        //NOTE:
        /*
        To switch between the three views, you can change the visibility of pickup_accept layout and
        To display both call and pick up option, leave everything as is
        For the layout of pre-delivery, change visibility of call to GONE and change the text of PickUp to 'Delivery Executive Picked Up
        For the layout after pickup, change visibility of pickup_accept to INVISIBLE'
        code with coniditon as the variable to be modified has been added accordingly
        * */


        /*
        Condition values, and behaviour
        0 -> call and picked up visible
        1 -> only picked up visible
        2 -> call and delivered visible
        */


        switch(condition)
        {
            case 0:
            {
                holder.caller.setVisibility(View.VISIBLE);
                holder.pickup_accept.setVisibility(View.VISIBLE);
                holder.pickup.setText("Picked Up");
                holder.header.setText("Buyer Pickup:");
                holder.header.setBackgroundColor(ContextCompat.getColor(mContext,R.color.ff_black));
            }
            case 1:
            {
                holder.caller.setVisibility(View.GONE);
                holder.pickup_accept.setVisibility(View.VISIBLE);
                holder.header.setText("Flashfetch Delivery:");
                holder.pickup.setText("Picked Up");
                holder.header.setBackgroundColor(ContextCompat.getColor(mContext,R.color.ff_green));

            }
            case 2:
            {
                holder.caller.setVisibility(View.VISIBLE);
                holder.pickup.setText("Delivered");
                holder.pickup_accept.setVisibility(View.VISIBLE);
                holder.header.setText("Deliver to buyer: "+mItems.get(position).buyer_name);
                holder.header.setBackgroundColor(ContextCompat.getColor(mContext,R.color.ff_red));
            }
        }

        Glide
                .with(mContext)
                .load(mItems.get(position).imgurl)
                .centerCrop()
                .into(holder.imageview);




        //mItems.get(position).email + " wants " + mItems.get(position).category + " at price Rs." + mItems.get(position).price);
        /*holder.notsfeed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Price");
                builder.setMessage("Enter price that you want to bargain");
                final EditText price = new EditText(mContext);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                price.setLayoutParams(lp);
                builder.setView(price);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                builder.show();

            }
        });*/


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}

