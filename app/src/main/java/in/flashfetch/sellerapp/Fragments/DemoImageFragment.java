package in.flashfetch.sellerapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import in.flashfetch.sellerapp.R;

/**
 * Created by KRANTHI on 11-07-2016.
 */
public class DemoImageFragment extends Fragment {

    private static int[] images = new int[]{R.drawable.demo,R.drawable.demo_2,R.drawable.demo_3,R.drawable.demo_4,R.drawable.demo_5};

    public static DemoImageFragment newInstance(int index){
        DemoImageFragment demoImageFragment = new DemoImageFragment();

        Bundle args = new Bundle();
        args.putInt("index", index);
        demoImageFragment.setArguments(args);

        return demoImageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.demo_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int index = getArguments().getInt("index");

        ImageView imageView = (ImageView)view.findViewById(R.id.demo_imageView);
        Glide.with(getActivity()).load(images[index]).into(imageView);

        if(index == 4){
            Toast.makeText(getContext(),"You reached end of demo",Toast.LENGTH_SHORT).show();
        }
    }
}
