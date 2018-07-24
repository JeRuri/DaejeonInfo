package com.abs2432gmail.daejeoninfo.fragment;


import android.content.Context;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abs2432gmail.daejeoninfo.R;
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
    private String mTAG = "NewsFragment";
    private ProgressBar progressBar;
    private String REQUEST_URL = NEWS + API_KEY;

    public NewsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        mContext = getActivity();

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar) ;

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new NewsRecyclerViewAdapter(new ArrayList<NewsData>());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;
                if (lastVisibleItemPosition == itemTotalCount) {
                    Toast.makeText(mContext, "마지막입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        new AsyncTaskGetNewsData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return view;
    }


    public class NewsData {
        public String imgNEWS, title, date, content, newsUri;

        public NewsData(String imgNEWS, String title, String date, String content, String newsUri) {
            this.imgNEWS = imgNEWS;
            this.title = title;
            this.date = date;
            this.content = content;
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
            TextView textView1, textView2, textView3;

            public ViewHolder(View itemView) {
                super(itemView);
                imgNEWS = itemView.findViewById(R.id.img2);
                textView1 = itemView.findViewById(R.id.newsTitle);
                textView2 = itemView.findViewById(R.id.newsDate);
                textView3 = itemView.findViewById(R.id.newsContent);
            }

            public void setListData(NewsData data) {
                this.recycleData = data;
                Glide.with(mContext).load(recycleData.imgNEWS).into(imgNEWS);
                textView1.setText(recycleData.title);
                textView2.setText(recycleData.date);
                textView3.setText(recycleData.content);
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
                    Toast.makeText(mContext,"해당페이지로 이동합니다", Toast.LENGTH_LONG ).show();
                    Uri uri = Uri.parse(item.newsUri);
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
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
            adapter = new NewsRecyclerViewAdapter(newsDatas);
            recyclerView.setAdapter(adapter);
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
                        .url(REQUEST_URL)
                        .get()
                        .build();

                Response response = toServer.newCall(request).execute();
                flag = response.isSuccessful();

                int responseCode = response.code();
                //200 연결잘됨

                //Log.d(mTAG, "response.code() : " + String.valueOf(responseCode));
                if (flag) {
                    responseBody = String.valueOf(response.body().string());
                    //Log.e(mTAG, "response.message() : " + response.message()); //응답에 대한 메세지(OK)
                    //Log.e(mTAG, "response.body() : " + responseBody);
                    //Log.e(mTAG, "flag :" + String.valueOf(flag));
                }

                if (responseBody.equals("false")) {
                    Log.d(mTAG, TaskName + " : 서버 연결 실패");
                    return null;
                }

                JSONObject jsonObject = new JSONObject(responseBody);
                JSONArray resultList = jsonObject.getJSONArray("resultList");

                int resultSize = resultList.length();
                if (resultSize == 0) {
                    Log.d(mTAG, TaskName + " : 정보가 없습니다.");
                    return null;
                }

                for(int i = 0; i < resultSize; i++){
                    JSONObject data = resultList.getJSONObject(i);
                    String imgNEWS = data.getString("thumnailPath");
                    String title = data.getString("title");
                    String date = data.getString("mkDate");
                    String content = data.getString("contentSimple");
                    String newsUri = data.getString("detailUrl");
                    NewsData newsData = new NewsData(imgNEWS,title,date,content,newsUri);

                    datas.add(newsData);
                }

            } catch (Exception e) {
                Log.d(mTAG, TaskName + " : " + e.toString());
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