package com.abs2432gmail.daejeoninfo.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abs2432gmail.daejeoninfo.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

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
    private ParkHandler parkHandler;

    public ParkFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_park, container, false);
        mContext = getActivity();
        parkHandler = new ParkHandler();
        linearLayoutManager = new LinearLayoutManager(mContext);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_park);
        recyclerView.setLayoutManager(linearLayoutManager);

        String strtext = getArguments().getString("data");

        if (strtext.equals("유성구")) {
            parkURL = PARK + API_KEY + "&searchCondition=ADDRESS&searchKeyword=유성구" + "&pageNo=" + page;
        } else if (strtext.equals("서구")) {
            parkURL = PARK + API_KEY + "&searchCondition=ADDRESS&searchKeyword=서구" + "&pageNo="+ page;
        } else if (strtext.equals("대덕구")) {
            parkURL = PARK + API_KEY + "&searchCondition=ADDRESS&searchKeyword=대덕구" + "&pageNo="+ page;
        } else if (strtext.equals("동구")) {
            parkURL = PARK + API_KEY + "&searchCondition=ADDRESS&searchKeyword=동구" + "&pageNo="+ page;
        } else if (strtext.equals("중구")) {
            parkURL = PARK + API_KEY + "&searchCondition=ADDRESS&searchKeyword=중구" + "&pageNo="+ page;
        }

        adapter = new ParkRecyclerViewAdapter(new ArrayList<ParkData>());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(onScrollListener);

        new AsyncTaskGetParkData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return view;
    }

    private class ParkHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter.notifyDataSetChanged();
        }
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

        private ParkData(){}

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
                linearLayout = itemView.findViewById(R.id.map_layout);
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

    String getXmlData() {
        StringBuffer buffer = new StringBuffer();
        ParkData data = null;

        try {
            URL url = new URL(parkURL + page);
            InputStream is = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));

            String tag;

            xpp.next();

            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();

                        if (tag.equals("item")) {
                            data = new ParkData();
                        } else if (tag.equals("title")) {
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                            data.title = xpp.getText().toString();
                        } else if (tag.equals("address")) {
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                            data.address = xpp.getText().toString();
                        } else if (tag.equals("latitude")) {
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                            data.latitude = xpp.getText().toString();
                        } else if (tag.equals("longitude")) {
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                            data.longitude = xpp.getText().toString();
                        } else if (tag.equals("tel")) {
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                            data.tel = xpp.getText().toString();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();
                        if (tag.equals("item")) {
                            adapter.add(data);
                        }
                        break;
                }
                eventType = xpp.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    private class AsyncTaskGetParkData extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            ParkHandler parkHandler = new ParkHandler();
            parkHandler.sendEmptyMessage(0);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    getXmlData();
                    parkHandler.sendEmptyMessage(0);
                }
            };
            thread.start();
            return null;
        }
    }
}