package com.abs2432gmail.daejeoninfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abs2432gmail.daejeoninfo.fragment.MainFragment1;
import com.abs2432gmail.daejeoninfo.fragment.MainFragment2;
import com.abs2432gmail.daejeoninfo.fragment.MainFragment3;
import com.pm10.library.CircleIndicator;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String TAG= "MainActivity";
        private Context mContext = MainActivity.this;
        private ViewPager viewPager;
        private Animation anim;
        RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new PagerAdapterClass(getSupportFragmentManager()));

        CircleIndicator circleIndicator = (CircleIndicator) findViewById(R.id.circle_indicator);
        circleIndicator.setupWithViewPager(viewPager);

        TextView tv = (TextView) findViewById(R.id.tv);
        tv.setSelected(true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext,3));

        final MainRecyclerViewAdapter mainRecyclerViewAdapter = new MainRecyclerViewAdapter(MakeItemData());
        recyclerView.setAdapter(mainRecyclerViewAdapter);

    }


    private class  PagerAdapterClass extends FragmentPagerAdapter{
        int MAX_PAGE = 3;

        public PagerAdapterClass(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            if (position <0 || MAX_PAGE <= position)
                return null;
            switch (position){
                case 0:
                    MainFragment1 mainFragment = new MainFragment1();
                    return mainFragment;
                case 1:
                    MainFragment2 mainFragment2 = new MainFragment2();
                    return mainFragment2;
                case 2:
                    MainFragment3 mainFragment3 = new MainFragment3();
                    return mainFragment3;
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return MAX_PAGE;
        }
    }


    private class MainRecyclerViewItemData{
        public int drawable;
        public String text;
        public Class aClass;

        public MainRecyclerViewItemData(int drawable, String text, Class aClass) {
            this.drawable = drawable;
            this.text = text;
            this.aClass = aClass;
        }
    }

    private ArrayList<MainRecyclerViewItemData> MakeItemData(){
        ArrayList<MainRecyclerViewItemData> mainRecyclerViewItemDataArrayList= new ArrayList<>();
        mainRecyclerViewItemDataArrayList.add(new MainRecyclerViewItemData(R.drawable.festival2,"축제",CultureActivity.class));
        mainRecyclerViewItemDataArrayList.add(new MainRecyclerViewItemData(R.drawable.news,"뉴스",NewsActivity.class));
        mainRecyclerViewItemDataArrayList.add(new MainRecyclerViewItemData(R.drawable.animal,"유기동물",PetActivity.class));
        mainRecyclerViewItemDataArrayList.add(new MainRecyclerViewItemData(R.drawable.hospital,"당직병원",HospitalActivity.class));
        mainRecyclerViewItemDataArrayList.add(new MainRecyclerViewItemData(R.drawable.exam,"시험",TestActivity.class));

        return mainRecyclerViewItemDataArrayList;
    }

    private class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder>{
        private ArrayList<MainRecyclerViewItemData> mainRecyclerViewItemDataes;

        public MainRecyclerViewAdapter(ArrayList<MainRecyclerViewItemData> dataes){
            mainRecyclerViewItemDataes = dataes;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            MainRecyclerViewItemData mainRecyclerViewItemData;
            RelativeLayout relativeLayout;
            ImageView imageView;
            TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                relativeLayout = itemView.findViewById(R.id.relativeLayout);
                imageView = itemView.findViewById(R.id.img);
                textView = itemView.findViewById(R.id.txt);

            }

            public void setListData(final MainRecyclerViewItemData data){
                this.mainRecyclerViewItemData = data;
                imageView.setImageResource(mainRecyclerViewItemData.drawable);
                textView.setText(mainRecyclerViewItemData.text);
                relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext,data.aClass);
                        startActivity(intent);
                    }
                });
            }

        }
        @NonNull
        @Override
        public MainRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.main_item,parent,false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MainRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.setListData(mainRecyclerViewItemDataes.get(position));
        }

        @Override
        public int getItemCount() {
            return mainRecyclerViewItemDataes.size();
        }

        public void add(MainRecyclerViewItemData mainRecyclerViewItemData){
            mainRecyclerViewItemDataes.add(mainRecyclerViewItemData);
            notifyDataSetChanged();
        }
    }
}
