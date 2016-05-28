package in.flashfetch.sellerapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v4.view.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import in.flashfetch.sellerapp.R;
import in.flashfetch.sellerapp.StartActivity;

/**
 * Created by SAM10795 on 20-04-2016.
 */
public class IntroPagerAdapter extends android.support.v4.view.PagerAdapter{

    private Context mcontext;
    LayoutInflater layoutInflater;
    //int[] res = {R.drawable.new_res,R.mipmap.page2,R.mipmap.page3,R.drawable.get_started_res};
    int[] res = {R.mipmap.page1,R.mipmap.page2,R.mipmap.page3,R.mipmap.page4};

    public IntroPagerAdapter(Context context)
    {
        mcontext = context;
        layoutInflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //container.removeView((FrameLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = layoutInflater.inflate(R.layout.intro_pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        imageView.setImageResource(res[position]);

        ImageView thumb1 = (ImageView)itemView.findViewById(R.id.view_screen_1);
        ImageView thumb2 = (ImageView)itemView.findViewById(R.id.view_screen_2);
        ImageView thumb3 = (ImageView)itemView.findViewById(R.id.view_screen_3);
        ImageView thumb4 = (ImageView)itemView.findViewById(R.id.view_screen_4);

        final Button button = (Button)itemView.findViewById(R.id.get_start);
        button.setVisibility(View.GONE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext,StartActivity.class);
                mcontext.startActivity(intent);
            }
        });

        switch (position)
        {
            case 0:
                thumb1.setScaleX(2);
                thumb1.setScaleY(2);
                break;

            case 1:
                thumb2.setScaleX(2);
                thumb2.setScaleY(2);
                break;

            case 2:
                thumb3.setScaleX(2);
                thumb3.setScaleY(2);
                break;

            case 3:
                thumb4.setScaleX(2);
                thumb4.setScaleY(2);
                button.setVisibility(View.VISIBLE);
                break;
        }


        container.addView(itemView);

        return itemView;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
