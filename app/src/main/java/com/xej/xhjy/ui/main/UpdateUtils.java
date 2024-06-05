package com.xej.xhjy.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.content.FileProvider;
import android.view.Gravity;

import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.http.download.DownloadObserver;
import com.xej.xhjy.common.http.http.RxHttpManager;
import com.xej.xhjy.common.utils.GenalralUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.ui.dialog.ClubDialog;
import com.xej.xhjy.common.view.Dialog.ClubSingleBtnDialog;
import com.xej.xhjy.ui.dialog.DownLoadDialog;
import com.xej.xhjy.common.view.Dialog.NegativeListener;
import com.xej.xhjy.common.view.Dialog.PositiveListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import io.reactivex.disposables.Disposable;

/**
 * @author dazhi
 * @class UpdateUtils  更新检查工具类
 * @Createtime 2018/8/29 15:35
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class UpdateUtils {
    /**
     * 下载下载配置文件
     *
     * @param mActivity
     */
    public static void checkUpdate(final BaseActivity mActivity) {
        final String tag = "download_update_file";
        mActivity.addTag(tag);
        RxHttpClient
                .downloadFile(NetConstants.CHECK_VERSION)
                .subscribe(new DownloadObserver("update.json") {

                    @Override
                    protected void getDisposable(Disposable d) {
                        RxHttpManager.addDisposable(tag, d);
                        LogUtils.dazhiLog("下载的文件地址---" + d);
                    }

                    @Override
                    protected void onError(String errorMsg) {
                        LogUtils.dazhiLog("下载的文件地址---" + errorMsg);
                    }

                    @Override
                    protected void onSuccess(long bytesRead, long contentLength, float progress, boolean done, String filePath) {
                        LogUtils.dazhiLog("下载的文件地址---" + filePath);
                        String jsString = getJson(filePath);
                        LogUtils.dazhiLog("更新文件json---》" + jsString);
                        showNewInfo(jsString, mActivity);
                    }
                });
    }

    /**
     * 对比版本号，进行弹窗
     *
     * @param json
     */
    private static void showNewInfo(String json, final BaseActivity activity) {
        try {
            if (GenalralUtils.isEmpty(json)) {
                return;
            }
            JSONObject js = new JSONObject(json);
            JSONObject content = js.optJSONObject("content");
            if (content == null) {
                return;
            }
            final int serverCode = Integer.parseInt(content.optString("AndroidVersion"));
            if (serverCode < 1) {
                return;
            }
            int localCode = getVersionCode(activity);
            LogUtils.dazhiLog("服务器版本---" + serverCode);
            LogUtils.dazhiLog("本地版本---" + localCode);
            if (serverCode > localCode) {//有新版本
                int clientUpdate = Integer.parseInt(content.optString("ClientUpdate"));
                if (clientUpdate == 1) {//需要更新
                    final String updateUrl = content.optString("Android-Path");
                    int forceUpdate = Integer.parseInt(content.optString("ForceUpdate"));
                    if (forceUpdate == 1) {//  1 强制更新  0 不强制更新
                        ClubSingleBtnDialog dialog = new ClubSingleBtnDialog(activity);
                        dialog.setCancelable(false);
                        dialog.setTitle("更新提示");
                        dialog.setMessageGravity(Gravity.CENTER_VERTICAL);
                        dialog.setMessage(content.optString("UpdateDescription"));
                        dialog.setPositiveListener("立即更新", new PositiveListener() {
                            @Override
                            public void onPositiveClick() {
                                downLoadNewApk(updateUrl, serverCode, activity);
                            }
                        });
                        dialog.show();
                    } else if (forceUpdate == 0) {//不强制更新
                        ClubDialog dialog = new ClubDialog(activity);
                        dialog.setTitle("更新提示");
                        dialog.setMessage(content.optString("UpdateDescription"));
                        dialog.setMessageGravity(Gravity.CENTER_VERTICAL);
                        dialog.setPositiveListener("立即更新", new PositiveListener() {
                            @Override
                            public void onPositiveClick() {
                                downLoadNewApk(updateUrl, serverCode, activity);
                            }
                        });
                        dialog.setNegativeListener("下次提醒", new NegativeListener() {
                            @Override
                            public void onNegativeClick() {

                            }
                        });
                        dialog.show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.dazhiLog("下载更新文件解析异常---" + e);

        }
    }

    private static void downLoadNewApk(String url, int code, final BaseActivity activity) {
        final String tag = "download_apk";
        activity.addTag(tag);
        final DownLoadDialog dialog = new DownLoadDialog(activity);
        dialog.setCancelable(false);
        dialog.show();
        final String fileName = code + ".apk";
        RxHttpClient
                .downloadFile(url)
                .subscribe(new DownloadObserver(fileName) {

                    @Override
                    protected void getDisposable(Disposable d) {
                        RxHttpManager.addDisposable(tag, d);
                    }

                    @Override
                    protected void onError(String errorMsg) {
                        ToastUtils.shortToast(activity, "下载失败！");
                    }

                    @Override
                    protected void onSuccess(long bytesRead, long contentLength, float progress, boolean done, String filePath) {
                        if (dialog != null) {
                            dialog.setProgress((int) progress);
                        }
                        if (done) {
                            dialog.dismiss();
                            installApk(activity, filePath);
                            LogUtils.dazhiLog("下载地址-------" + filePath);
                        }
                    }
                });
    }

    //安装应用
    @SuppressLint("CheckResult")
    public static void installApk(BaseActivity activity, String apkPath) {
        File file = new File(apkPath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//7.0以上要使用provider
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(activity.getApplicationContext(),
                    "com.xej.xhjy.fileprovider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            activity.startActivity(intent);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        }
    }


    /**
     * 取文件里的数据
     *
     * @param fileurl
     * @return
     */
    public static String getJson(String fileurl) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream is = new FileInputStream(new File(fileurl));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

}
