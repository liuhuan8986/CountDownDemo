package com.liuhuan.widget;

import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;


public class CountDownView extends android.support.v7.widget.AppCompatTextView {
    private boolean mStopTicking;
    private boolean mShouldRunTicker;
    private long endTime;
    //和时间结合一起的描述语,必须是这种格式：测试格式%s
    private String description="";
    /**
     * 时分秒是否需要颜色
     */
    private int timeColor=-1;
    private CountDownLinstener countDownLinstener;

    public void setCountDownLinstener(CountDownLinstener linstener) {
        this.countDownLinstener = linstener;
    }

    public void setDescription(String des) {
        this.description = des;
    }

    public void setTimeColor(int timeColor) {
        this.timeColor = timeColor;
    }
    public void setEndTime(long endTime) {
        this.endTime = endTime;
        //onTimeChanged();
    }
    public CountDownView(Context context) {
        super(context);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancel();
    }

    @Override
    public void onVisibilityAggregated(boolean isVisible) {
        super.onVisibilityAggregated(isVisible);

        if (!mShouldRunTicker && isVisible) {
            mShouldRunTicker = true;
            mTicker.run();
        } else if (mShouldRunTicker && !isVisible) {
            mShouldRunTicker = false;
            getHandler().removeCallbacks(mTicker);
        }
    }
    private void onTimeChanged() {
        if (mShouldRunTicker) {
            long currentTime = System.currentTimeMillis();
            long distanceTime = endTime -currentTime;
            if(distanceTime/1000 > 0){
                setText(dealTime(distanceTime));
            }else{
                //setText("倒计时完成1");
                if(countDownLinstener!=null && !mStopTicking){
                    mStopTicking = true;
                    countDownLinstener.complete(CountDownView.this);
                }
            }
        }
    }

    public void start(){
        if(!isAttachedToWindow()){
            return;
        }
        mStopTicking = false;
        mShouldRunTicker = true;
        long distanceTime = endTime -  System.currentTimeMillis();
        if(endTime == -1){
            return;
        }
        if(endTime > 0 && distanceTime/1000 > 0){
            mTicker.run();
        }else{
            onTimeChanged();
        }
    }

    public void cancel(){
        if(mShouldRunTicker && isAttachedToWindow()){
            //LogUtils.e("倒计时取消了");
            mShouldRunTicker = false;
            mStopTicking = true;
            getHandler().removeCallbacks(mTicker);
        }
    }
    private final Runnable mTicker = new Runnable() {
        public void run() {
            if (mStopTicking) {
                return; // Test disabled the clock ticks
            }
            onTimeChanged();

            long now = SystemClock.uptimeMillis();
            long next = now + (1000 - now % 1000);//凑足1秒
            long distanceTime = endTime - System.currentTimeMillis();
            if(distanceTime/1000 > 0){
                getHandler().postAtTime(mTicker, next);
            }else{
                if(countDownLinstener!=null && !mStopTicking){
                    mStopTicking = true;
                    countDownLinstener.complete(CountDownView.this);
                    //setText("倒计时完成2");
                }
            }

        }
    };

    public CharSequence dealTime(long time) {
        StringBuffer returnString = new StringBuffer();
        time = time/1000;

        long day = time / (24 * 60 * 60);
        long hours = (time % (24 * 60 * 60)) / (60 * 60);
        long minutes = ((time % (24 * 60 * 60)) % (60 * 60)) / 60;
        long second = ((time % (24 * 60 * 60)) % (60 * 60)) % 60;


       String format = "%02d";//补O使用
        String dayStr =String.format(format,day);
        String hoursStr = String.format(format,hours);
        String minutesStr = String.format(format,minutes);
        String secondStr = String.format(format,second);

        /* returnString.append(hoursStr).append("小时").append(minutesStr)
                .append("分钟").append(secondStr).append("秒");*/

        if(day > 0){
            returnString.append(dayStr).append("天");
            returnString.append(hoursStr).append("小时");
            returnString.append(minutesStr).append("分钟");
            returnString.append(secondStr).append("秒");
        }else if(hours > 0){
            returnString.append(hoursStr).append("小时");
            returnString.append(minutesStr).append("分钟");
            returnString.append(secondStr).append("秒");
        }else if(minutes > 0){
            returnString.append(minutesStr).append("分钟");
            returnString.append(secondStr).append("秒");
        }else if(second >= 0){
            returnString.append(secondStr).append("秒");
        }
        String content  = returnString.toString();
        if(!TextUtils.isEmpty(description)){
            content = String.format(description,content);
        }
        if(timeColor!=-1){
            //注意 description不能出现，天，小时，分钟，秒，字样，不然这里的颜色设置位置会出错
            ForegroundColorSpan colorSpan0= new ForegroundColorSpan(timeColor);
            ForegroundColorSpan colorSpan1= new ForegroundColorSpan(timeColor);
            ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(timeColor);
            ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(timeColor);
            int dayIndex = content.indexOf("天")-dayStr.length();
            int hoursIndex = content.indexOf("小时")-2;
            int minutesIndex = content.indexOf("分钟")-2;
            int secondIndex = content.indexOf("秒")-2;
            SpannableStringBuilder txt = new SpannableStringBuilder(content);
            if(day > 0) {
                txt.setSpan(colorSpan0, dayIndex, dayIndex+dayStr.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                txt.setSpan(colorSpan1,hoursIndex,hoursIndex+2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                txt.setSpan(colorSpan2,minutesIndex,minutesIndex+2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                txt.setSpan(colorSpan3,secondIndex,secondIndex+2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            }else if(hours > 0){
                txt.setSpan(colorSpan1,hoursIndex,hoursIndex+2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                txt.setSpan(colorSpan2,minutesIndex,minutesIndex+2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                txt.setSpan(colorSpan3,secondIndex,secondIndex+2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            }else if(minutes > 0){
                txt.setSpan(colorSpan2,minutesIndex,minutesIndex+2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                txt.setSpan(colorSpan3,secondIndex,secondIndex+2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            } else if(second >= 0){
                txt.setSpan(colorSpan3,secondIndex,secondIndex+2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            }
            return txt;
        }
        return content;
    }

    public interface CountDownLinstener{
        void complete(CountDownView countDownView);
    }
}
