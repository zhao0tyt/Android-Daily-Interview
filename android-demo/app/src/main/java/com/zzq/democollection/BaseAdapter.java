package com.zzq.democollection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.VH>{
    public static final String ITEM_KEY = "item";
    //② 创建ViewHolder
    public static class VH extends RecyclerView.ViewHolder{
        public final TextView title;
        public VH(View v) {
            super(v);
            title = v.findViewById(R.id.tv_txt);
        }
    }

    private List<String> mDatas;
    public BaseAdapter(List<String> data) {
        this.mDatas = data;
    }

    //③ 在Adapter中实现3个方法
    @Override
    public void onBindViewHolder(final VH holder, int position) {
        holder.title.setText(mDatas.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //item 点击事件
                String item = (String) holder.title.getText();
                Log.d("zzq",item);
                Context context = v.getContext();
                Intent intent = new Intent();
                intent.setClass(context,DetailActivity.class);
                intent.putExtra(ITEM_KEY,item);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater.from指定写法
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.base_rv_item, parent, false);
        return new VH(v);
    }
}