1. AccessibilityService监听系统所有按键
2. synchronized关键字参数数是可变或不可
3. IdleHandler机制验证，可以结合setMessageLogging来查看非idle状态时执行什么任务
4. Choreographer获取应用刷新帧率
   每个线程都会创建自己的Choreographer对象，即每个线程Choreographer.getInstance()返回的对象都不一样
   当postFrameCallback(mFrameCallback) －》调用到 scheduleFrameLocked时，会检查looper是否一致，
   如果不一致，则通过Handler转回之前Choreographer对象所在线程执行，这样就确保了doFrame回调与Choreographer对象是同一线程;

   简单点说就是无法跨进程监听非本线程frame，而且一般监听子线程也没有什么意义，监听UI线程才有意义。
```
   屏幕刷新率：
   屏幕刷新率代表屏幕在一秒内刷新屏幕的次数，这个值用赫兹来表示，取决于硬件的固定参数。这个值一般是60Hz,即每16.66ms系统发出一个 VSYNC 信号来通知刷新一次屏幕。

   帧速率：
   帧速率代表了GPU在一秒内绘制操作的帧数，比如30fps/60fps。
```
 4.1 doFrame的call stack
```
01-01 09:26:46.070 W/System.err( 7564): java.security.InvalidParameterException: fake exception for debug
01-01 09:26:46.071 W/System.err( 7564):         at com.cw.app.MainActivity$MyFrameCallback.doFrame(MainActivity.java:73)
01-01 09:26:46.071 W/System.err( 7564):         at android.view.Choreographer$CallbackRecord.run(Choreographer.java:765)
01-01 09:26:46.071 W/System.err( 7564):         at android.view.Choreographer.doCallbacks(Choreographer.java:580)
01-01 09:26:46.071 W/System.err( 7564):         at android.view.Choreographer.doFrame(Choreographer.java:549)
01-01 09:26:46.071 W/System.err( 7564):         at android.view.Choreographer$FrameDisplayEventReceiver.run(Choreographer.java:753)
```

  