package in.flashfetch.sellerapp.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import in.flashfetch.sellerapp.Accepted;
import in.flashfetch.sellerapp.Provided;
import in.flashfetch.sellerapp.Requested;

/**
 * Created by SAM10795 on 04-02-2016.
 */public class PagerAdapter extends FragmentStatePagerAdapter {

    private String[] titles = {"Requested", "Provided", "Accepted" };

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch(i){
            case 0:
                return new Requested();
            case 1:
                return new Provided();
            case 2:
                return new Accepted();
        }
        return null;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
