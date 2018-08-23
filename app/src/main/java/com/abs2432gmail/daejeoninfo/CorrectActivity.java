package com.abs2432gmail.daejeoninfo;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.abs2432gmail.daejeoninfo.fragment.CorrectFragment;

public class CorrectActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ImageView imageView;
    private Context mCotext = CorrectActivity.this;
    private CorrectFragment correctFragment;
    private TabPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correct);

        viewPager = (ViewPager) findViewById(R.id.correct_viewPager);
        tabLayout = (TabLayout) findViewById(R.id.correct_tabLayout);
        imageView = (ImageView) findViewById(R.id.correct_imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCotext, WebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url","https://www.daejeon.go.kr/drh/board/boardNormalList.do?boardId=normal_0096&menuSeq=1631");
                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });
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
        int max_page = 1;

        public TabPagerAdapter(FragmentManager fm){
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            if (position < 0 || max_page <= position)
                return null;

            switch (position){
                case 0:
                    correctFragment = new CorrectFragment();
                    return correctFragment;
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
