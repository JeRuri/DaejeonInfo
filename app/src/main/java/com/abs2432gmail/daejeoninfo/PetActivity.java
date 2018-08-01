package com.abs2432gmail.daejeoninfo;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.abs2432gmail.daejeoninfo.fragment.PetFragment;


public class PetActivity extends AppCompatActivity {
    private Context mContext = this;
    private ViewPager viewPager;
    private TabLayout tabLayout = null;
    private TabPagerAdapter tabPagerAdapter = null;
    private PetFragment petFragment, petFragment1, petFragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);
        setTitle("유기동물");

        viewPager = (ViewPager) findViewById(R.id.viewPager3);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs3);
        tabLayout.setupWithViewPager(viewPager, true);
        viewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager()));
        tabLayout.getTabAt(0).setText("최신순");
        tabLayout.getTabAt(1).setText("강아지");
        tabLayout.getTabAt(2).setText("고양이");
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
        int MAX_PAGE = 3;

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
                    petFragment = new PetFragment();
                    bundle.putString("data3","최신순");
                    petFragment.setArguments(bundle);
                    return petFragment;

                case 1:
                    petFragment1 = new PetFragment();
                    bundle.putString("data3","강아지");
                    petFragment1.setArguments(bundle);
                    return petFragment1;

                case 2:
                    petFragment2 = new PetFragment();
                    bundle.putString("data3","고양이");
                    petFragment2.setArguments(bundle);
                    return petFragment2;

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
