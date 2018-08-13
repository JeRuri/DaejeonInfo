package com.abs2432gmail.daejeoninfo.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abs2432gmail.daejeoninfo.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.abs2432gmail.daejeoninfo.Common.NetworkConstant.API_KEY;
import static com.abs2432gmail.daejeoninfo.Common.NetworkConstant.PARK;

public class ParkFragment extends Fragment {
    private RecyclerView recyclerView;
    private Context mContext;
    private String TAG = "ParkFragment";
    private LinearLayoutManager linearLayoutManager;
    private ParkRecyclerViewAdapter adapter;
    String parkURL = PARK + API_KEY + "&pageNo=";
    int page = 1;
    int totalPage = 20;

    public ParkFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_park, container, false);
        mContext = getActivity();
        linearLayoutManager = new LinearLayoutManager(mContext);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_park);
        recyclerView.setLayoutManager(linearLayoutManager);

        String strtext = getArguments().getString("data");

        if (strtext.equals("유성구")) {
            parkURL = PARK + API_KEY + "&searchCondition=ADDRESS&searchKeyword=유성구" + "&pageNo=";
        } else if (strtext.equals("서구")) {
            parkURL = PARK + API_KEY + "&searchCondition=ADDRESS&searchKeyword=서구" + "&pageNo=";
        } else if (strtext.equals("대덕구")) {
            parkURL = PARK + API_KEY + "&searchCondition=ADDRESS&searchKeyword=대덕구" + "&pageNo=";
        } else if (strtext.equals("동구")) {
            parkURL = PARK + API_KEY + "&searchCondition=ADDRESS&searchKeyword=동구" + "&pageNo=";
        } else if (strtext.equals("중구")) {
            parkURL = PARK + API_KEY + "&searchCondition=ADDRESS&searchKeyword=중구" + "&pageNo=";
        }

        adapter = new ParkRecyclerViewAdapter(new ArrayList<ParkData>());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(onScrollListener);

        new AsyncTaskGetParkData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

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
                new AsyncTaskGetParkData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    };

    public class ParkData {
        String title, address, latitude, longitude, section, tel;

        public ParkData(String title, String address, String latitude, String longitude, String section, String tel) {
            this.title = title;
            this.address = address;
            this.latitude = latitude;
            this.longitude = longitude;
            this.section = section;
            this.tel = tel;
        }
    }

    private class ParkRecyclerViewAdapter extends RecyclerView.Adapter<ParkRecyclerViewAdapter.ViewHolder> {
        private ArrayList<ParkData> datas;

        public ParkRecyclerViewAdapter(ArrayList<ParkData> datas){
            this.datas = datas;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            ParkData recyclerData;
            TextView textView1, textView2, textView3, textView4;
            LinearLayout linearLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                linearLayout = itemView.findViewById(R.id.park_map_view);
                textView1 = itemView.findViewById(R.id.parkTitle);
                textView2 = itemView.findViewById(R.id.parkSection);
                textView3 = itemView.findViewById(R.id.parkAdr);
                textView4 = itemView.findViewById(R.id.parkTel);
            }

            public void setListData(ParkData data){
                this.recyclerData = data;
                textView1.setText(recyclerData.title);
                textView2.setText(recyclerData.section);
                textView3.setText(recyclerData.address);
                textView4.setText(recyclerData.tel);
            }
        }
        @NonNull
        @Override
        public ParkRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.park_item, parent, false);
            ParkRecyclerViewAdapter.ViewHolder viewHolder = new ParkRecyclerViewAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ParkRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.setListData(datas.get(position));
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        public void add(ParkData recyclerData){
            datas.add(recyclerData);
        }
    }

    private class AsyncTaskGetParkData extends AsyncTask<Void, Integer, ArrayList<ParkData>> {
        private String TaskName = "AsyncTaskGetParkData";

        //시작하기 전
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //갱신할 때
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        //완료된 후
        @Override
        protected void onPostExecute(ArrayList<ParkData> parkDatas) {
            super.onPostExecute(parkDatas);
            if (parkDatas == null)
                return;
            if (parkDatas.size() == 0)
                return;
            for (int i = 0; i < parkDatas.size(); i++){
                adapter.add(parkDatas.get(i));
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        protected ArrayList<ParkData> doInBackground(Void... voids) {
            Boolean flag = null;
            String responseBody = "false";
            ArrayList<ParkData> datas = new ArrayList<>();
            try {
                OkHttpClient toServer = new OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .build();
                RequestBody requestBody = new FormBody.Builder()
                        .build();

                Request request = new Request.Builder()
                        .url(parkURL + page)
                        .get()
                        .build();

                Response response = toServer.newCall(request).execute();
                flag = response.isSuccessful();

                int responseCode = response.code();
                //200 연결잘됨

                Log.d(TAG, "response.code() : " + String.valueOf(responseCode));
                if (flag) {
                    responseBody = String.valueOf(response.body().string());
                    Log.e(TAG, "response.message() : " + response.message()); //응답에 대한 메세지(OK)
                    Log.e(TAG, "response.body() : " + responseBody);
                    Log.e(TAG, "flag :" + String.valueOf(flag));
                }

                if (responseBody.equals("false")) {
                    Log.d(TAG, TaskName + ": 서버 연결 실패");
                    return null;
                }

                JSONObject jsonObject = new JSONObject(responseBody);
                JSONObject pageInfo = jsonObject.getJSONObject("Item");
                totalPage = pageInfo.getInt("totalCount");

                JSONArray resultList = jsonObject.getJSONArray("item");

                int resultSize = resultList.length();
                if (resultSize == 0) {
                    return null;
                }

                for (int i = 0; i < resultSize; i++){
                    JSONObject data = resultList.getJSONObject(i);
                    String title = data.getString("title");
                    String address = data.getString("address");
                    String latitude = data.getString("latitude");
                    String longitude = data.getString("longitude");
                    String section = data.getString("section");
                    String tel = data.getString("tel");

                    ParkData parkData = new ParkData(title,address,latitude,longitude,section,tel);

                    datas.add(parkData);
                }

            } catch (Exception e){
                Log.d(TAG, TaskName + " :  "+ e.toString());
                return null;
            }
            return datas;
        }
    }
}
