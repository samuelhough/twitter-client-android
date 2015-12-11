package com.codepath.apps.twitterclient.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.codepath.apps.twitterclient.activities.fragments.BaseTimelineFragment;
import com.codepath.apps.twitterclient.activities.fragments.MentionsFragment;
import com.codepath.apps.twitterclient.activities.fragments.TimelineFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samuelhough on 12/9/15.
 */
public class HomeFragmentStatePagerAdapter extends SmartFragmentStatePagerAdapter {

    List<Class<?>> fragments = new ArrayList<>();

    public static interface FragmentInfo{
        public String getFragmentTitle();
    }

    FragmentManager mManager;
    public HomeFragmentStatePagerAdapter(FragmentManager manager){
        super(manager);

        fragments.add(TimelineFragment.class);
        fragments.add(MentionsFragment.class);

        mManager = manager;
    }

    @Override
    public Fragment getItem(int position) {
        Class<?> FragKlass = fragments.get(position);
        try {
            return (Fragment) FragKlass.newInstance();
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position){
        System.out.println("Get page");
        BaseTimelineFragment frag = (BaseTimelineFragment) getItem(position);
        return frag.getFragmentTitle();
    }


}
