package com.abs2432gmail.daejeoninfo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.abs2432gmail.daejeoninfo.fragment.MainFragment1;
import com.abs2432gmail.daejeoninfo.fragment.MainFragment2;
import com.abs2432gmail.daejeoninfo.fragment.MainFragment3;
import com.pm10.library.CircleIndicator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static com.kakao.util.maps.helper.Utility.getPackageInfo;

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

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position){
                    case 0:
                        Intent intent = new Intent(mContext, CultureActivity.class);
                        startActivity(intent);
                        break;

                    case 1:
                        Intent intent1 = new Intent(mContext, NewsActivity.class);
                        startActivity(intent1);
                        break;

                    case 2:
                        Intent intent2= new Intent(mContext, PetActivity.class);
                        startActivity(intent2);
                        break;

                    case 3:
                        Intent intent3= new Intent(mContext, HospitalActivity.class);
                        startActivity(intent3);
                        break;

                    case 4:
                        Intent intent4= new Intent(mContext, TestActivity.class);
                        startActivity(intent4);
                        break;

                     default:
                         break;
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                Intent intent = new Intent(mContext, CultureActivity.class);
                startActivity(intent);
            }
        }));

    }

    public interface ClickListener{
        public void onClick(View view, int position);
        public void onLongClick (View view, int  position);
    }

    private class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{
        private ClickListener clickListener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener){
            this.clickListener = (ClickListener) clickListener;
            gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }


      @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
          View child = rv.findChildViewUnder(e.getX(),e.getY());
          if(child!=null && clickListener!=null && gestureDetector.onTouchEvent(e)){
              clickListener.onClick(child,rv.getChildAdapterPosition(child));
          }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) { }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) { }

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

        public MainRecyclerViewItemData(int drawable, String text) {
            this.drawable = drawable;
            this.text = text;
        }
    }

    private ArrayList<MainRecyclerViewItemData> MakeItemData(){
        ArrayList<MainRecyclerViewItemData> mainRecyclerViewItemDataArrayList= new ArrayList<>();
        mainRecyclerViewItemDataArrayList.add(new MainRecyclerViewItemData(R.drawable.festival2,"축제"));
        mainRecyclerViewItemDataArrayList.add(new MainRecyclerViewItemData(R.drawable.news,"뉴스"));
        mainRecyclerViewItemDataArrayList.add(new MainRecyclerViewItemData(R.drawable.animal,"유기동물"));
        mainRecyclerViewItemDataArrayList.add(new MainRecyclerViewItemData(R.drawable.hospital,"당직병원"));
        mainRecyclerViewItemDataArrayList.add(new MainRecyclerViewItemData(R.drawable.exam,"시험"));
        mainRecyclerViewItemDataArrayList.add(new MainRecyclerViewItemData(R.drawable.job,"구직"));

        return mainRecyclerViewItemDataArrayList;
    }

    private class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder>{
        private ArrayList<MainRecyclerViewItemData> mainRecyclerViewItemDataes;

        public MainRecyclerViewAdapter(ArrayList<MainRecyclerViewItemData> dataes){
            mainRecyclerViewItemDataes = dataes;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            MainRecyclerViewItemData mainRecyclerViewItemData;
            Button button;
            ImageView imageView;
            TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                button = itemView.findViewById(R.id.btn);
                imageView = itemView.findViewById(R.id.img);
                textView = itemView.findViewById(R.id.txt);
            }

            public void setListData(MainRecyclerViewItemData data){
                this.mainRecyclerViewItemData = data;
                imageView.setImageResource(mainRecyclerViewItemData.drawable);
                textView.setText(mainRecyclerViewItemData.text);
            }

        }
        @NonNull
        @Override
        public MainRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.activity_main_item,parent,false);
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
