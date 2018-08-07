package com.abs2432gmail.daejeoninfo;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.abs2432gmail.daejeoninfo.fragment.TestFragment;


public class TestActivity extends AppCompatActivity {
    private Context mContext = TestActivity.this;
    private ViewPager viewPager;
    private TabLayout tabLayout = null;
    private TabPagerAdapter tabPagerAdapter = null;
    private TestFragment testFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setTitle("시험정보");

        viewPager = (ViewPager) findViewById(R.id.viewPager4);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs4);
        tabLayout.setupWithViewPager(viewPager, true);
        viewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager()));
        tabLayout.getTabAt(0).setText("대전시");
        tabLayout.getTabAt(1).setText("임기제");
        tabLayout.getTabAt(2).setText("자격증");
        tabLayout.getTabAt(3).setText("공무원");
        tabLayout.getTabAt(4).setText("타기관");
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
        int MAX_PAGE = 5;

        public TabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            if(position < 0 || MAX_PAGE <= position)
                return null;
            Bundle bundle = new Bundle();
            switch (position) {
                case 0:
                    testFragment = new TestFragment();
                    bundle.putString("data","대전시");
                    testFragment.setArguments(bundle);
                    return testFragment;
                case 1:
                    testFragment = new TestFragment();
                    bundle.putString("data","임기제");
                    testFragment.setArguments(bundle);
                    return testFragment;
                case 2:
                    testFragment = new TestFragment();
                    bundle.putString("data","자격증");
                    testFragment.setArguments(bundle);
                    return testFragment;
                case 3:
                    testFragment = new TestFragment();
                    bundle.putString("data","공무원");
                    testFragment.setArguments(bundle);
                    return testFragment;
                case 4:
                    testFragment = new TestFragment();
                    bundle.putString("data","타기관");
                    testFragment.setArguments(bundle);
                    return testFragment;
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
