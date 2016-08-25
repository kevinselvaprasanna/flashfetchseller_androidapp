package in.flashfetch.sellerapp.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.flashfetch.sellerapp.Objects.Notification;
import in.flashfetch.sellerapp.R;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private Context context;
    private Typeface font;
    private ArrayList<Notification> notificationsArrayList;

    public NotificationsAdapter(Context context, ArrayList<Notification> notificationsArrayList) {
        this.context = context;
        this.notificationsArrayList = notificationsArrayList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView heading,description,timer;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.notifications_image_view);
            heading = (TextView)itemView.findViewById(R.id.notifications_heading);
            description = (TextView)itemView.findViewById(R.id.notifications_description);
            timer = (TextView)itemView.findViewById(R.id.notifications_timer);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public NotificationsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto_Medium.ttf");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nots, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Notification notification = notificationsArrayList.get(position);

        if(notification.getImageURL() != null) {
            Glide.with(context).load(notification.getImageURL()).centerCrop().into(holder.imageView);
        }else {
            holder.imageView.setVisibility(View.GONE);
        }

        holder.heading.setText(notification.getHeading());
        holder.description.setText(notification.getDescription());

        long time = System.currentTimeMillis();

        int difference = (int) ((time - notification.getTimeInMillis())/60000);

        if(difference < 60){
            holder.timer.setText(difference + "ago");
        }else{
            holder.timer.setText((difference/60) + "ago");
        }
    }

    @Override
    public int getItemCount() {
        return notificationsArrayList.size();
    }
}
