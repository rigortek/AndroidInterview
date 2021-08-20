package com.cw.secondapp;

import android.app.ActivityManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Description:
 * Created by robin.jiang on 2021/8/20.
 */
public class CallerUtils {

    /**
     * Get package name of the process id.
     *
     * @param context
     * @param pid
     * @return package name of the pid
     */
    public static final String getPackageNameFromPid(Context context, int pid) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processes = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : processes) {
            if (info.pid == pid) {
                String[] packages = info.pkgList;
                if (packages.length > 0) {
                    return packages[0];
                }
                break;
            }
        }
        return null;
    }

    /**
     * Get package name of the process id.
     *
     * @param pid
     * @return package name of the pid
     */
    public static String getPackageNameFromPid(int pid) {
        String pkgName = "";
        File f = null;
        f = new File("/proc/" + pid + "/cmdline");
        if (pid != 0 && f != null && f.exists()) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"), 256);
                String line;
                if ((line = br.readLine()) != null) {
                    pkgName = line.trim();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return pkgName;
    }

}
