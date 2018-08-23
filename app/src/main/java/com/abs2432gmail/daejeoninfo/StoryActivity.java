package com.abs2432gmail.daejeoninfo;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.abs2432gmail.daejeoninfo.fragment.StoryFragment;

public class StoryActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private StoryFragment storyFragment;
    private ImageView imageView;
    private Context mContext = StoryActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        viewPager = (ViewPager) findViewById(R.id.story_viewPager);
        tabLayout = (TabLayout) findViewById(R.id.story_tabLayout);
        imageView = (ImageView) findViewById(R.id.story_imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", "https://www.daejeon.go.kr/drh/drhStoryDaejeonList.do?boardId=blog_0001&menuSeq=1479");
                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });
        tabLayout.setupWithViewPager(viewPager,true);
        viewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager()));
        tabLayout.getTabAt(0).setText("소통하는행정");
        tabLayout.getTabAt(1).setText("희망원도심으로");
        tabLayout.getTabAt(2).setText("함께사회적자본");
        tabLayout.getTabAt(3).setText("경제복지환경");
        tabLayout.getTabAt(4).setText("문화예술관광");
        tabLayout.getTabAt(5).setText("과학도시대전");
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

    public class TabPagerAdapter extends FragmentPagerAdapter{
        int max_page = 6;

        public TabPagerAdapter(android.support.v4.app.FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position < 0 || max_page <= position)
                return null;

            Bundle bundle = new Bundle();

            switch (position){
                case 0:
                    storyFragment = new StoryFragment();
                    bundle.putString("data","소통하는행정");
                    storyFragment.setArguments(bundle);
                    return storyFragment;

                case 1:
                    storyFragment = new StoryFragment();
                    bundle.putString("data","희망원도심으로");
                    storyFragment.setArguments(bundle);
                    return storyFragment;

                case 2:
                    storyFragment = new StoryFragment();
                    bundle.putString("data","함께사회적자본");
                    storyFragment.setArguments(bundle);
                    return storyFragment;

                case 3:
                    storyFragment = new StoryFragment();
                    bundle.putString("data","경제복지환경");
                    storyFragment.setArguments(bundle);
                    return storyFragment;

                case 4:
                    storyFragment = new StoryFragment();
                    bundle.putString("data","문화예술관광");
                    storyFragment.setArguments(bundle);
                    return storyFragment;

                case 5:
                    storyFragment = new StoryFragment();
                    bundle.putString("data","과학도시대전");
                    storyFragment.setArguments(bundle);
                    return storyFragment;

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
