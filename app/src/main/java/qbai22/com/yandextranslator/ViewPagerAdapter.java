package qbai22.com.yandextranslator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qbai on 15.03.2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentsList = new ArrayList<>();
    private final List<String> mFragmentsTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentsList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentsList.add(fragment);
        mFragmentsTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }
}

