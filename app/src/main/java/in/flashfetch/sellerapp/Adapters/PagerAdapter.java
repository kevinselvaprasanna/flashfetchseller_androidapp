package in.flashfetch.sellerapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import in.flashfetch.sellerapp.Fragments.Accepted;
import in.flashfetch.sellerapp.Fragments.Provided;
import in.flashfetch.sellerapp.Fragments.Requested;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private String[] titles = {"Requested", "Provided", "Accepted" };
    private final FragmentManager fm;
    private Context mContext;
    private Bundle bundle = new Bundle();
    private Fragment fragment;

    public PagerAdapter(FragmentManager fm, Context context, boolean isAccessed) {
        super(fm);
        this.fm = fm;
        mContext = context;

        bundle.putBoolean("ACCESS",isAccessed);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                fragment = new Requested();
                fragment.setArguments(bundle);
                return fragment;
            case 1:
                fragment = new Provided();
                fragment.setArguments(bundle);
                return fragment;
            case 2:
                fragment = new Accepted();
                fragment.setArguments(bundle);
                return fragment;
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
