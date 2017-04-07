package qbai22.com.yandextranslator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qbai on 07.04.2017.
 */

public class ContainerFragment extends Fragment {

    @BindView(R.id.main_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.main_tab_layout)
    TabLayout mTabLayout;

    private int[] tabIcons = {
            R.drawable.ic_translate_logo_tab,
            R.drawable.ic_logo_history_tab,
            R.drawable.ic_bookmark_logo_tab
    };

    private ViewPagerAdapter mViewPagerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_containter, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
