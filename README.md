
# CountDownDemo
#一个列表倒计时的控件 和 一个获取验证码的控件,

#倒计时控件有几个方法：

1.setEndTime 设置结束的时间，必须大于当前时间，这样倒计时才有意义

2.setTimeColor 设置数字时间的颜色，比如：  01天00小时02分05秒，对01,00,02,05这些数字设置颜色

3.setDescription 拼接 时间字符串，但是有特殊的要求，字符串里面必须带有 %s，比如：距离任务剩余%s, 实际看到的是：距离任务剩余01天00小时02分05秒
