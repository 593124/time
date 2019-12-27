package com.exmaple.Keep;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;


public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private Context context;
    private List<Keep> keeps;
    ItemClickListener listener;
    public void setOnItemClickListener(ItemClickListener listener ){
        this.listener = listener;
    }
    public HomeAdapter(Context context, List<Keep> keeps) {
        this.context = context;
        this.keeps = keeps;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_home, parent,false);
        return  new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Keep keep = keeps.get(position);
        holder.mTvName.setText(keep.title);
        String pic = keep.pic;
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(pic));
            holder.ivImg.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null){
                    listener.setOnItemClickListener(position);
                }
            }
        });

        holder.mtvTime.setText(keep.time);
        holder.tv_day.setText(Utils.getDays(keep.time)+"  DAYS");
    }

    @Override
    public int getItemCount() {
        return keeps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView mTvName;
        TextView mtvTime;
        TextView tv_day;
        ImageView ivImg;
        public ViewHolder(View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.title);
            mtvTime = itemView.findViewById(R.id.tvTime);
            ivImg = itemView.findViewById(R.id.ivPic);
            tv_day = itemView.findViewById(R.id.timeDis);
        }
    }




}
