package in.flashfetch.sellerapp.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.flashfetch.sellerapp.Objects.Nots;
import in.flashfetch.sellerapp.R;

/**
 * Created by kevinselvaprasanna on 4/1/16.
 */
public class NotAdapter extends RecyclerView.Adapter<NotAdapter.ViewHolder> {

    Context mContext;
    Typeface font;
    ArrayList<Nots> mItems;

    /*0 -> item_notification
     1 -> list_item_provided_2
     2 -> list_item_provided_2
     3-> list_item_accepted
    */


    public NotAdapter(Context context, ArrayList<Nots> items) {
        mContext = context;
        mItems = items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView not;

        public ViewHolder(View itemView) {
            super(itemView);
            not = (TextView)itemView.findViewById(R.id.not);

        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public NotAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout;


       /* font = Typeface.createFromAsset(mContext.getAssets(),
                "fonts/Lato-Medium.ttf");*/

        layout = R.layout.item_nots;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.not.setText(mItems.get(position).text);
    }



    @Override
    public int getItemCount() {
        return mItems.size();
    }


}
