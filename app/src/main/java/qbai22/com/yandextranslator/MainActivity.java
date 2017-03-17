package qbai22.com.yandextranslator;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setupViewPager();
        mTabLayout.setupWithViewPager(mViewPager);
        setupTabIcons();
    }

    private void setupViewPager() {
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.addFragment(new TranslationFragment(), getString(R.string.translation_tab_title));
        mViewPagerAdapter.addFragment(new HistoryFragment(), getString(R.string.history_tab_title));
        mViewPagerAdapter.addFragment(new BookmarkFragment(), getString(R.string.bookmark_tab_title));
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mViewPagerAdapter);
    }

    private void setupTabIcons() {
        mTabLayout.getTabAt(0).setIcon(tabIcons[0]);
        mTabLayout.getTabAt(1).setIcon(tabIcons[1]);
        mTabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }
}
