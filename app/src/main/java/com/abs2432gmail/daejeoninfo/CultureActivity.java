package com.abs2432gmail.daejeoninfo;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.abs2432gmail.daejeoninfo.fragment.CultureFragment;

public class CultureActivity extends AppCompatActivity {
    private Context mContext = CultureActivity.this;
    private ViewPager viewPager;
    private TabLayout tabLayout = null;
    private TabPagerAdapter tabPagerAdapter = null;
    private CultureFragment cultureFragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_culture);
        setTitle("축제");

        viewPager = (ViewPager) findViewById(R.id.viewPager1);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs1);
        tabLayout.setupWithViewPager(viewPager, true);
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
        public void onTabUnselected(TabLayout.Tab tab) { }

        @Override
        public void onTabReselected(TabLayout.Tab tab) { }
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
                    cultureFragment1 = new CultureFragment();
                    bundle.putString("data", "최신순");
                    cultureFragment1.setArguments(bundle);
                    return cultureFragment1;
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
