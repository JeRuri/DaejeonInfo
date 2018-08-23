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
import android.widget.TextView;
import android.widget.Toast;

import com.abs2432gmail.daejeoninfo.OkHttp.OkHttpAPICall;
import com.abs2432gmail.daejeoninfo.OkHttp.OkHttpInitSingletonManager;
import com.abs2432gmail.daejeoninfo.R;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Response;

import static com.abs2432gmail.daejeoninfo.Common.NetworkConstant.API_KEY;
import static com.abs2432gmail.daejeoninfo.Common.NetworkConstant.CORRECT;

public class CorrectFragment extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Context mContext;
    private String correctURL = CORRECT + API_KEY + "&pageNo=";
    private int page = 1;
    private int totalPage = 2;
    private CorrectRecyclerViewAdapter adapter;

    public CorrectFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_correct, container, false);
        mContext = getActivity();
        linearLayoutManager = new LinearLayoutManager(mContext);

        recyclerView = (RecyclerView) view.findViewById(R.id.correct_recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new CorrectRecyclerViewAdapter(new ArrayList<CorrectData>());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(onScrollListener);
        new AsyncTaskGetCorrectData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return view;
    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastVisibleItemPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;
            if (lastVisibleItemPosition == itemTotalCount){
                if (page == totalPage){
                    Toast.makeText(mContext, "마지막입니다...", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(mContext, "로딩중...", Toast.LENGTH_SHORT).show();
                page++;
                new AsyncTaskGetCorrectData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    };
    public class CorrectData{
        String deptNm, title, regDtTm, tel, atchFileCnt;

        public CorrectData(String deptNm, String title, String regDtTm, String tel, String atchFileCnt){
            this.deptNm = deptNm;
            this.title = title;
            this.regDtTm = regDtTm;
            this.tel = tel;
            this.atchFileCnt = atchFileCnt;
        }
    }

    private class CorrectRecyclerViewAdapter extends RecyclerView.Adapter<CorrectRecyclerViewAdapter.ViewHolder> {
        private ArrayList<CorrectData> datas;

        public CorrectRecyclerViewAdapter(ArrayList<CorrectData> datas){
            this.datas = datas;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            CorrectData recyclerData;
            TextView textView1, textView2, textView3, textView4, textView5;

            public ViewHolder(View itemView){
                super(itemView);
                textView1 = itemView.findViewById(R.id.correct_dept);
                textView2 = itemView.findViewById(R.id.correct_title);
                textView3 = itemView.findViewById(R.id.correct_Date);
                textView4 = itemView.findViewById(R.id.correct_Tel);
                textView5 = itemView.findViewById(R.id.correct_atch);
            }

            public void setListData(CorrectData data){
                this.recyclerData = data;
                textView1.setText("["+recyclerData.deptNm+"]");
                textView2.setText(recyclerData.title);
                textView3.setText("공고일 : "+recyclerData.regDtTm);
                textView4.setText("Tel : "+recyclerData.tel);
                textView5.setText("첨부파일 "+recyclerData.atchFileCnt+"개");
            }
        }

        @NonNull
        @Override
        public CorrectRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.correct_item,parent,false);
            CorrectRecyclerViewAdapter.ViewHolder  viewHolder = new CorrectRecyclerViewAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CorrectRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.setListData(datas.get(position));
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        public void add(CorrectData recyclerData){
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
            List<CorrectData> items;
            int numberOfRows;
            int pageNo;
            int totalCount;
        }
    }

    private class AsyncTaskGetCorrectData extends AsyncTask<Void, Integer, Void>{
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
                response = OkHttpAPICall.GET(client, correctURL + page);
                MyResponse myResponse = new GsonXmlBuilder()
                        .setXmlParserCreator(xmlParserCreator)
                        .setPrimitiveArrays(true)
                        .setSkipRoot(true)
                        .create()
                        .fromXml(response.body().string(), MyResponse.class);

                int itemSize = myResponse.body.items.size();
                totalPage = myResponse.body.totalCount/10 + 1;
                for (int i = 0; i < itemSize; i++){
                    adapter.add(myResponse.body.items.get(i));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
