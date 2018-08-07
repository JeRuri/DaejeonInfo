package com.abs2432gmail.daejeoninfo.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abs2432gmail.daejeoninfo.R;
import com.bumptech.glide.Glide;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.TransitionManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import static com.abs2432gmail.daejeoninfo.Common.NetworkConstant.API_KEY;
import static com.abs2432gmail.daejeoninfo.Common.NetworkConstant.PET;

public class PetFragment extends Fragment {
    private RecyclerView recyclerView;
    private PetRecyclerViewAdapter adapter;
    private ArrayList<PetRecyclerViewItemData> list = new ArrayList<>();
    private Context mContext;
    private String TAG = "PetFragment";
    String pageUrl = PET + API_KEY + "&pageNo=";
    int page = 1;
    int totalPage = 2;
    private MyHandler handler;
    public PetFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet, container, false);
        mContext = getActivity();
        handler = new MyHandler();
        String strtext = getArguments().getString("data3");
        Log.d(TAG, "onCreateView: " + strtext);

        if (strtext.equals("강아지")){
            pageUrl = PET + API_KEY + "&searchCondition=1"+ "&pageNo=";
        } else if (strtext.equals("고양이")){
            pageUrl = PET + API_KEY + "&searchCondition=2"+ "&pageNo=";

        }
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView3);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new PetRecyclerViewAdapter(new ArrayList<PetRecyclerViewItemData>());
        recyclerView.setAdapter(adapter);

        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                getXmlData();
                handler.sendEmptyMessage(0);
            }
        };
        thread.start();

        recyclerView.addOnScrollListener(onScrollListener);

        return view;
    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;
            if (lastVisibleItemPosition == itemTotalCount) {
                if(page== totalPage){
                    Toast.makeText(mContext,"마지막입니다...",Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(mContext,"찾아주세요...",Toast.LENGTH_SHORT).show();
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

    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter.notifyDataSetChanged();
        }
    }

    public class PetRecyclerViewItemData {
        public String imgPET, adoptionStatus,classification, gender, date, age, location, memo;

        public PetRecyclerViewItemData(){}
        //여기에 데이터 들어감
        public PetRecyclerViewItemData(String imgPET, String adoptionStatus, String classification, String gender,
                                       String date, String age, String location, String memo){
            this.imgPET = imgPET;
            this.adoptionStatus = adoptionStatus;
            this.classification = classification;
            this.gender = gender;
            this.date = date;
            this.age = age;
            this.location = location;
            this.memo = memo;
        }
    }

    public ArrayList<PetRecyclerViewItemData> getList() {
        ArrayList<PetRecyclerViewItemData> PetRecyclerViewItemDataList = new ArrayList<>();
        return PetRecyclerViewItemDataList;
    }

    private class PetRecyclerViewAdapter extends RecyclerView.Adapter<PetRecyclerViewAdapter.ViewHolder>{
        private ArrayList<PetRecyclerViewItemData> petRecyclerViewItemDatas;

        public PetRecyclerViewAdapter (ArrayList<PetRecyclerViewItemData> data){
            petRecyclerViewItemDatas = data;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            PetRecyclerViewItemData petRecyclerViewItemData;
            ImageView imageView, expandBtn;
            TextView textView1, textView2, textView3, textView4,
                        textView5, textView6, textView7;
            LinearLayout lin_expandView;

            public ViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imgPET);
                textView1 = itemView.findViewById(R.id.adoptionStatus);
                textView2 = itemView.findViewById(R.id.classification);
                textView3 = itemView.findViewById(R.id.gender);
                textView4 = itemView.findViewById(R.id.age);
                textView5 = itemView.findViewById(R.id.date);
                textView6 = itemView.findViewById(R.id.location);
                textView7 = itemView.findViewById(R.id.memo);
                lin_expandView = itemView.findViewById(R.id.lin_expandview);
                expandBtn = itemView.findViewById(R.id.expandBtn);
            }

            public void setListData (PetRecyclerViewItemData data) {
                this.petRecyclerViewItemData = data;
                //데이터 사용은 여기서 하는거야
                //여기서 사용하는거야<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                //글라이드/context/load 파일경로/into 이미지뷰
                Glide.with(mContext).load(petRecyclerViewItemData.imgPET).into(imageView);
                textView1.setText(petRecyclerViewItemData.adoptionStatus);
                textView2.setText(petRecyclerViewItemData.classification);
                textView3.setText(petRecyclerViewItemData.gender);
                textView4.setText(petRecyclerViewItemData.age);
                textView5.setText(petRecyclerViewItemData.date);
                textView6.setText(petRecyclerViewItemData.location);
                textView7.setText(petRecyclerViewItemData.memo);
            }
        }

        public PetRecyclerViewAdapter.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_item, parent, false);
            PetRecyclerViewAdapter.ViewHolder viewHolder = new PetRecyclerViewAdapter.ViewHolder(view);
            return viewHolder;
        }

        public void onBindViewHolder (final PetRecyclerViewAdapter.ViewHolder holder, int position){
            holder.setListData(petRecyclerViewItemDatas.get(position));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean shouldExpand = holder.lin_expandView.getVisibility() == View.GONE;

                    ChangeBounds transition = new ChangeBounds();
                    transition.setDuration(125);

                    if(shouldExpand){
                        holder.lin_expandView.setVisibility(View.VISIBLE);
                        holder.expandBtn.setRotationX(180);
                        holder.expandBtn.setPivotX(50);
                        holder.expandBtn.setPivotY(50);
                    } else {
                        holder.lin_expandView.setVisibility(View.GONE);
                        holder.expandBtn.setRotationX(360);
                        holder.expandBtn.setPivotX(50);
                        holder.expandBtn.setPivotY(50);
                    }

                    TransitionManager.beginDelayedTransition(recyclerView, transition);
                    holder.itemView.setActivated(shouldExpand);
                }
            });
        }

        public int getItemCount() { return petRecyclerViewItemDatas.size();}
        public void add (PetRecyclerViewItemData petRecyclerViewItemData){
            petRecyclerViewItemDatas.add(petRecyclerViewItemData);
        }
    }

    String getXmlData () {
        StringBuffer buffer = new StringBuffer();

        PetRecyclerViewItemData petRecyclerViewItemData = null;

        try {
            URL url = new URL(pageUrl + page);
            InputStream is = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));
            String tag;
            xpp.next();

            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();
                        if (tag.equals("item")) {
                            petRecyclerViewItemData = new PetRecyclerViewItemData();
                        }

                        else if (tag.equals("filePath")){
                            xpp.next();
                            buffer.append(xpp.getText());
                            petRecyclerViewItemData.imgPET = "http://www.daejeon.go.kr/"+xpp.getText();
                        }

                        else if(tag.equals("adoptionStatusCd")) {
                            //1:공고중,2:입양가능,3:입양예정,4:입양완료,7:주인반환
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                            String strAdoptionStatusCd = xpp.getText();
                            int adoptionStatusCd = Integer.parseInt(strAdoptionStatusCd);
                            switch (adoptionStatusCd){
                                case 1:
                                    petRecyclerViewItemData.adoptionStatus = "공고중";
                                    break;
                                case 2:
                                    petRecyclerViewItemData.adoptionStatus = "입양가능";
                                    break;
                                case 3:
                                    petRecyclerViewItemData.adoptionStatus = "입양예정";
                                    break;
                                case 4:
                                    petRecyclerViewItemData.adoptionStatus = "입양완료";
                                    break;
                                case 5:
                                    petRecyclerViewItemData.adoptionStatus = "자연사";
                                    break;
                                case 6:
                                    petRecyclerViewItemData.adoptionStatus = "안락사";
                                    break;
                                case 7:
                                    petRecyclerViewItemData.adoptionStatus = "주인반환";
                                    break;
                                default:
                                    break;
                            }
                        }

                        else if(tag.equals("classification")) {
                            //1:강아지 , 2:고양이, 3:기타동물
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                            String strClassification = xpp.getText();
                            int classification = Integer.parseInt(strClassification);
                            switch (classification){
                                case 1:
                                    petRecyclerViewItemData.classification = "강아지";
                                    break;
                                case 2:
                                    petRecyclerViewItemData.classification = "고양이";
                                    break;
                                case 3:
                                    petRecyclerViewItemData.classification = "기타동물";
                                    break;
                            }
                        }

                        else if(tag.equals("gender")) {
                            //1:암, 2:수
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                            String strGender = xpp.getText();
                            int gender = Integer.parseInt(strGender);
                            switch (gender){
                                case 1:
                                    petRecyclerViewItemData.gender = "암컷";
                                    break;
                                case 2:
                                    petRecyclerViewItemData.gender = "수컷";
                                    break;
                            }
                        }

                        else if(tag.equals("rescueDate")) {
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                            petRecyclerViewItemData.date = "구조날짜 : "+xpp.getText().toString();
                        }

                        else if(tag.equals("age")) {
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                            petRecyclerViewItemData.age = xpp.getText().toString();
                        }

                        else if(tag.equals("foundPlace")) {
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                            petRecyclerViewItemData.location = "발견장소 : "+ xpp.getText().toString();
                        }

                        else if(tag.equals("memo")) {
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                            petRecyclerViewItemData.memo = xpp.getText().toString();
                        }else if(tag.equals("totalCount")){
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                            totalPage = Integer.parseInt(xpp.getText().toString())/10 + 1;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();
                        if(tag.equals("item")) {
                            adapter.add(petRecyclerViewItemData);
                        }
                        break;
                }
                eventType = xpp.next();
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
