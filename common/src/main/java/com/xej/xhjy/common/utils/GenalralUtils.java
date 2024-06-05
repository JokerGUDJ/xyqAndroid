package com.xej.xhjy.common.utils;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;

import com.xej.xhjy.common.R;
import com.xej.xhjy.common.base.BaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author dazhi
 * @class GenalralUtils
 * @Createtime 2018/6/14 15:19
 * @description 常用工具类
 * @Revisetime
 * @Modifier
 */
public class GenalralUtils {


    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (TextUtils.isEmpty(str) || str.equals("null")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取缓存目录
     * @param context
     * @return
     */
    public static String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath()+"/path/";
        } else {
            cachePath = context.getCacheDir().getPath()+"/path/";
        }
        return cachePath;
    }

    /**
     * 获取SD卡路径
     * @return
     */
    public static String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if(sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }
    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static int px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxVal / scale);
    }

    /**
     * px转sp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static int px2sp(Context context, float pxVal) {
        return (int) (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 判断程序是否在任务栈里
     *
     * @param context
     * @param pageName
     * @return
     */
    public static int getAppSatus(Context context, String pageName) {

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(20);

        //判断程序是否在栈顶
        if (list.get(0).topActivity.getPackageName().equals(pageName)) {
            return 1;
        } else {
            //判断程序是否在栈里
            for (ActivityManager.RunningTaskInfo info : list) {
                if (info.topActivity.getPackageName().equals(pageName)) {
                    return 2;
                }
            }
            return 3;//栈里找不到，返回3
        }
    }

    /**
     * 判断是否有网络连接
     *
     * @param context 上下文
     * @return
     */
    public static boolean isNetWorkConnected(Context context) {
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null) {
                return info.isAvailable();
            }
        }
        return false;
    }

    public static <T> boolean notEmpty(List<T> list) {
        return !isEmpty(list);
    }

    public static <T> boolean isEmpty(List<T> list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 校验手机号格式
     *
     * @param num
     * @return
     */
    public static boolean checkMobilePhome(String num) {
        if (isEmpty(num)) {
            return false;
        }
        // "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String pattern = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";
        return Pattern.matches(pattern, num);
    }


    /**
     * 校验固定电话
     *
     * @param str
     * @return
     */
    public static boolean isTelephone(String str) {
        if (isEmpty(str)) {
            return false;
        }
        String regex = "(?:(\\(\\+?86\\))(0[0-9]{2,3}\\-?)?([1-9][0-9]{6,7})+(\\-[0-9]{1,6})?)|" +
                "(?:(86-?)?(0[0-9]{2,3}\\-?)?([1-9][0-9]{6,7})+(\\-[0-9]{1,6})?)";
        return Pattern.matches(regex, str);
    }

    /**
     * 校验邮箱
     *
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        String regex = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";
        return Pattern.matches(regex, email);
    }

    /**
     * 校验密码
     *
     * @param password
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPassword(String password) {
        if (isEmpty(password)) {
            return false;
        }
        String regex = "^.{6,18}$";
        return Pattern.matches(regex, password);
    }

    /**
     * 检查某个App是否存在
     * @param context  上下文
     * @param packname 包名
     * @return boolean
     */
    public static boolean checkPackInfo(Context context, String packname) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packname, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }


    /**
     * 适配7.0系统不能操作正常uri，转化为共享URI
     * @param context
     * @param filePath 文件地址
     * @return
     */
    public static Uri getImageContentUri(Context context, String filePath) {
        File file = new File(filePath);
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (file.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     *  利用java原生的摘要实现SHA256加密
     * @param str 加密后的报文
     * @return
     */
    public static String getSHA256StrJava(String str){
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
     * 将byte转为16进制
     * @param bytes
     * @return
     */
    private static String byte2Hex(byte[] bytes){
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i=0;i<bytes.length;i++){
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length()==1){
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    /**
     * 加载本地图片
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        File mFile=new File(url);
        //若该文件存在
        if (mFile.exists()) {
            Bitmap bitmap=BitmapFactory.decodeFile(url);
            return bitmap;
        }
        return null;
    }

    /**
     * 获取当前apk的crc32值
     * @return
     */
    public static Long getInfo(Context context){
        ZipFile zipfile = null;
        ZipEntry dexentry = null;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),PackageManager.GET_META_DATA);
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            String apkPath = applicationInfo.publicSourceDir;
            zipfile = new ZipFile(apkPath);
            dexentry = zipfile.getEntry("classes.dex");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dexentry.getCrc();
    }

    /**
     * 把图片压缩至100K以下
     * @param bitmap
     * @return
     */
    public static Bitmap getCompressImage(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 100是压缩率不压缩，如果是30就是压缩70%，压缩后的存放在baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            options -= 10;// 每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        }
        byte[] bytes = baos.toByteArray();
        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

    }
}
