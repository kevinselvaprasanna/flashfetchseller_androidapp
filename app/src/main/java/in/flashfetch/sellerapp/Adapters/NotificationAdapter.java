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
import android.widget.ImageView;
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
    int LayoutSelector;

    /*0 -> item_notification
     1 -> list_item_provided_2
     2 -> list_item_provided_2
     3-> list_item_accepted
    */


    public NotificationAdapter(Context context, ArrayList<Notification> items, int LayoutSelect) {
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
            imageview = (ImageView)itemView.findViewById(R.id.imageView);
            name =(TextView)itemView.findViewById(R.id.name);
            price = (TextView)itemView.findViewById(R.id.price);
            time = (TextView)itemView.findViewById(R.id.time);
            decline = (TextView)itemView.findViewById(R.id.decline);
            quote = (TextView)itemView.findViewById(R.id.quote);
            notsfeed = (LinearLayout)itemView.findViewById(R.id.notsfeed);
            cv = (CardView)itemView.findViewById(R.id.card_view);
        }
    }

    public static class ViewHolder1 extends NotificationAdapter.ViewHolder{
        TextView name,price,time_left,price_offered;
        LinearLayout notsfeed;
        ImageView imageview;
        CardView cv;

        public ViewHolder1(View itemView) {
            super(itemView);
            imageview = (ImageView)itemView.findViewById(R.id.imageView);
            name =(TextView)itemView.findViewById(R.id.name);
            price = (TextView)itemView.findViewById(R.id.price);
            time_left = (TextView)itemView.findViewById(R.id.time_left);
            price_offered = (TextView)itemView.findViewById(R.id.price_offered);
            notsfeed = (LinearLayout)itemView.findViewById(R.id.notsfeed);
            cv = (CardView)itemView.findViewById(R.id.card_view);
        }
    }

    public static class ViewHolder2 extends NotificationAdapter.ViewHolder{
        TextView name,price_quoted,price_bargained,timeleft,decline,accept;
        LinearLayout notsfeed,requested,offered;
        ImageView imageview;
        CardView cv;

        public ViewHolder2(View itemView) {
            super(itemView);
            imageview = (ImageView)itemView.findViewById(R.id.imageView);
            name =(TextView)itemView.findViewById(R.id.name);
            price_quoted = (TextView)itemView.findViewById(R.id.price_quoted);
            price_bargained = (TextView)itemView.findViewById(R.id.price_bargain);
            timeleft = (TextView)itemView.findViewById(R.id.time_left);
            decline = (TextView)itemView.findViewById(R.id.decline);
            accept = (TextView)itemView.findViewById(R.id.accept);
            notsfeed = (LinearLayout)itemView.findViewById(R.id.notsfeed);
            requested = (LinearLayout)itemView.findViewById(R.id.requested);
            offered = (LinearLayout)itemView.findViewById(R.id.offered);
            cv = (CardView)itemView.findViewById(R.id.card_view);
        }
    }

    public static class ViewHolder3 extends NotificationAdapter.ViewHolder{
        TextView name,price_final;
        LinearLayout notsfeed,call,location;
        ImageView imageview;
        CardView cv;

        public ViewHolder3(View itemView) {
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

    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout;
        switch (LayoutSelector)
        {
            case 1:
                layout = R.layout.list_item_provided_1;
                break;
            case 2:
                layout = R.layout.list_item_provided_2;
                break;
            case 3:
                layout = R.layout.list_item_accepted;
                break;
            default:
                layout = R.layout.item_notifications;
        }
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        switch (LayoutSelector)
        {
            case 1:
                return new ViewHolder1(view);
            case 2:
                return new ViewHolder2(view);
            case 3:
                return new ViewHolder3(view);
            default:
                return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(NotificationAdapter.ViewHolder holder, final int position) {

        //TODO: Populate items depending on the holder returned via LayoutSelect

        //th = new TimeHelper();
        holder.name.setText(mItems.get(position).email);
        holder.price.setText(mItems.get(position).price);
        holder.time.setText(Long.toString(mItems.get(position).time));
        //mItems.get(position).email + " wants " + mItems.get(position).category + " at price Rs." + mItems.get(position).price);
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
