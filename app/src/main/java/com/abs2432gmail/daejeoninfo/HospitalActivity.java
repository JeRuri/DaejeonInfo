package com.abs2432gmail.daejeoninfo;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import com.abs2432gmail.daejeoninfo.fragment.HospitalFragment;


public class HospitalActivity extends AppCompatActivity {
    private Context mContext = this;
    private ViewPager viewPager;
    private TabLayout tabLayout = null;
    private TabPagerAdapter tabPagerAdapter = null;
    private HospitalFragment hospitalFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
        setTitle("당직병원");

        viewPager = (ViewPager) findViewById(R.id.viewPager5);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs5);
        tabLayout.setupWithViewPager(viewPager, true);
        viewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager()));
        tabLayout.getTabAt(0).setText("전체");
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

        public TabPagerAdapter(FragmentManager fm){
            super (fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position < 0 || MAX_PAGE <= position)
            return null;

            Bundle bundle = new Bundle();
            switch (position) {
                case 0:
                    hospitalFragment = new HospitalFragment();
                    bundle.putString("data","전체");
                    hospitalFragment.setArguments(bundle);
                    return hospitalFragment;

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
