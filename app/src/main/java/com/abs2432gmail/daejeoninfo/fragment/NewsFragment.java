package com.abs2432gmail.daejeoninfo.fragment;


import android.content.Context;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abs2432gmail.daejeoninfo.R;
import com.abs2432gmail.daejeoninfo.WebViewActivity;
import com.bumptech.glide.Glide;


import org.json.JSONArray;

import org.json.JSONObject;


import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.abs2432gmail.daejeoninfo.Common.NetworkConstant.API_KEY;
import static com.abs2432gmail.daejeoninfo.Common.NetworkConstant.NEWS;

public class NewsFragment extends Fragment {
    private RecyclerView recyclerView;
    private NewsRecyclerViewAdapter adapter;
    private Context mContext;
    private String TAG = "NewsFragment";
    private String REQUEST_URL = NEWS + API_KEY;
    int page = 1;
    int totalPage = 2;
    String urlPage = REQUEST_URL + "&pageIndex=";
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;

    public NewsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        mContext = getActivity();

        linearLayoutManager = new LinearLayoutManager(mContext);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(linearLayoutManager);

        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.bottomborder,null));
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new NewsRecyclerViewAdapter(new ArrayList<NewsData>());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(onScrollListener);
        new AsyncTaskGetNewsData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return view;
    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;
            if (lastVisibleItemPosition == itemTotalCount) {
                if(page==totalPage){
                    Toast.makeText(mContext,"마지막입니다...",Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(mContext,"로딩중...",Toast.LENGTH_SHORT).show();
                page++;
                new AsyncTaskGetNewsData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    };

    public class NewsData {
        public String imgNEWS, title, date, newsUri;

        public NewsData(String imgNEWS, String title, String date, String newsUri) {
            this.imgNEWS = imgNEWS;
            this.title = title;
            this.date = date;
            this.newsUri = newsUri;
        }
    }

    private class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder> {
        private ArrayList<NewsData> datas;

        public NewsRecyclerViewAdapter(ArrayList<NewsData> datas) {
           this.datas = datas;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            NewsData recycleData;
            ImageView imgNEWS;
            TextView textView1, textView2;

            public ViewHolder(View itemView) {
                super(itemView);
                imgNEWS = itemView.findViewById(R.id.img2);
                textView1 = itemView.findViewById(R.id.newsTitle);
                textView2 = itemView.findViewById(R.id.newsDate);
            }

            public void setListData(NewsData data) {
                this.recycleData = data;
                Glide.with(mContext).load(recycleData.imgNEWS).into(imgNEWS);
                textView1.setText(recycleData.title);
                textView2.setText(recycleData.date);
            }
        }


        @NonNull
        @Override
        public NewsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemPosition = recyclerView.getChildAdapterPosition(view);
                    NewsData item = datas.get(itemPosition);
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("url", item.newsUri);
                    intent.putExtra("data", bundle);
                    startActivity(intent);

                }
            });
            NewsRecyclerViewAdapter.ViewHolder viewHolder = new NewsRecyclerViewAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull NewsRecyclerViewAdapter.ViewHolder viewHolder, int position) {
            viewHolder.setListData(datas.get(position));
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        public void add(NewsData recycleData) {
            datas.add(recycleData);
            notifyDataSetChanged();
        }
    }

    /**
     * AsyncTask 뉴스 정보 가져오기
     * AsyncTask<doInBackground()의 변수 종류, onProgressUpdate()에서 사용할 변수 종류, onPostExecute()에서 사용할 변수종류>
     * <p>
     * doInBackground()의 변수 종류 : 우리가 정의한 AsyncTask를 execute할 때 전해줄 값의 종류
     * onProgressUpdate()에서 사용할 변수 종류 : 진행상황을 업데이트할 때 전달할 값의 종류
     * onPostExecute()에서 사용할 변수종류 : AsyncTask가 끝난 뒤 결과값의 종류
     */
    private class AsyncTaskGetNewsData extends AsyncTask<Void, Integer, ArrayList<NewsData>> {
        private String TaskName = "AsyncTaskGetNewsData";

        //시작하기 전
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //완료된 후
        @Override
        protected void onPostExecute(ArrayList<NewsData> newsDatas) {
            super.onPostExecute(newsDatas);
            if(newsDatas == null)
                return;
            if(newsDatas.size() == 0)
                return;
            for (int i = 0; i < newsDatas.size(); i++){
                adapter.add(newsDatas.get(i));
            }
            adapter.notifyDataSetChanged();
        }

        //백그라운드
        @Override
        protected ArrayList<NewsData> doInBackground(Void... voids) {
            Boolean flag = null;
            String responseBody = "false";
            ArrayList<NewsData> datas = new ArrayList<>();
            MediaType jpegType = MediaType.parse("image/jpeg");
            MediaType pngType = MediaType.parse("image/png");
            try {
                OkHttpClient toServer = new OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .build();

                RequestBody requestBody = new FormBody.Builder()
                        .build();

                Request request = new Request.Builder()
                        .url(urlPage + page)
                        .get()
                        .build();

                Response response = toServer.newCall(request).execute();
                flag = response.isSuccessful();

                int responseCode = response.code();
                //200 연결잘됨

                //Log.d(TAG, "response.code() : " + String.valueOf(responseCode));
                if (flag) {
                    responseBody = String.valueOf(response.body().string());
                    //Log.e(TAG, "response.message() : " + response.message()); //응답에 대한 메세지(OK)
                    //Log.e(TAG, "response.body() : " + responseBody);
                    //Log.e(TAG, "flag :" + String.valueOf(flag));
                }

                if (responseBody.equals("false")) {
                    Log.d(TAG, TaskName + " : 서버 연결 실패");
                    return null;
                }

                JSONObject jsonObject = new JSONObject(responseBody);
                JSONObject pageInfo = jsonObject.getJSONObject("paginationInfo");
                totalPage = pageInfo.getInt("totalPageCount");

                JSONArray resultList = jsonObject.getJSONArray("resultList");

                int resultSize = resultList.length();
                if (resultSize == 0) {
                    Log.d(TAG, TaskName + " : 정보가 없습니다.");
                    return null;
                }

                for(int i = 0; i < resultSize; i++){
                    JSONObject data = resultList.getJSONObject(i);
                    String imgNEWS = data.getString("thumnailPath");
                    String title = data.getString("title");
                    String date = data.getString("mkDate");
                    String newsUri = data.getString("detailUrl");

                    NewsData newsData = new NewsData(imgNEWS,title,date,newsUri);

                    datas.add(newsData);
                }

            } catch (Exception e) {
                Log.d(TAG, TaskName + " : " + e.toString());
                return null;
            }
            return datas;
        }

        //갱신할 때
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
}