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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abs2432gmail.daejeoninfo.OkHttp.OkHttpAPICall;
import com.abs2432gmail.daejeoninfo.OkHttp.OkHttpInitSingletonManager;
import com.abs2432gmail.daejeoninfo.R;
import com.stanfy.gsonxml.GsonXml;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;

import net.daum.android.map.MapViewEventListener;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
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

    public ParkFragment() {
    }

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
                if (page == totalPage) {
                    Toast.makeText(mContext, "마지막입니다...", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(mContext, "로딩중...", Toast.LENGTH_SHORT).show();
                page++;

                new AsyncTaskGetParkData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    };

    public class ParkData {
        String title, address, section, tel;
        double latitude,longitude;

        ParkData (String title, String address, String section, String tel, double latitude, double longitude){
            this.title = title;
            this.address = address;
            this.section = section;
            this.tel = tel;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    private class ParkRecyclerViewAdapter extends RecyclerView.Adapter<ParkRecyclerViewAdapter.ViewHolder> {
        private ArrayList<ParkData> datas;

        public ParkRecyclerViewAdapter(ArrayList<ParkData> datas) {
            this.datas = datas;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ParkData recyclerData;
            TextView textView1, textView2, textView3, textView4;

            public ViewHolder(View itemView) {
                super(itemView);
                textView1 = itemView.findViewById(R.id.parkTitle);
                textView2 = itemView.findViewById(R.id.parkSection);
                textView3 = itemView.findViewById(R.id.parkAdr);
                textView4 = itemView.findViewById(R.id.parkTel);
            }

            public void setListData(ParkData data) {
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
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.park_item, parent, false);
            Button park_mark_button = (Button) view.findViewById(R.id.park_mark_button);
            park_mark_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int itemPosition = recyclerView.getChildAdapterPosition(view);
                    ParkData data = datas.get(itemPosition);
                    String url = "daummaps://open";
                    String geo = "geo:"+data.latitude+","+data.longitude;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.setData(Uri.parse(geo));
                    startActivity(intent);
                }

            });

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

        public void add(ParkData recyclerData) {
            datas.add(recyclerData);
        }
    }

    XmlParserCreator xmlParserCreator = new XmlParserCreator() {
        @Override
        public XmlPullParser createParser() {
            try {
                return XmlPullParserFactory.newInstance().newPullParser();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    };

    private class MyResponse{
        Body body;
        private class Body{
            List<ParkData> items;
            int numOfRows;
            int pageNo;
            int totalCount;
        }
    }
    private class AsyncTaskGetParkData extends AsyncTask<Void, Integer, Void> {
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

            try {
                //통신
                response = OkHttpAPICall.GET(client, parkURL + page);
                //xml 파싱
                MyResponse myResponse = new GsonXmlBuilder()
                        .setXmlParserCreator(xmlParserCreator)
                        .setPrimitiveArrays(true)
                        .setSkipRoot(true)
                        .create()
                        .fromXml(response.body().string(),MyResponse.class);

                //데이터 확인 및 입력
                int itemSize = myResponse.body.items.size();
                totalPage = myResponse.body.totalCount/10 + 1;

                for(int i = 0 ; i < itemSize; i++){
                    adapter.add(myResponse.body.items.get(i));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}