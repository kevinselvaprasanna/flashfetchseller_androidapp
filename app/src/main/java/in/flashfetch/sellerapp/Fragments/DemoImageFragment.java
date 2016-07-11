package in.flashfetch.sellerapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import in.flashfetch.sellerapp.R;

/**
 * Created by KRANTHI on 11-07-2016.
 */
public class DemoImageFragment extends Fragment {

    private static int[] images = new int[]{R.drawable.arrow_up,R.drawable.arrow_down};

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

        ImageView imageView = (ImageView)view.findViewById(R.id.demo_imageView);
        imageView.setBackgroundResource(images[getArguments().getInt("index")]);
    }
}
