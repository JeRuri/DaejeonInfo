package com.abs2432gmail.daejeoninfo;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.abs2432gmail.daejeoninfo.fragment.NewsFragment;


public class NewsActivity extends AppCompatActivity {
    private Context mContext = this;
    private ViewPager viewPager;
    private TabLayout tabLayout = null;
    private TabPagerAdapter tabPagerAdapter = null;
    private NewsFragment newsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        setTitle("뉴스");

        viewPager = (ViewPager) findViewById(R.id.viewPager2);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs2);
        tabLayout.setupWithViewPager(viewPager,true);
        viewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager()));
        tabLayout.getTabAt(0).setText("최신순");
        tabLayout.addOnTabSelectedListener(onTabSelectedListener);

    }

    TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int position = tab.getPosition();
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    public class TabPagerAdapter extends FragmentPagerAdapter {
        int MAX_PAGE = 1;

        public TabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            if (position < 0 || MAX_PAGE <= position)
                return null;
            Bundle bundle = new Bundle();
            switch (position) {
                case 0:
                    newsFragment = new NewsFragment();
                    bundle.putString("data", "최신순");
                    newsFragment.setArguments(bundle);
                    return newsFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return MAX_PAGE;
        }
    }
}
