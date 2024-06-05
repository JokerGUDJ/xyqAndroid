package com.xej.xhjy.ui.web;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsReaderView;
import com.tencent.smtt.sdk.ValueCallback;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.utils.GenalralUtils;
import com.xej.xhjy.common.utils.JsonUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.https.DownLoadCallback;
import com.xej.xhjy.https.DownloadUtils;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.ui.dialog.ClubLoadingDialog;
import com.xej.xhjy.common.view.Dialog.ClubSingleBtnDialog;
import com.xej.xhjy.common.view.Dialog.PositiveListener;
import com.xej.xhjy.ui.view.TitleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author dazhi
 * @class WebPagerActivity
 * @Createtime 2018/6/7 17:02
 * @description 基于腾讯TBS文件浏览服务
 * @Revisetime
 * @Modifier
 */
public class WebFileBrowserActivity extends BaseActivity {
    @BindView(R.id.ll_webContain)
    LinearLayout llWebContain;
    @BindView(R.id.titleview)
    TitleView titleview;
    /**
     * 文件地址
     */
    public static final String FILE_URL_KEY = "file_address";
    @BindView(R.id.img_view)
    ImageView mImage;
    private ClubLoadingDialog mLoadingDialog;
    private TbsReaderView mTbsReaderView;
    private Map<String, String> params = new HashMap<>();
    String mFileName, mFileUrl = "http://222.190.125.58:8080/btbmobi/content/000483/鑫元货币市场基金基金合同.pdf";
    boolean isImage = false;
    Bitmap mBitmap;
    private String filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);
        ButterKnife.bind(this);
        String message = getIntent().getStringExtra(FILE_URL_KEY);
        try {
            JSONObject js = new JSONObject(message);
            params = JsonUtils.jsonObjectToMap(js);
            mFileUrl = NetConstants.BASE_URL + params.get("transactionId").replace("\\", "");
            mFileName = params.get("fileName");
            titleview.setTitle(mFileName);
            filePath = params.get("filePath");
            params.remove("fileName");
            params.remove("transactionId");
            //如果是图片类型
            if (mFileName.endsWith("PNG") || mFileName.endsWith("JPG") ||mFileName.endsWith("JPEG") ||mFileName.endsWith("png") ||mFileName.endsWith("jpg")|| mFileName.endsWith("jpeg")) {
                mImage.setVisibility(View.VISIBLE);
                isImage = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.dazhiLog("mFileName----" + mFileName);
        if (!isImage) {
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            mTbsReaderView = new TbsReaderView(this, new TbsReaderView.ReaderCallback() {
                @Override
                public void onCallBackAction(Integer integer, Object o, Object o1) {

                }
            });
            llWebContain.addView(mTbsReaderView, ll);
        }
        //查看文件是否已下载
//        String fileLocalPath = GenalralUtils.getDiskCacheDir(RxHttpUtils.getContext()) + File.separator + mFileName;
//        if (isLocalExist(fileLocalPath)) {//文件已存在
//            if (isImage){
//                mBitmap = GenalralUtils.getLoacalBitmap(fileLocalPath);
//                mImage.setImageBitmap(mBitmap);
//            } else {
//                displayFile(fileLocalPath);
//            }

//        } else {
            if(!isImage){
                mLoadingDialog = new ClubLoadingDialog(this);
                mLoadingDialog.show();
                LogUtils.dazhiLog("下载的文件usl---" + mFileUrl);
                String tag = "download_file";
                addTag(tag);
                DownloadUtils.downLoadFile(mFileUrl, mFileName,tag, params, new DownLoadCallback() {
                    @Override
                    public void onProgress(float progress) {
//                    生产环境不支持进度
//                    mLoadingDialog.setmChangeMsg(progress + "%");
                    }

                    @Override
                    public void onSuccess(String path) {
                        LogUtils.dazhiLog("下载的文件地址---" + path);
                        if (isImage){
                            mBitmap = GenalralUtils.getLoacalBitmap(path);
                            if (mBitmap !=null){
                                mImage.setImageBitmap(mBitmap);
                            }
                        } else {
                            displayFile(path);
                        }
                        mLoadingDialog.dismiss();
                    }

                    @Override
                    public void onError() {
                        mLoadingDialog.dismiss();
                        ClubSingleBtnDialog dialog = new ClubSingleBtnDialog(WebFileBrowserActivity.this);
                        dialog.setMessage("文件加载失败");
                        dialog.setPositiveListener("确定", new PositiveListener() {
                            @Override
                            public void onPositiveClick() {
                                WebFileBrowserActivity.this.finishWithAnim();
                            }
                        });
                        dialog.show();
                    }
                });
            }else{
                Glide.with(this).load(NetConstants.BASE_IP + filePath).into(mImage);
            }
//        }
    }

    private void displayFile(String filePath) {
//        Bundle bundle = new Bundle();
//        bundle.putString("filePath", filePath);
//        bundle.putString("tempPath", Environment.getExternalStorageDirectory().getPath());
//        boolean result = mTbsReaderView.preOpen(parseFormat(mFileName), false);
//        if (result) {
//            mTbsReaderView.openFile(bundle);
//        }
        openOtherFile(filePath);
    }

    private void openOtherFile(String path) {
        HashMap<String,String> params = new HashMap<>();
        //“0”表示文件查看器使用默认的UI 样式。“1”表示文件查看器使用微信的UI 样式。不设置此key或设置错误值，则为默认UI 样式。
//        params.put("style","1");
        //“true”表示是进入文件查看器，如果不设置或设置为“false”，则进入miniqb 浏览器模式。不是必须设置项
        params.put("local","true");
        //定制文件查看器的顶部栏背景色。格式为“#xxxxxx”，例“#2CFC47”;不设置此key 或设置错误值，则为默认UI 样式。
//        params.put("topBarBgColor","#ff8b3d");
        QbSdk.openFileReader(WebFileBrowserActivity.this, path, params, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {


            }
        });
    }

    private String parseFormat(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private boolean isLocalExist(String fileLocalPath) {
        File file = new File(fileLocalPath);
        return file.exists();
    }


    @Override
    protected void onDestroy() {
        if (mTbsReaderView!=null){
            mTbsReaderView.onStop();
        }
        super.onDestroy();
    }
}
