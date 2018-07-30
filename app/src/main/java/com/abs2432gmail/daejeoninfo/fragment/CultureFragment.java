package com.abs2432gmail.daejeoninfo.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abs2432gmail.daejeoninfo.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.abs2432gmail.daejeoninfo.Common.NetworkConstant.API_KEY;
import static com.abs2432gmail.daejeoninfo.Common.NetworkConstant.CULTURE;

public class CultureFragment extends Fragment {
    private RecyclerView recyclerView;
    private CulRecyclerViewAdapter adapter;
    private ArrayList<CulRecyclerViewItemData> list = new ArrayList<>();
    private Context mContext = getContext();
    private String mTAG = "CultureFragment";

    public CultureFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_culture, container, false);
        String strtext = getArguments().getString("date");
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new CulRecyclerViewAdapter(new ArrayList<CulRecyclerViewItemData>());
        recyclerView.setAdapter(adapter);
        /**데이터 가져오기*/
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                getXmlData();
                handler.sendEmptyMessage(0);
            }
        };
        thread.start();
        return view;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter = new CulRecyclerViewAdapter(list);
            recyclerView.setAdapter(adapter);
        }
    };

    public class CulRecyclerViewItemData {
        public int drawable;
        public String text1, text2, text3;

        public CulRecyclerViewItemData() {
        }

        public CulRecyclerViewItemData(int drawable, String text1, String text2, String text3) {
            this.drawable = drawable;
            this.text1 = text1;
            this.text2 = text2;
            this.text3 = text3;
        }
    }

    public ArrayList<CulRecyclerViewItemData> MakeItemData() {
        ArrayList<CulRecyclerViewItemData> CulRecyclerViewItemDataArrayList = new ArrayList<>();
        return CulRecyclerViewItemDataArrayList;
    }

    private class CulRecyclerViewAdapter extends RecyclerView.Adapter<CulRecyclerViewAdapter.ViewHolder> {
        private ArrayList<CulRecyclerViewItemData> culRecyclerViewItemDatas;

        public CulRecyclerViewAdapter(ArrayList<CulRecyclerViewItemData> data) {
            culRecyclerViewItemDatas = data;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            CulRecyclerViewItemData culRecyclerViewItemData;
            CircleImageView circleImageView;
            TextView textView1, textView2, textView3;

            public ViewHolder(View itemView) {
                super(itemView);
                circleImageView = itemView.findViewById(R.id.img1);
                textView1 = itemView.findViewById(R.id.culTitle);
                textView2 = itemView.findViewById(R.id.culDate);
                textView3 = itemView.findViewById(R.id.culLoc);
            }

            public void setListData(CulRecyclerViewItemData data) {
                this.culRecyclerViewItemData = data;
                circleImageView.setImageResource(R.drawable.festival);
                textView1.setText(culRecyclerViewItemData.text1);
                textView2.setText(culRecyclerViewItemData.text2);
                textView3.setText(culRecyclerViewItemData.text3);
            }
        }

        public CulRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.culture_item, parent, false);
            CulRecyclerViewAdapter.ViewHolder viewHolder = new CulRecyclerViewAdapter.ViewHolder(view);
            return viewHolder;
        }

        public void onBindViewHolder(CulRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.setListData(culRecyclerViewItemDatas.get(position));
        }

        public int getItemCount() {
            return culRecyclerViewItemDatas.size();
        }

        public void add(CulRecyclerViewItemData culRecycleriewItemData) {
            culRecyclerViewItemDatas.add(culRecycleriewItemData);
            notifyDataSetChanged();
        }

    }

    /**
     * 데이터 가져오는 메소드
     */
    String getXmlData() {
        StringBuffer buffer = new StringBuffer();

        String queryUrl = CULTURE + API_KEY;
        CulRecyclerViewItemData culRecyclerViewItemData = null;
        try {
            URL url = new URL(queryUrl);
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
                            culRecyclerViewItemData = new CulRecyclerViewItemData();
                        } else if (tag.equals("auspiceAgency")) {
                            buffer.append("주최: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        } else if (tag.equals("title")) {
                            buffer.append("제목: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                            culRecyclerViewItemData.text1 = xpp.getText().toString();
                        } else if (tag.equals("content")) {
                            buffer.append("내용: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        } else if (tag.equals("homepageUrl")) {
                            buffer.append("홈페이지: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        } else if (tag.equals("opnStrtDt")) {
                            buffer.append("시작하는날: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("");
                            culRecyclerViewItemData.text2 = xpp.getText().toString() + culRecyclerViewItemData.text2;
                        } else if (tag.equals("opnEndDt")) {
                            buffer.append("끝나는날: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                            culRecyclerViewItemData.text2 = "~" + xpp.getText().toString();
                        } else if (tag.equals("openPlace")) {
                            buffer.append("장소: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            culRecyclerViewItemData.text3 = xpp.getText().toString();
                        } else if (tag.equals("tel")) {
                            buffer.append("전화번호: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();
                        if (tag.equals("item")) {
                            list.add(culRecyclerViewItemData);
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
}
