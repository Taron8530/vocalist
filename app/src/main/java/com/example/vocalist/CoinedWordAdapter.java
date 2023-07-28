package com.example.vocalist;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CoinedWordAdapter extends RecyclerView.Adapter<CoinedWordAdapter.ViewHolder> {
    private String TAG = "SearchWordAdapter";
    private ArrayList<CoinedWordPojoClass.Data> list;
    public void setList(ArrayList<CoinedWordPojoClass.Data> list){
        this.list = list;
    }
    @NonNull
    @Override
    public CoinedWordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coinedword_item, parent, false);
        Log.d(TAG, "onCreateViewHolder: 호출됨");
        return new CoinedWordAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinedWordAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: 호출됨");
        holder.onbind(list.get(position));
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+list.size());
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView word;
        private TextView detail;
        private TextView writer;
        private TextView dataTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            word = itemView.findViewById(R.id.word_item);
            detail = itemView.findViewById(R.id.detail_item);
            writer = itemView.findViewById(R.id.writer_item);
            dataTime = itemView.findViewById(R.id.dataTime);
            Log.e(TAG, "ViewHolder: 호출됨");
        }

        public void onbind(CoinedWordPojoClass.Data item) {
            Log.e(TAG, "onbind: 호출됨" + item.getWord());
            word.setText(item.getWord());
            detail.setText(item.getDetail());
            writer.setText(item.getWriter());
            dataTime.setText("등록일 : "+item.getDatatime());
        }
    }
}
