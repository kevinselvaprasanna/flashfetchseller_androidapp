package in.flashfetch.sellerapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import in.flashfetch.sellerapp.Objects.Deal;
import in.flashfetch.sellerapp.R;

/**
 * Created by SAM10795 on 30-01-2016.
 */
public class DealsAdapter extends ArrayAdapter<Deal> {

    ArrayList<Deal> mdealslist;
    Context context;

    public DealsAdapter(Context context,ArrayList<Deal> deals)
    {
        super(context, R.layout.list_item_request);
        mdealslist = deals;
        this.context= context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Viewholder holder;
        if(convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_request,parent,false);
            holder = new Viewholder();
            holder.name = (TextView)convertView.findViewById(R.id.name);
            holder.price = (TextView)convertView.findViewById(R.id.price);
            holder.time = (TextView)convertView.findViewById(R.id.time);
            holder.image = (ImageView)convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Viewholder)convertView.getTag();
        }

        Deal deal = getItem(position);
        holder.name.setText(deal.getName());
        holder.price.setText(deal.getPrice());
        holder.time.setText(deal.getTime());
        holder.image.setImageBitmap(deal.getImage());

        return convertView;
    }

    static class Viewholder
    {
        TextView name;
        TextView price;
        TextView time;
        ImageView image;
    }
}
