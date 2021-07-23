package com.cw.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Choreographer;
import android.view.KeyEvent;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "jcw";
    RelativeLayout mRelativeLayout;

    private Choreographer.FrameCallback mFrameCallback;
    private Handler mHandler;

    private static class MyHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1000:
                    fakeBlockUI();
                    sendEmptyMessageDelayed(1000, 500);
                    break;

                default:
                    break;
            }
        }
    }

    private static class MyFrameCallback implements Choreographer.FrameCallback {
        private long mLastFrameTimeNanos = 0;

        @Override
        public void doFrame(long frameTimeNanos) {
            // callback runs once then is automatically removed
            // 所以无需手动remove
            // Choreographer.getInstance().removeFrameCallback(this);

            if (mLastFrameTimeNanos == 0) {
                mLastFrameTimeNanos = frameTimeNanos;
            } else {
                long diffTimes = (frameTimeNanos - mLastFrameTimeNanos) / 1000000;
                int frames = (int) (diffTimes / 16);

                if (diffTimes > 60) {
                    Log.w(TAG, "主线程超时:" + diffTimes + "ms" + " , 丢帧: " + frames
                            + ", main thread? " + Looper.myLooper().equals(Looper.getMainLooper()));
                } else {
                    Log.w(TAG, "当前帧率: " + 1000 / (diffTimes <=0 ? 1 : diffTimes) + ", main thread? "
                            + Looper.myLooper().equals(Looper.getMainLooper()));
                }
                mLastFrameTimeNanos = frameTimeNanos;
            }
            // for debug callstack
//            try {
//                throw new InvalidParameterException("fake exception for debug");
//            } catch (InvalidParameterException e) {
//                e.printStackTrace();
//            }

            // 由于只执行一次，所以要继续post
            Choreographer.getInstance().postFrameCallback(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRelativeLayout = new RelativeLayout(this);
        Log.d(TAG, "---------- onCreate: ---------- " + mRelativeLayout);

        Intent intent = new Intent();
        intent.setClass(this, GloabKeyLinstenService.class);
        startService(intent);

        Log.d(TAG, "---------- onCreate: ---------- " + mRelativeLayout);

        mFrameCallback = new MyFrameCallback();
        Choreographer.getInstance().postFrameCallbackDelayed(mFrameCallback, 0);

        Log.d(TAG, "onCreate: Choreographer.getInstance(): " + Choreographer.getInstance());

//        HandlerThread handlerThread = new HandlerThread("frametest");
//        handlerThread.start();
//        new Handler(handlerThread.getLooper()).postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Choreographer.getInstance().postFrameCallbackDelayed(mFrameCallback, 0);
//                Log.d(TAG, "run: Choreographer.getInstance(): " + Choreographer.getInstance());
//            }
//        }, 100);

        mHandler = new MyHandler();
        mHandler.sendEmptyMessage(1000);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "---------- onNewIntent: ---------- " + mRelativeLayout);

        super.onNewIntent(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "---------- onKeyDown: ---------- " + keyCode);

//        printInstalledCertificates();

        return super.onKeyDown(keyCode, event);
    }

    private static void fakeBlockUI() {
        try {
            TimeUnit.MILLISECONDS.sleep(1000);  // > 16.6ms
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "---------- onStart: ----------" + mRelativeLayout);
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "---------- onRestart: ---------- " + mRelativeLayout);
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "---------- onResume: ---------- " + mRelativeLayout);
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "---------- onPause: ----------" + mRelativeLayout);
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "---------- onStop: ---------- " + mRelativeLayout);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "---------- onDestroy: ---------- " + mRelativeLayout);

        Choreographer.getInstance().removeFrameCallback(mFrameCallback);

        super.onDestroy();
    }

    public void printInstalledCertificates() {
        try {
            KeyStore ks = KeyStore.getInstance("AndroidCAStore");

            if (ks != null) {
                ks.load(null, null);
                Enumeration<String> aliases = ks.aliases();
                DateFormat format = new SimpleDateFormat("yyyy/MM/dd");

                int count = 0;
                while (aliases.hasMoreElements()) {

                    String alias = (String) aliases.nextElement();

                    java.security.cert.X509Certificate cert = (java.security.cert.X509Certificate) ks.getCertificate(alias);
//                    //To print System Certs only
//                    if(cert.getIssuerDN().getName().contains("system")){
//                        Log.d(TAG, "1 printInstalledCertificates: " + cert.getIssuerDN().getName());
//                    }
//
//                    //To print User Certs only
//                    if(cert.getIssuerDN().getName().contains("user")){
//                        Log.d(TAG, "2 printInstalledCertificates: " + cert.getIssuerDN().getName());
//                    }

                    ++count;

                    //To print all certs
                    Log.d(TAG, "颁发给: \r\n"
                            + cert.getIssuerX500Principal() + "\r\n"
                            + "序列号 :" + cert.getSerialNumber()
                            + "\r\n\r\n"
                            + "颁发者: \r\n"
                            + cert.getSubjectX500Principal()
                            + "\r\n\r\n"
                            + "有效期: \r\n"
                            + "颁发时间: " + format.format(cert.getNotBefore()) + "\r\n"
                            + "有效期至: " + format.format(cert.getNotAfter()) + "\r\n"
                    );
                }

                Log.d(TAG, "printInstalledCertificates: count->" + count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (java.security.cert.CertificateException e) {
            e.printStackTrace();
        }
    }
}