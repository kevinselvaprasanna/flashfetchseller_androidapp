package in.flashfetch.sellerapp.Adapters;

/**
 * Created by SAM10795 on 02-03-2016.
 */


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import in.flashfetch.sellerapp.R;


import java.util.ArrayList;

public class NotificationAdapter1 extends RecyclerView.Adapter<NotificationAdapter1.ViewHolder> {

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


    public NotificationAdapter1(Context context, int LayoutSelect, ArrayList<Notification> items) {
        mContext = context;
        mItems = items;
        LayoutSelector = LayoutSelect;
    }
    public static class ViewHolder extends NotificationAdapter.ViewHolder{

        TextView name,time_left,price,price_quoted,quoted,price_amount,decline;
        ImageView imageview;
        CardView cv;

        public ViewHolder(View itemView) {
            super(itemView);
            imageview = (ImageView)itemView.findViewById(R.id.image);
            decline = (TextView)itemView.findViewById(R.id.decline);
            name =(TextView)itemView.findViewById(R.id.name);
            price = (TextView)itemView.findViewById(R.id.price);
            time_left = (TextView)itemView.findViewById(R.id.time_left);
            price_amount=(TextView)itemView.findViewById(R.id.price_amount);
            price_quoted=(TextView)itemView.findViewById(R.id.price_quoted);
            quoted = (TextView)itemView.findViewById(R.id.quoted);
            notsfeed = (LinearLayout)itemView.findViewById(R.id.notsfeed);
            cv = (CardView)itemView.findViewById(R.id.card_view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public NotificationAdapter1.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout;

      /*  font = Typeface.createFromAsset(mContext.getAssets(),
                "fonts/Lato-Medium.ttf");*/
               layout = R.layout.list_item_provided_1;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationAdapter1.ViewHolder holder, final int position) {

        //TODO: Populate items depending on the holder returned via LayoutSelect
        //TODO: Set typeface for text

        //th = new TimeHelper();
        holder.name.setText(mItems.get(position).name);
        holder.name.setTypeface(font);
        holder.price.setText(mItems.get(position).price);
        holder.price.setTypeface(font);
        holder.price_quoted.setText(String.valueOf(mItems.get(position).qprice));
        holder.price_quoted.setTypeface(font);
        holder.time_left.setText(String.valueOf(System.currentTimeMillis() - mItems.get(position).time));
        holder.time_left.setTypeface(font);
        holder.quoted.setTypeface(font);
        holder.price_amount.setTypeface(font);
        holder.quoted.setText("Quoted");
        holder.price_amount.setText("Amount");
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
