package com.xej.xhjy.common.safe;


import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class AntiEmulator {

    private static final String TAG = "AntiEmulator";
    private static String[] known_qemu_drivers = {"goldfish"};

    private static String[] known_files = {
            "/system/lib/libc_malloc_debug_qemu.so", "/sys/qemu_trace",
            "/system/bin/qemu-props"};

    private static String[] known_numbers = {"15555215554", "15555215556",
            "15555215558", "15555215560", "15555215562", "15555215564",
            "15555215566", "15555215568", "15555215570", "15555215572",
            "15555215574", "15555215576", "15555215578", "15555215580",
            "15555215582", "15555215584",};


    private static String[] known_imsi_ids = {"310260000000000" // 默认的 imsi id
    };

    // 检测驱动文件内容
    // 读取文件内容，然后检查已知QEmu的驱动程序的列表
    private static Boolean checkQEmuDriverFile() {
        File driver_file = new File("/proc/tty/drivers");
        if (driver_file.exists() && driver_file.canRead()) {
            byte[] data = new byte[1024]; // (int)driver_file.length()
            try {
                InputStream inStream = new FileInputStream(driver_file);
                inStream.read(data);
                inStream.close();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            String driver_data = new String(data);
            for (String known_qemu_driver : com.xej.xhjy.common.safe.AntiEmulator.known_qemu_drivers) {
                if (driver_data.indexOf(known_qemu_driver) != -1) {
                    Log.i("Result:", "Find know_qemu_drivers!");
                    return true;
                }
            }
        }
        Log.i("Result:", "Not Find known_qemu_drivers!");
        return false;
    }

    // 检测模拟器上特有的几个文件
    private static Boolean CheckEmulatorFiles() {
        for (int i = 0; i < known_files.length; i++) {
            String file_name = known_files[i];
            File qemu_file = new File(file_name);
            if (qemu_file.exists()) {
                Log.v("Result:", "Find Emulator Files!");
                return true;
            }
        }
        Log.v("Result:", "Not Find Emulator Files!");
        return false;
    }

    // 检测模拟器默认的电话号码
    private static Boolean CheckPhoneNumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        String phonenumber = telephonyManager.getLine1Number();

        for (String number : known_numbers) {
            if (number.equalsIgnoreCase(phonenumber)) {
                Log.v("Result:", "Find PhoneNumber!");
                return true;
            }
        }
        Log.v("Result:", "Not Find PhoneNumber!");
        return false;
    }

    // 检测imsi id是不是“310260000000000”
    private static Boolean CheckImsiIDS(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        String imsi_ids;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            //获取Android_ID
            imsi_ids = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            imsi_ids = telephonyManager.getSubscriberId();// 手机卡唯一标识,android 10.0获取不到
        }

        for (String know_imsi : known_imsi_ids) {
            if (know_imsi.equalsIgnoreCase(imsi_ids)) {
                Log.v("Result:", "Find imsi ids: 310260000000000!");
                return true;
            }
        }
        Log.v("Result:", "Not Find imsi ids: 310260000000000!");
        return false;
    }

    // 检测手机上的一些硬件信息
    private static Boolean CheckEmulatorBuild(Context context) {
        String BOARD = Build.BOARD;
        String BOOTLOADER = Build.BOOTLOADER;
        String BRAND = Build.BRAND;
        String DEVICE = Build.DEVICE;
        String HARDWARE = Build.HARDWARE;
        String MODEL = Build.MODEL;
        String PRODUCT = Build.PRODUCT;
        if (BOARD == "unknown" || BOOTLOADER == "unknown" || BRAND == "generic"
                || DEVICE == "generic" || MODEL == "sdk" || PRODUCT == "sdk"
                || HARDWARE == "goldfish") {
            Log.v("Result:", "Find Emulator by EmulatorBuild!");
            return true;
        }
        Log.v("Result:", "Not Find Emulator by EmulatorBuild!");
        return false;
    }

    // 检测手机运营商家
    private static boolean CheckOperatorNameAndroid(Context context) {
        String szOperatorName = ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName();

        if (szOperatorName.toLowerCase().equals("android") == true) {
            Log.v("Result:", "Find Emulator by OperatorName!");
            return true;
        }
        Log.v("Result:", "Not Find Emulator by OperatorName!");
        return false;
    }

    // 检测设备ID    设备型号
    private static boolean CheckImei(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (imei != null && imei.equals("000000000000000")) {
                return true;
            }
            return (Build.MODEL.equals("sdk"))
                    || (Build.MODEL.equals("google_sdk"));
        } catch (Exception ioe) {

        }
        return false;
    }

    /**
     * 检测运行环境是否是模拟器
     *
     * @param context 上下文对象
     * @return boolean 检测结果
     */
    public static boolean isEmulator(Context context) {
        //检测imei和设备型号
        boolean imei = CheckImei(context);
        //检测硬件信息
//		boolean EmulatorBuild = CheckEmulatorBuild(context);
        //检测驱动列表
        boolean QEmuDriverFile = checkQEmuDriverFile();
        //检测imsi
        boolean ImsiIDS = CheckImsiIDS(context);
        //检测运营商
        boolean OperatorNameAndroid = CheckOperatorNameAndroid(context);
        return imei || QEmuDriverFile || ImsiIDS || OperatorNameAndroid;
    }
}
