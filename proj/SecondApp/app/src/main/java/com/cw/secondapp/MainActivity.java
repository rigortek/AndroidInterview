package com.cw.secondapp;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cw.secondapp.SerializableParcelable.ParcelableObjects;
import com.cw.secondapp.SerializableParcelable.SerializableObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "jcw";

    TextView noticeText;
    Button startSecondActivityBt;
    Thread mThread;

    @Override
     public void onClick(View v) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
//        intent.setClassName(getPackageName(), SecondActivity.class.getName());
        testSerizableAndParcelable(intent);

//        intent.setClassName("com.cw.thirdsystemapp", "com.cw.thirdsystemapp.FullscreenActivity");
        intent.setClassName(getPackageName(), "com.cw.secondapp.SecondActivity");
        // 方式一
//        startActivity(intent);
//        // 方式二
        startActivityForResult(intent, 1);
//        // 方式三
//        getApplicationContext().startActivity(intent);
    }

    private void testSerizableAndParcelable(Intent intent) {
        // Serializable传值，方式一：
        ArrayList arrayList = new ArrayList<String>();
        arrayList.add("pudong");
        arrayList.add("changni");
        arrayList.add("nanghui");
//        Serializable serializable = new SerializableObjects("shanghai", 70, arrayList);
        Serializable serializable = new SerializableObjects("shanghai", 70,
                new ArrayList<String>(Arrays.asList("changsha", "zhuzhou", "xiantang")));

//        TODO 下面这种初始化方式不行，会导致异常，原因待查，怀疑是初始化问题
//        java.lang.RuntimeException:
//        Parcelable encountered IOException writing serializable object (name = com.cw.secondapp.SerializableParcelable.SerializableObjects)

//        Serializable serializable = new SerializableObjects("shanghai", 70, new ArrayList<String>() {
//            {
//                add("changsha");
//                add("zhuzhou");
//                add("xiantang");
//            }
//        });
        intent.putExtra("serializable_second", serializable);

        /**************************************************************/
        // Serializable传值，方式二：
        ArrayList arrayList2 = new ArrayList<String>();
        arrayList2.add("jiading");
        arrayList2.add("jingshang");
        arrayList2.add("chongming");
        Serializable serializable2 = new SerializableObjects("shanghai2", 70, arrayList2);
        Bundle bundle = new Bundle();
        bundle.putSerializable("serialzable_one", serializable2);
        intent.putExtras(bundle);
        /**************************************************************/

        // Parcelable传值
        Parcelable parcelable = new ParcelableObjects("hunan", 500, new ArrayList<String>() {
            {
                add("changsha");
                add("zhuzhou");
                add("xiantang");
            }
        });
        intent.putExtra("parcelable", parcelable);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mThread = Thread.currentThread();

        Log.d(TAG, "onCreate: currentThread " + Thread.currentThread());
        noticeText = (TextView) findViewById(R.id.textNotice);

        startSecondActivityBt = (Button) findViewById(R.id.startSecondActivity);
        startSecondActivityBt.setOnClickListener(this);

        // 获取app UID方法一
        int uid;
        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo(
                    getPackageName(), 0);
            uid = info.uid;
        } catch (PackageManager.NameNotFoundException e) {
            uid = -1;
        }

        Log.i(TAG, "pms get UID = " + uid);

        // 获取app UID方法二
        uid = android.os.Process.myUid();
        Log.i(TAG, "android.os.Process.myUid(): " + uid);

//        testCallerPackageName();
    }

    private void testCallerPackageName() {
        int pid = android.os.Process.myPid();

        Log.d(TAG, "onCreate: get package name method1 : "
                + CallerUtils.getPackageNameFromPid(this, pid));
        Log.d(TAG, "onCreate: get package name metho2 : "
                + CallerUtils.getPackageNameFromPid(pid));
    }

    @Override
    protected void onResume() {
        super.onResume();

//        getContentResolver().call(Uri.parse("content://businessprovider.authorities"), "invoke", null, null);

//        ApkExtracter.copyApk(getApplicationContext(), getExternalCacheDir().getAbsolutePath(), "com.cw.firstapp");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        try {
            throw new NullPointerException("onActivityResult fake exception for print callstack");
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: " + "requestCode: " + requestCode + ", resultCode: " + resultCode);
    }
}
