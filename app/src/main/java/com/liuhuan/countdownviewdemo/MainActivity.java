package com.liuhuan.countdownviewdemo;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.liuhuan.widget.SmsCodeView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerview;
    private CountDownAdapter adapter;
    private SmsCodeView smscodeview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerview = findViewById(R.id.recyclerview);
        smscodeview = findViewById(R.id.smscodeview);
        smscodeview.setTime(30);
        smscodeview.setText("获取验证码");
        smscodeview.setDescription("重新获取（%s）");
        smscodeview.setCountDownLinstener(new SmsCodeView.CountDownLinstener() {
            @Override
            public void complete(SmsCodeView smsCodeView) {
                smscodeview.setText("获取验证码");
                smscodeview.setBackgroundColor(ContextCompat.getColor(MainActivity.this,android.R.color.holo_blue_bright));
            }
        });
        smscodeview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smscodeview.start();
                smscodeview.setBackgroundColor(ContextCompat.getColor(MainActivity.this,android.R.color.darker_gray));
            }
        });

        adapter = new CountDownAdapter(this);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerview.setAdapter(adapter);

        List<CountDownVO> list = new ArrayList<>();
        for (int i=0;i < 30 ;i++){
            CountDownVO vo = new CountDownVO();
            if(i%2==0){
                vo.setEndTime((long) (System.currentTimeMillis()+1000*60*60*24*(i+1)*(Math.random()*10)));
            }else{
                vo.setEndTime((long) (System.currentTimeMillis()+1000*(i+1)));
            }
            list.add(vo);
        }
        adapter.getDataList().addAll(list);
        adapter.notifyDataSetChanged();

    }
}
