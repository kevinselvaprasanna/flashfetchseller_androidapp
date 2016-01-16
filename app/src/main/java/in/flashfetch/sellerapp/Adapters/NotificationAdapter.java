package in.flashfetch.sellerapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.flashfetch.sellerapp.Objects.Notification;
import in.flashfetch.sellerapp.R;


import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    Context mContext;
    ArrayList<Notification> mItems;
    //TimeHelper th;
    private static String LOG_TAG = "EventDetails";


    public NotificationAdapter(Context context, ArrayList<Notification> items) {
        mContext = context;
        mItems = items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvheading, tvnot;
        LinearLayout notsfeed;
        CardView cv;

        public ViewHolder(View itemView) {
            super(itemView);
            tvheading =(TextView)itemView.findViewById(R.id.tvHeading);
            tvnot = (TextView)itemView.findViewById(R.id.tvNot);
            notsfeed = (LinearLayout)itemView.findViewById(R.id.notsfeed);
            cv = (CardView)itemView.findViewById(R.id.card_view);
        }
    }

    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notifications, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationAdapter.ViewHolder holder, final int position) {

        //th = new TimeHelper();

        holder.tvheading.setText(mItems.get(position).category);
        holder.tvnot.setText(mItems.get(position).email + " wants " + mItems.get(position).category + " at price Rs."+ mItems.get(position).price);
        holder.notsfeed.setOnClickListener(new View.OnClickListener() {

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
        });


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
