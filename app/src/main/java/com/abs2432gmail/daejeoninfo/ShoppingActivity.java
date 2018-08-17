package com.abs2432gmail.daejeoninfo;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.abs2432gmail.daejeoninfo.fragment.ShoppingFragment;


public class ShoppingActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout = null;
    private Context mContext = ShoppingActivity.this;
    private ShoppingFragment shoppingFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        viewPager = (ViewPager) findViewById(R.id.shopping_viewPager);
        tabLayout = (TabLayout) findViewById(R.id.shopping_tab);
        tabLayout.setupWithViewPager(viewPager, true);
        viewPager.setAdapter(new Shopping_TabPagerAdapter(getSupportFragmentManager()));
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

    public class Shopping_TabPagerAdapter extends FragmentPagerAdapter {
        int max_page = 5;

        public Shopping_TabPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position < 0 || max_page <= position)
                return null;

            Bundle bundle = new Bundle();

            switch (position) {
                case 0:
                    shoppingFragment = new ShoppingFragment();
                    bundle.putString("data","유성구");
                    shoppingFragment.setArguments(bundle);
                    return shoppingFragment;
                case 1:
                    shoppingFragment = new ShoppingFragment();
                    bundle.putString("data","서구");
                    shoppingFragment.setArguments(bundle);
                    return shoppingFragment;
                case 2:
                    shoppingFragment = new ShoppingFragment();
                    bundle.putString("data","대덕구");
                    shoppingFragment.setArguments(bundle);
                    return shoppingFragment;
                case 3:
                    shoppingFragment = new ShoppingFragment();
                    bundle.putString("data","동구");
                    shoppingFragment.setArguments(bundle);
                    return shoppingFragment;
                case 4:
                    shoppingFragment = new ShoppingFragment();
                    bundle.putString("data","중구");
                    shoppingFragment.setArguments(bundle);
                    return shoppingFragment;
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
