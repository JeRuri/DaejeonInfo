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
import android.widget.Toast;

import com.abs2432gmail.daejeoninfo.R;

import junit.framework.Test;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import static com.abs2432gmail.daejeoninfo.Common.NetworkConstant.API_KEY;
import static com.abs2432gmail.daejeoninfo.Common.NetworkConstant.TEST;

public class TestFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<TestRecyclerViewItemData> list = new ArrayList<>();
    private TestRecyclerViewAdapter adapter;
    private  TestHandler handler;
    private String testUrl = TEST + API_KEY + "&pageNo=";
    private int page = 1;
    private int totalPage = 577;
    private Context mContext;
    private LinearLayoutManager linearLayoutManager;

    public TestFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        mContext = getActivity();
        handler = new TestHandler();
        linearLayoutManager = new LinearLayoutManager(mContext);

        String strtext = getArguments().getString("data");

        if (strtext.equals("공무원")){
            testUrl = TEST + API_KEY + "&categorySeq=86" + "&pageNo=";
        } else if (strtext.equals("임기제")) {
            testUrl = TEST + API_KEY + "&categorySeq=87" + "&pageNo=";
        } else if (strtext.equals("자격증")) {
            testUrl = TEST + API_KEY + "&categorySeq=88" + "&pageNo=";
        } else if (strtext.equals("대전시")) {
            testUrl = TEST + API_KEY + "&categorySeq=89" + "&pageNo=";
        } else if (strtext.equals("타기관")) {
            testUrl = TEST + API_KEY + "&categorySeq=90" + "&pageNo=";
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView4);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new TestRecyclerViewAdapter(new ArrayList<TestRecyclerViewItemData>());
        recyclerView.setAdapter(adapter);

        Thread thread = new Thread() {
            public void run(){
                super.run();
                getXmlData();
                handler.sendEmptyMessage(0);
            }
        };
        thread.start();

        recyclerView.addOnScrollListener(onScrollListener);
        return view;
    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
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
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            getXmlData();
                            handler.sendEmptyMessage(0);
                        }
                    };
                    thread.start();
            }
        }
    };


    private class TestHandler extends Handler {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter = new TestFragment.TestRecyclerViewAdapter(list);
            recyclerView.setAdapter(adapter);
        }
    }

    public class TestRecyclerViewItemData {
        public String testCategory, testTitle, opnStrtDtTm, opnEndDtTm;

        public TestRecyclerViewItemData() {
        }


        public TestRecyclerViewItemData(String testCategory, String testTitle, String opnStrtDtTm, String opnEndDtTm) {
            this.testCategory = testCategory;
            this.testTitle = testTitle;
            this.opnStrtDtTm = opnStrtDtTm;
            this.opnEndDtTm = opnEndDtTm;
        }
    }

    public ArrayList<TestRecyclerViewItemData> getList() {
        ArrayList<TestRecyclerViewItemData> TestRecyclerViewItemDataList = new ArrayList<>();
        return TestRecyclerViewItemDataList;
    }

    private class TestRecyclerViewAdapter extends RecyclerView.Adapter<TestRecyclerViewAdapter.ViewHolder> {
        private ArrayList<TestRecyclerViewItemData> testRecyclerViewItemDatas;

        public TestRecyclerViewAdapter(ArrayList<TestRecyclerViewItemData> data) {
            testRecyclerViewItemDatas = data;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TestRecyclerViewItemData testRecyclerViewItemData;
            TextView textView1, textView2, textView3, textView4;

            public ViewHolder(View itemView) {
                super(itemView);
                textView1 = itemView.findViewById(R.id.testCategory);
                textView2 = itemView.findViewById(R.id.testTitle);
                textView3 = itemView.findViewById(R.id.opnStrtDtTm);
                textView4 = itemView.findViewById(R.id.opnEndDtTm);
            }

            public void setListData(TestRecyclerViewItemData data) {
                this.testRecyclerViewItemData = data;
                textView1.setText(testRecyclerViewItemData.testCategory);
                textView2.setText(testRecyclerViewItemData.testTitle);
                textView3.setText(testRecyclerViewItemData.opnStrtDtTm);
                textView4.setText(testRecyclerViewItemData.opnEndDtTm);
            }
        }

        public TestRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_item, parent, false);
            TestRecyclerViewAdapter.ViewHolder viewHolder = new TestRecyclerViewAdapter.ViewHolder(view);
            return viewHolder;
        }

        public void onBindViewHolder(TestRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.setListData(testRecyclerViewItemDatas.get(position));

        }

        public int getItemCount() {
            return testRecyclerViewItemDatas.size();
        }

        public void add(TestRecyclerViewItemData testRecyclerViewItemData) {
            testRecyclerViewItemDatas.add(testRecyclerViewItemData);
            notifyDataSetChanged();
        }
    }

    String getXmlData() {
        StringBuffer buffer = new StringBuffer();
        TestRecyclerViewItemData testRecyclerViewItemData = null;

        try {
            URL url = new URL(testUrl + page);
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
                            testRecyclerViewItemData = new TestRecyclerViewItemData();
                        } else if (tag.equals("categoryNm")) {
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                            testRecyclerViewItemData.testCategory = xpp.getText().toString();
                        } else if (tag.equals("title")) {
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                            testRecyclerViewItemData.testTitle = xpp.getText().toString();
                        } else if (tag.equals("opnStrtDtTm")) {
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                            testRecyclerViewItemData.opnStrtDtTm = xpp.getText().toString();
                        } else if (tag.equals("opnEndDtTm")) {
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                            testRecyclerViewItemData.opnEndDtTm = xpp.getText().toString();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();
                        if (tag.equals("item")) {
                            list.add(testRecyclerViewItemData);
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