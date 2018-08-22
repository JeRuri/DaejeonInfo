package com.abs2432gmail.daejeoninfo.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.abs2432gmail.daejeoninfo.OkHttp.OkHttpAPICall;
import com.abs2432gmail.daejeoninfo.OkHttp.OkHttpInitSingletonManager;
import com.abs2432gmail.daejeoninfo.R;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Response;

import static com.abs2432gmail.daejeoninfo.Common.NetworkConstant.API_KEY;
import static com.abs2432gmail.daejeoninfo.Common.NetworkConstant.HOTEL;

public class HotelFragment extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Context mContext;
    private String HotelURL = HOTEL + API_KEY + "&pageNo=";
    private int page = 1;
    private int totalPage = 2;
    private HotelRecyclerViewAdapter adapter;

    public HotelFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hotel, container, false);

        mContext = getActivity();
        linearLayoutManager = new LinearLayoutManager(mContext);

        recyclerView = (RecyclerView) view.findViewById(R.id.hotel_recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);

        String strtext = getArguments().getString("data");
        if (strtext.equals("유성구")) {
            HotelURL = HOTEL + API_KEY + "&dgu=C0604" + "&pageNo=";
        } else if (strtext.equals("서구")) {
            HotelURL = HOTEL + API_KEY + "&dgu=C0603" + "&pageNo=";
        } else if (strtext.equals("대덕구")) {
            HotelURL = HOTEL + API_KEY + "&dgu=C0601" + "&pageNo=";
        } else if (strtext.equals("중구")) {
            HotelURL = HOTEL + API_KEY + "&dgu=C0605" + "&pageNo=";
        } else if (strtext.equals("동구")) {
            HotelURL = HOTEL + API_KEY + "&dgu=C0602" + "&pageNo=";
        }

        adapter = new HotelRecyclerViewAdapter(new ArrayList<HotelData>());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(onScrollListener);

        new AsyncTaskGetHotelData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
                new AsyncTaskGetHotelData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    };

    public class HotelData {
        String dcodeNm, name, telCode, telKuk, telNo;

        public HotelData(String dcodeNm, String name, String telCode, String telKuk, String telNo) {
            this.dcodeNm = dcodeNm;
            this.name = name;
            this.telCode = telCode;
            this.telKuk = telKuk;
            this.telNo = telNo;
        }
    }

    private class HotelRecyclerViewAdapter extends RecyclerView.Adapter<HotelRecyclerViewAdapter.ViewHolder> {
        private ArrayList<HotelData> datas;

        public HotelRecyclerViewAdapter(ArrayList<HotelData> datas) {
            this.datas = datas;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            HotelData recyclerData;
            TextView textView1, textView2, textView3, textView4, textView5;

            public ViewHolder(View itemView) {
                super(itemView);
                textView1 = itemView.findViewById(R.id.hotel_category);
                textView2 = itemView.findViewById(R.id.hotel_name);
                textView3 = itemView.findViewById(R.id.hotel_telCode);
                textView4 = itemView.findViewById(R.id.hotel_telKuk);
                textView5 = itemView.findViewById(R.id.hotel_telNo);
            }

            public void setListData(HotelData data) {
                this.recyclerData = data;
                textView1.setText("[" + recyclerData.dcodeNm + "]");
                textView2.setText(recyclerData.name);
                textView3.setText("Tel : " + recyclerData.telCode);
                textView4.setText("-" + recyclerData.telKuk);
                textView5.setText("-" + recyclerData.telNo);
            }
        }

        @NonNull
        @Override
        public HotelRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_item, parent, false);
            HotelRecyclerViewAdapter.ViewHolder viewHolder = new HotelRecyclerViewAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull HotelRecyclerViewAdapter.ViewHolder viewHolder, int position) {
            viewHolder.setListData(datas.get(position));
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        public void add(HotelData recyclerData) {
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
            List<HotelData> items;
            int numOfRows;
            int pageNo;
            int totalCount;
        }
    }

    private class AsyncTaskGetHotelData extends AsyncTask<Void, Integer, Void> {
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
                response = OkHttpAPICall.GET(client, HotelURL + page);
                MyResponse myResponse = new GsonXmlBuilder()
                        .setXmlParserCreator(xmlParserCreator)
                        .setPrimitiveArrays(true)
                        .setSkipRoot(true)
                        .create()
                        .fromXml(response.body().string(),MyResponse.class);

                int itemSize = myResponse.body.items.size();
                totalPage = myResponse.body.totalCount/10 + 1;

                for (int i = 0; i < itemSize; i++) {
                    adapter.add(myResponse.body.items.get(i));
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}


