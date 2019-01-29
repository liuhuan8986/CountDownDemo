package com.liuhuan.countdownviewdemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liuhuan.widget.CountDownView;

import java.util.ArrayList;
import java.util.List;

public class CountDownAdapter extends RecyclerView.Adapter<CountDownAdapter.MyVH> {
    private Context mContext;
    private List<CountDownVO> dataList;

    public CountDownAdapter(Context mContext) {
        this.mContext = mContext;
        dataList = new ArrayList<>();
    }


    public List<CountDownVO> getDataList() {
        return dataList;
    }

    public void setDataList(List<CountDownVO> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view  = LayoutInflater.from(mContext).inflate(R.layout.item,null);
        MyVH viewHolder = new MyVH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyVH viewHolder, int i) {
        CountDownVO vo = getDataList().get(i);
        viewHolder.countDownView.setDescription("距离任务结束还有%s");
        viewHolder.countDownView.setTimeColor(ContextCompat.getColor(mContext,android.R.color.holo_red_light));
        viewHolder.countDownView.setEndTime(vo.getEndTime());
        viewHolder.countDownView.setCountDownLinstener(new MyCountDownLinstener(dataList.get(i)));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyVH extends RecyclerView.ViewHolder{
        private CountDownView countDownView;
        public MyVH(@NonNull View itemView) {
            super(itemView);
            countDownView = itemView.findViewById(R.id.countdownview);
        }
    }


    private class MyCountDownLinstener implements CountDownView.CountDownLinstener{

        private CountDownVO vo;

        public MyCountDownLinstener(CountDownVO vo) {
            this.vo = vo;
        }

        @Override
        public void complete(CountDownView countDownView) {
            countDownView.setText("倒计时完成 "+getDataList().indexOf(vo)+"");
        }
    }
}
