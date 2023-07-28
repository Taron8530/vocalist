package com.example.vocalist;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchWordAdapter extends RecyclerView.Adapter<SearchWordAdapter.ViewHolder> {
    private String TAG = "SearchWordAdapter";
    private ArrayList<WordPojoClass.Item> list;
    public void setList(ArrayList<WordPojoClass.Item> list){
        this.list = list;
    }
    @NonNull
    @Override
    public SearchWordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_word_item, parent, false);
        Log.d(TAG, "onCreateViewHolder: 호출됨");
        return new SearchWordAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchWordAdapter.ViewHolder holder, int position) {
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            word = itemView.findViewById(R.id.word_item);
            detail = itemView.findViewById(R.id.detail_item);
            Log.e(TAG, "ViewHolder: 호출됨");
        }

        public void onbind(WordPojoClass.Item item) {
            Log.e(TAG, "onbind: 호출됨" + item.getWord());
            word.setText(item.getWord());
            detail.setText(item.getSense().getDefinition());
        }
    }
}
