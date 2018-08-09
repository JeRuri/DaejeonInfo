package com.abs2432gmail.daejeoninfo;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.abs2432gmail.daejeoninfo.fragment.ParkFragment;

public class ParkActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout = null;
    private Context mContext = ParkActivity.this;
    private ParkFragment parkFragment;
    private Park_TabPagerAdapter tabPagerAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park);

        viewPager = (ViewPager) findViewById(R.id.viewPager_park);
        tabLayout = (TabLayout) findViewById(R.id.park_tabLayout);
        tabLayout.setupWithViewPager(viewPager, true);
        viewPager.setAdapter(new Park_TabPagerAdapter(getSupportFragmentManager()));
        tabLayout.getTabAt(0).setText("유성구");
        tabLayout.getTabAt(1).setText("서구");
        tabLayout.getTabAt(2).setText("대덕구");
        tabLayout.getTabAt(3).setText("동구");
        tabLayout.getTabAt(4).setText("중구");
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


    public class Park_TabPagerAdapter extends FragmentPagerAdapter {
        int max_page = 5;

        public Park_TabPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            if (position < 0 || max_page <= position)
            return null;

            Bundle bundle = new Bundle();

            switch (position) {
                case 0:
                    parkFragment = new ParkFragment();
                    bundle.putString("data","유성구");
                    parkFragment.setArguments(bundle);
                    return parkFragment;

                case 1:
                    parkFragment = new ParkFragment();
                    bundle.putString("data", "서구");
                    parkFragment.setArguments(bundle);
                    return parkFragment;

                case 2:
                    parkFragment = new ParkFragment();
                    bundle.putString("data","대덕구");
                    parkFragment.setArguments(bundle);
                    return parkFragment;

                case 3:
                    parkFragment = new ParkFragment();
                    bundle.putString("data","동구");
                    parkFragment.setArguments(bundle);
                    return parkFragment;

                case 4:
                    parkFragment = new ParkFragment();
                    bundle.putString("data","중구");
                    parkFragment.setArguments(bundle);
                    return parkFragment;

                default:
                        return null;
            }
        }

        @Override
        public int getCount() {
            return max_page;
        }
    }
}
