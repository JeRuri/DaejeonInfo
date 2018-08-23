package com.abs2432gmail.daejeoninfo.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abs2432gmail.daejeoninfo.OkHttp.OkHttpAPICall;
import com.abs2432gmail.daejeoninfo.OkHttp.OkHttpInitSingletonManager;
import com.abs2432gmail.daejeoninfo.R;
import com.bumptech.glide.Glide;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Response;

import static com.abs2432gmail.daejeoninfo.Common.NetworkConstant.API_KEY;
import static com.abs2432gmail.daejeoninfo.Common.NetworkConstant.STORY;

public class StoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private Context mContext;
    private LinearLayoutManager linearLayoutManager;
    private String StoryURL = STORY + API_KEY + "&pageNo=";
    private int page = 1;
    private int totalPage = 2;
    public StoryFragment() {}
    private StoryRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story, container,false);
        mContext = getActivity();
        linearLayoutManager = new LinearLayoutManager(mContext);

        String strText = getArguments().getString("data");
        if (strText.equals("소통하는행정")){
            StoryURL = STORY + API_KEY + "&categorySeq=291"+"&pageNo=";
        } else if (strText.equals("희망원도심으로")){
            StoryURL = STORY + API_KEY + "&categorySeq=292"+"&pageNo=";
        } else if (strText.equals("함께사회적자본")){
            StoryURL = STORY + API_KEY + "&categorySeq=293"+"&pageNo=";
        } else if (strText.equals("경제복지환경")){
            StoryURL = STORY + API_KEY + "&categorySeq=294"+"&pageNo=";
        } else if (strText.equals("문화예술관광")){
            StoryURL = STORY + API_KEY + "&categorySeq=295"+"&pageNo=";
        } else if (strText.equals("과학도시대전")){
            StoryURL = STORY + API_KEY + "&categorySeq=296"+"&pageNo=";
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.story_recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new StoryRecyclerViewAdapter(new ArrayList<StoryData>());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(onScrollListener);

        new AsyncTaskGetStoryData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return view;
    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;
            if (lastVisibleItemPosition == itemTotalCount){
                if (page == totalPage){
                    Toast.makeText(mContext, "마지막입니다...", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(mContext, "로딩중...", Toast.LENGTH_SHORT).show();
                page++;
                new AsyncTaskGetStoryData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    };
    public class StoryData {
        String categoryNm, deptNm, regDtTm, title, thumbnail;

        public StoryData(String categoryNm, String deptNm, String regDtTm, String title, String thumbnail){
            this.categoryNm = categoryNm;
            this.deptNm = deptNm;
            this.regDtTm = regDtTm;
            this.title = title;
            this.thumbnail = thumbnail;
        }
    }

    private class StoryRecyclerViewAdapter extends RecyclerView.Adapter<StoryRecyclerViewAdapter.ViewHolder>{
        private ArrayList<StoryData> datas;

        public StoryRecyclerViewAdapter(ArrayList<StoryData> datas){
            this.datas = datas;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            StoryData recyclerData;
            ImageView imageView;
            TextView textView1, textView2, textView3, textView4;

            public ViewHolder(View itemView){
                super(itemView);
                textView1 = itemView.findViewById(R.id.story_category);
                textView2 = itemView.findViewById(R.id.story_title);
                imageView = itemView.findViewById(R.id.story_image);
                textView3 = itemView.findViewById(R.id.story_date);
                textView4 = itemView.findViewById(R.id.story_dept);
            }

            public void setListData(StoryData data){
                this.recyclerData = data;
                Glide.with(mContext).load("http://www.daejeon.go.kr/"+recyclerData.thumbnail).into(imageView);
                textView1.setText("["+recyclerData.categoryNm+"]");
                textView2.setText(recyclerData.title);
                textView3.setText(recyclerData.regDtTm);
                textView4.setText(recyclerData.deptNm);
            }
        }

        @NonNull
        @Override
        public StoryRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_item, parent, false);
            StoryRecyclerViewAdapter.ViewHolder viewHolder = new StoryRecyclerViewAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull StoryRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.setListData(datas.get(position));
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        public void add(StoryData recyclerData){
            datas.add(recyclerData);
        }
    }

    XmlParserCreator xmlParserCreator = new XmlParserCreator() {
        @Override
        public XmlPullParser createParser() {
            try {
                return XmlPullParserFactory.newInstance().newPullParser();
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    };

    private class MyResponse{
        Body body;
        private class Body{
            List<StoryData> items;
            int numOfRows;
            int pageNo;
            int totalCount;
        }
    }

    private class AsyncTaskGetStoryData extends AsyncTask<Void, Integer, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            OkHttpClient client = OkHttpInitSingletonManager.getOkHttpClient();
            Response response = null;

            try{
                response = OkHttpAPICall.GET(client,StoryURL + page);
                MyResponse myResponse = new GsonXmlBuilder()
                        .setXmlParserCreator(xmlParserCreator)
                        .setPrimitiveArrays(true)
                        .setSkipRoot(true)
                        .create()
                        .fromXml(response.body().string(), MyResponse.class);

                int itemSize = myResponse.body.items.size();
                totalPage = myResponse.body.totalCount/10 + 1;

                for (int i = 0; i < itemSize; i++) {
                    adapter.add(myResponse.body.items.get(i));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
