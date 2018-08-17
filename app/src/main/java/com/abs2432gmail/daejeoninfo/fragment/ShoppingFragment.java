package com.abs2432gmail.daejeoninfo.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.abs2432gmail.daejeoninfo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.abs2432gmail.daejeoninfo.Common.NetworkConstant.API_KEY;
import static com.abs2432gmail.daejeoninfo.Common.NetworkConstant.SHOPPING;

public class ShoppingFragment extends Fragment{
    private RecyclerView recyclerView;
    private Context mContext;
    private String TAG = "ShoppingFragment";
    private String Shopping_URL = SHOPPING + API_KEY + "&pageIndex=";
    private int page = 1;
    private int totalPage = 2;
    private LinearLayoutManager linearLayoutManager;
    private ShoppingRecyclerViewAdapter adapter;
    private Button mapBtn;

    public ShoppingFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping, container, false);
        mContext = getActivity();
        linearLayoutManager = new LinearLayoutManager(mContext);

        recyclerView = (RecyclerView) view.findViewById(R.id.shopping_recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);

        String strtext = getArguments().getString("data");
        Log.d(TAG, "onCreateView: " + strtext);

        if (strtext.equals("유성구")) {
            Shopping_URL = SHOPPING + API_KEY + "&searchCondition=ADDRESS&searchKeyword=유성구" + "&dgu=C0604"+"&pageIndex=";
        } else if (strtext.equals("서구")) {
            Shopping_URL = SHOPPING + API_KEY + "&searchCondition=ADDRESS&searchKeyword=서구" + "&dgu=C0603"+"&pageIndex=";
        } else if (strtext.equals("대덕구")) {
            Shopping_URL = SHOPPING + API_KEY + "&searchCondition=ADDRESS&searchKeyword=대덕구" + "&dgu=C0601"+"&pageIndex=";
        } else if (strtext.equals("동구")) {
            Shopping_URL = SHOPPING + API_KEY + "&searchCondition=ADDRESS&searchKeyword=동구"+"&dgu=C0602"+"&pageIndex=";
        } else if (strtext.equals("중구")) {
            Shopping_URL = SHOPPING + API_KEY + "&searchCondition=ADDRESS&searchKeyword=중구" +"&dgu=C0605"+"&pageIndex=";
        }

        adapter = new ShoppingRecyclerViewAdapter(new ArrayList<ShoppingData>());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(onScrollListener);
        new AsyncTaskGetShoppingData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

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
                Toast.makeText(mContext, "로딩중...",Toast.LENGTH_LONG).show();
                page++;
                new AsyncTaskGetShoppingData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    };

    public class ShoppingData {
        public String shopping_name, shopping_address, shopping_tel, shopping_memo;

        public ShoppingData(String shopping_name, String shopping_address, String shopping_tel, String shopping_memo){
            this.shopping_name = shopping_name;
            this.shopping_address = shopping_address;
            this.shopping_tel = shopping_tel;
            this.shopping_memo = shopping_memo;
        }
    }

    private class ShoppingRecyclerViewAdapter extends RecyclerView.Adapter<ShoppingRecyclerViewAdapter.ViewHolder> {
        private ArrayList<ShoppingData> shoppingDatas;

        public ShoppingRecyclerViewAdapter(ArrayList<ShoppingData> shoppingDatas) {
            this.shoppingDatas = shoppingDatas;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ShoppingData shoppingData;
            TextView textView1, textView2, textView3, textView4;

            public ViewHolder(View itemView) {
                super(itemView);
                textView1 = itemView.findViewById(R.id.shopping_name);
                textView2 = itemView.findViewById(R.id.shopping_address);
                textView3 = itemView.findViewById(R.id.shopping_tel);
                textView4 = itemView.findViewById(R.id.shopping_memo);
            }

            public void setListData(ShoppingData data) {
                this.shoppingData = data;
                textView1.setText(shoppingData.shopping_name);
                textView2.setText(shoppingData.shopping_address);
                textView3.setText(shoppingData.shopping_tel);
                textView4.setText(shoppingData.shopping_memo);
            }
        }

        @NonNull
        @Override
        public ShoppingRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_item, parent, false);

            mapBtn = (Button) view.findViewById(R.id.shopping_map);
            mapBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int itemPosition = recyclerView.getChildAdapterPosition(view);
                    ShoppingData data = shoppingDatas.get(itemPosition);
                    String url = "daummaps://open";
                    String geo = "geo:0,0?q=" + data.shopping_address;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.setData(Uri.parse(geo));
                    startActivity(intent);
                }
            });
            ShoppingRecyclerViewAdapter.ViewHolder viewHolder = new ShoppingRecyclerViewAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ShoppingRecyclerViewAdapter.ViewHolder viewHolder, int position) {
            viewHolder.setListData(shoppingDatas.get(position));
        }

        @Override
        public int getItemCount() {
            return shoppingDatas.size();
        }

        public void add(ShoppingData shoppingData) {
            shoppingDatas.add(shoppingData);
            notifyDataSetChanged();
        }
    }

    private class AsyncTaskGetShoppingData extends AsyncTask<Void, Integer, ArrayList<ShoppingData>> {
        private String TaskName = "AsyncTaskGetShoppingData";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<ShoppingData> shoppingDatas) {
            super.onPostExecute(shoppingDatas);
            if (shoppingDatas == null)
                return;
            if (shoppingDatas.size() == 0)
                return;
            for (int i = 0; i < shoppingDatas.size(); i++){
                adapter.add(shoppingDatas.get(i));
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<ShoppingData> doInBackground(Void... voids) {
            Boolean flag = null;
            String responseBody = "false";
            ArrayList<ShoppingData> shoppingDatas = new ArrayList<>();

            try {
                OkHttpClient toServer = new OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .build();

                RequestBody requestBody = new FormBody.Builder().build();

                Request request = new Request.Builder()
                        .url(Shopping_URL + page)
                        .get()
                        .build();

                Response response = toServer.newCall(request).execute();
                flag = response.isSuccessful();

                int responseCode = response.code();

                Log.d(TAG, response.message());

                if (flag) {
                    responseBody = String.valueOf(response.body().string());
                    Log.e(TAG, "response.message() : " + response.message()); //응답에 대한 메세지(OK)
                    Log.e(TAG, "response.body() : " + responseBody);
                    Log.e(TAG, "flag :" + String.valueOf(flag));
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
                    Log.d(TAG, TaskName+" : 정보가 없습니다");
                    return null;
                }

                for (int i = 0; i < resultSize; i++) {
                    JSONObject data = resultList.getJSONObject(i);
                    String shopping_name = data.getString("name");
                    String shopping_address = data.getString("addr1");
                    String shopping_tel = data.getString("telCode") + "-" + data.getString("telKuk")+ "-" + data.getString("telNo");
                    String shopping_memo = data.getString("contents1");

                    ShoppingData shoppingData = new ShoppingData(shopping_name, shopping_address,shopping_tel,shopping_memo);

                    shoppingDatas.add(shoppingData);
                }

            } catch (Exception e){
                return null;
            }
            return shoppingDatas;
        }
    }
}
