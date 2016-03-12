package in.flashfetch.sellerapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import in.flashfetch.sellerapp.Objects.Notification;
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

    /*0 -> item_notification
     1 -> list_item_provided_2
     2 -> list_item_provided_2
     3-> list_item_accepted
    */


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
    public void onBindViewHolder(NotificationAdapter.ViewHolder holder, final int position) {


        //th = new TimeHelper();
        holder.name.setText(mItems.get(position).name);
        /*holder.name.setTypeface(font);
        holder.price.setTypeface(font);
        holder.time.setTypeface(font);*/
        holder.price.setText(String.valueOf(mItems.get(position).price));
        holder.time.setText(String.valueOf(System.currentTimeMillis()-mItems.get(position).time)+ "s");
        holder.quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, QuoteActivity.class);
                //TODO: Populate the intent with required data
                mContext.startActivity(intent);
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
                mContext.startActivity(new Intent(mContext, QuoteActivity.class));
            }
        });


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
