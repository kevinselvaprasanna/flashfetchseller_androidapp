package in.flashfetch.sellerapp.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;

import in.flashfetch.sellerapp.Fragments.DemoImageFragment;

/**
 * Created by KRANTHI on 11-07-2016.
 */
public class DemoViewPagerAdapter extends FragmentStatePagerAdapter{

    private FragmentManager fragmentManager;

    public DemoViewPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        DemoImageFragment demoImageFragment = DemoImageFragment.newInstance(position);
        return demoImageFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
