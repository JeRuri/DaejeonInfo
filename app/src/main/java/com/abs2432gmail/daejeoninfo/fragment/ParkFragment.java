package com.abs2432gmail.daejeoninfo.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abs2432gmail.daejeoninfo.R;

import java.util.ArrayList;

public class ParkFragment extends Fragment {
    private RecyclerView recyclerView;
    private Context mContext;
    private LinearLayoutManager linearLayoutManager;
    private ParkRecyclerViewAdapter adapter;


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

        } else if (strtext.equals("서구")) {

        } else if (strtext.equals("대덕구")) {

        } else if (strtext.equals("동구")) {

        } else if (strtext.equals("중구")) {

        }

        adapter = new ParkRecyclerViewAdapter(new ArrayList<ParkData>());
        recyclerView.setAdapter(adapter);

        return view;
    }

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
            RelativeLayout relativeLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                relativeLayout = itemView.findViewById(R.id.park_map_view);
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
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull ParkRecyclerViewAdapter.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

    }
}
