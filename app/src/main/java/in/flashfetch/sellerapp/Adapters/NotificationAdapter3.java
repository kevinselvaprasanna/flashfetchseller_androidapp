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
import android.widget.Toast;

import in.flashfetch.sellerapp.Objects.Notification;
import in.flashfetch.sellerapp.R;


import java.util.ArrayList;

public class NotificationAdapter3 extends RecyclerView.Adapter<NotificationAdapter3.ViewHolder> {

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


    public NotificationAdapter3(Context context, int LayoutSelect) {
        mContext = context;
        //mItems = items;
        LayoutSelector = LayoutSelect;
    }


    public static class ViewHolder extends NotificationAdapter.ViewHolder{
        TextView name,price_final;
        LinearLayout notsfeed,call,location;
        ImageView imageview;
        CardView cv;

        public ViewHolder(View itemView) {
            super(itemView);
            imageview = (ImageView)itemView.findViewById(R.id.imageView);
            name =(TextView)itemView.findViewById(R.id.name);
            price_final = (TextView)itemView.findViewById(R.id.price_final);
            notsfeed = (LinearLayout)itemView.findViewById(R.id.notsfeed);
            call = (LinearLayout)itemView.findViewById(R.id.call);
            location = (LinearLayout)itemView.findViewById(R.id.location);
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

        holder.name.setText("Name");
        holder.name.setTypeface(font);
        holder.price_final.setTypeface(font);
        holder.price_final.setText("Final price");
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"Call",Toast.LENGTH_SHORT).show();
            }
        });
        holder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"Locate",Toast.LENGTH_SHORT).show();
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
        return 5;//mItems.size();
    }
}

