package com.liuhuan.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;


public class SmsCodeView extends android.support.v7.widget.AppCompatTextView {
    private final int DETAULT_TIME = 60;
    //单位秒
    private int time = DETAULT_TIME;
    private int tempTime = time;

    private String description;

    /**
     * description 必须并且只能带一个%s的字符串,比如：重新获取（%s）
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * //单位秒
     * @param time
     */
    public void setTime(int time) {
        this.time = time;
        tempTime = time;
    }
    private CountDownLinstener countDownLinstener;
    private boolean mStopTicking = true;
    public void setCountDownLinstener(CountDownLinstener countDownLinstener) {
        this.countDownLinstener = countDownLinstener;
    }

    public SmsCodeView(Context context) {
        super(context);
    }

    public SmsCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void  start(){
        mStopTicking = false;
        mTicker.run();
        setEnabled(false);
    }

    public boolean isStopTicking() {
        return mStopTicking;
    }

    public void reset(){
        mStopTicking = false;
        tempTime = time;
        setEnabled(true);
    }

    private final Runnable mTicker = new Runnable() {
        public void run() {
            if (mStopTicking) {
                return; // Test disabled the clock ticks
            }
            tempTime-=1;
            if(!TextUtils.isEmpty(description) && description.contains("%s")){
                setText(String.format(description,String.valueOf(tempTime)));
            }else{
                setText(tempTime);
            }
            if(tempTime > 0){
                getHandler().postDelayed(mTicker, 1000);
            }else{
                if(countDownLinstener!=null && !mStopTicking){
                    reset();
                    countDownLinstener.complete(SmsCodeView.this);
                }
            }
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getHandler().removeCallbacks(mTicker);
    }

    public interface CountDownLinstener{
        void complete(SmsCodeView smsCodeView);
    }

}
