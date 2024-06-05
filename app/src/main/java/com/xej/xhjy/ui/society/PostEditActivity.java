package com.xej.xhjy.ui.society;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.Base64Utils;
import com.xej.xhjy.common.utils.JsonUtils;
import com.xej.xhjy.common.utils.KeybordUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.common.view.draggridview.DragGridView;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.tools.EventTrackingUtil;
import com.xej.xhjy.ui.dialog.ClubLoadingDialog;
import com.xej.xhjy.common.view.Dialog.ClubSingleBtnDialog;
import com.xej.xhjy.common.view.Dialog.PositiveListener;
import com.xej.xhjy.ui.society.adapter.PostEditPictureAdapter;
import com.xej.xhjy.ui.society.bean.ImageUpLoadBean;
import com.xej.xhjy.ui.web.WebOtherPagerActivity;
import com.yanzhenjie.album.Album;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;

/**
 * @author dazhi
 * @class PostEditActivity 帖子编辑页面
 * @Createtime 2018/12/3 16:03
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class PostEditActivity extends BaseActivity {
    @BindView(R.id.head_back)
    ImageView headBack;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.post_input_edit)
    EditText postInputEdit;
    @BindView(R.id.drag_gridview)
    DragGridView dragGridview;
    @BindView(R.id.tv_topic)
    TextView tvTopic;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.add_layout)
    LinearLayout addLayout;
    @BindView(R.id.add_canel)
    TextView addCanel;
    @BindView(R.id.add_picture)
    TextView addPicture;
    @BindView(R.id.add_video)
    TextView addVideo;
    @BindView(R.id.add_bg)
    View addBg;

    @BindView(R.id.img_video)
    ImageView imgVideo;
    @BindView(R.id.video_play)
    View videoPlay;
    @BindView(R.id.ll_video_layout)
    LinearLayout llVideoLayout;
    @BindView(R.id.iv_delete_video)
    ImageView iv_vidio_delete;
    @BindView(R.id.tv_message_length_click)
    TextView tv_message_length_click;

    PostEditPictureAdapter mAdaper;
    List<String> mList = new ArrayList<>();
    private ClubLoadingDialog mLoadingDialog;
    private boolean isImage, isVideo;
    private String orgId, tipName;
    private boolean postFlag;
    public static final String TIPICTYPE = "tipicType";
    public static final String ORGID = "orgid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_posting);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        orgId = getIntent().getStringExtra("orgId");
        postFlag = getIntent().getBooleanExtra("postFlag", false);
        if (postFlag) {
            topicId = getIntent().getStringExtra("topicId");
            tipName = getIntent().getStringExtra("topicName");
            if (!TextUtils.isEmpty(tipName)) {
                tvTopic.setText(tipName);
            } else {
                tvTopic.setText("");
            }
        } else {
            topicId = "";
        }
        mLoadingDialog = new ClubLoadingDialog(this);
        mList.add("add");
        mAdaper = new PostEditPictureAdapter(mActivity, mList, new PostEditPictureAdapter.DeleteListener() {

            @Override
            public void deleteItem(int position) {//删除一项
                mList.remove(position);
                if (mList.size() == 5 && !mList.contains("add")) {
                    mList.add(5, "add");
                }
                mAdaper.notifyDataSetChanged();
            }

            @Override
            public void clickItem(int position) {//点击一项Item
                KeybordUtils.closeKeybord(postInputEdit, postInputEdit.getContext());
                if (mList.get(position).equals("add")) {//图片和视频不能并存
                    if (mList.size() == 1) {
                        showAddView();
                    } else {
                        Album.album(mActivity)
                                .requestCode(100) // 请求码，返回时onActivityResult()的第一个参数。
                                .toolBarColor(mActivity.getResources().getColor(R.color.red)) // Toolbar 颜色，默认蓝色。
                                .statusBarColor(mActivity.getResources().getColor(R.color.red)) // StatusBar 颜色，默认蓝色。
                                .navigationBarColor(mActivity.getResources().getColor(R.color.red)) // NavigationBar 颜色，默认黑色，建议使用默认。
                                .selectCount(7 - mList.size()) // 最多选择几张图片。
                                .columnCount(3) // 相册展示列数，默认是2列。
                                .camera(true) // 是否有拍照功能。
                                .start();
                    }
                } else {//查看详情

                }
            }
        });
        dragGridview.setAdapter(mAdaper);
        postInputEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    tv_message_length_click.setText("0/1000");
                } else {
                    tv_message_length_click.setText(editable.length() + "/1000");
                }
            }
        });
    }

    private String videoStrBase64, videoFileName, videoImgBase64, videoImgFileName;
    private String topicId, topicName;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                List<String> pathList = Album.parseResult(data);
                mList.remove("add");
                mList.addAll(pathList);
                if (mList.size() < 6) {
                    mList.add("add");
                }
                mAdaper.notifyDataSetChanged();
                if (pathList.size() > 0) {
                    List<JsonObject> list = new ArrayList<>();
                    for (String str : pathList) {
                        JsonObject mapList = new JsonObject();
                        try {
                            File newFile = new Compressor(this)
                                    .setMaxWidth(640)
                                    .setMaxHeight(480)
                                    .setQuality(80)
                                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                    .compressToFile(new File(str));
                            LogUtils.dazhiLog("压缩前文件大小------->" + new File(str).length() / 1024);
                            LogUtils.dazhiLog("压缩后文件大小------->" + newFile.length() / 1024);
                            mapList.addProperty("imgStr", Base64Utils.fileToBase64(newFile));
                            mapList.addProperty("fileName", newFile.getName());
                            mapList.addProperty("fileType", "1");
                            list.add(mapList);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    postImage(list);
                }
            } else if (requestCode == 99) {
                llVideoLayout.setVisibility(View.VISIBLE);
                dragGridview.setVisibility(View.GONE);
                String path = data.getStringExtra("video_path");
                String imageBase64 = data.getStringExtra("video_first_image");
                imgVideo.setImageBitmap(Base64Utils.base64ToBitmap(imageBase64));
                LogUtils.dazhiLog("视频地址-----》" + path);
                LogUtils.dazhiLog("视频大小-----》" + new File(path).length() / 1024);
                //视频base64
                videoStrBase64 = Base64Utils.encodeBase64File(path);
                videoFileName = new File(path).getName();
                //缩略图base64
                videoImgBase64 = imageBase64;
                videoImgFileName = System.currentTimeMillis() + ".jpg";
                postUpLoadVideo();
            } else if (requestCode == 101) {
                String path = data.getStringExtra("loaction_addr");
                tvAddress.setText(path);
            }
        } else if (resultCode == TopicListActivity.RESULTOK && requestCode == 132) {
            topicId = data.getStringExtra("topicId");
            topicName = data.getStringExtra("topicName");
            tvTopic.setText("#" + topicName);
        }
    }

    private StringBuffer listPath = new StringBuffer();

    /**
     * 上传图片
     *
     * @param list
     */
    private void postImage(List<JsonObject> list) {
        mLoadingDialog.show();
        String TAG = "send_post_image";
        mActivity.addTag(TAG);
        Map<String, String> map = new HashMap<>();
        map.put("imgStrList", list.toString());
        String url = NetConstants.SOCIETY_POOT_IMAGE;

        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                isImage = true;
                mLoadingDialog.dismiss();
                ImageUpLoadBean bean = JsonUtils.stringToObject(jsonString, ImageUpLoadBean.class);
                if ("0".equals(bean.getCode())) {
                    ToastUtils.shortToast(mActivity, "上传成功");
                    String strPath = null;
                    for (int i = 0; i < bean.getContent().size(); i++) {
                        strPath = bean.getContent().get(i).getImgPath();
                        if (strPath != null) {
                            listPath.append(strPath);
                            listPath.append(",");
                        }
                    }
                } else {
                    ToastUtils.shortToast(mActivity, "上传失败");
                }
            }

            @Override
            public void onError(String errorMsg) {
                mLoadingDialog.dismiss();
            }
        });


    }

    private void showAddView() {
        addLayout.setAnimation(AnimationUtils.loadAnimation(this, R.anim.bottom_up));
        addLayout.setVisibility(View.VISIBLE);
        addBg.setAlpha(0f);
        addBg.setVisibility(View.VISIBLE);
        addBg.animate()
                .alpha(1f)
                .setDuration(300)
                .setListener(null);
    }

    private void hideAddView() {
        addLayout.setAnimation(AnimationUtils.loadAnimation(this, R.anim.bottom_down));
        addLayout.setVisibility(View.GONE);
        addBg.animate()
                .alpha(0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        addBg.setVisibility(View.GONE);
                    }
                });
    }

    @OnClick({R.id.head_back, R.id.tv_topic, R.id.add_picture, R.id.add_video, R.id.add_canel, R.id.add_bg, R.id.tv_address, R.id.tv_send, R.id.iv_delete_video})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back:
                finishWithAnim();
                break;
            case R.id.tv_topic:
                Intent intentTopic = new Intent(PostEditActivity.this, TopicListActivity.class);
                intentTopic.putExtra(TIPICTYPE, true);
                intentTopic.putExtra(ORGID, orgId);
                mActivity.startActivityForResult(intentTopic, 132);
                break;
            case R.id.tv_address:
                //腾讯地图定位选址，采用h5实现
                new RxPermissions(this)
                        .request(Manifest.permission.ACCESS_FINE_LOCATION)
                        .subscribe(granted -> {
                            if (granted) {
                                Intent intent = new Intent(mActivity, WebOtherPagerActivity.class);
                                intent.putExtra(WebOtherPagerActivity.LOAD_URL, AppConstants.AMAP_URL);
                                intent.putExtra(WebOtherPagerActivity.HEAD_TITLE, "定位");
                                mActivity.startActivityForResult(intent, 101);
                            } else {
                                ClubSingleBtnDialog dialog = new ClubSingleBtnDialog(mActivity);
                                dialog.setMessage("鑫合家园未获得定位相关权限，请到设置中修改！");
                                dialog.setPositiveListener("确定", new PositiveListener() {
                                    @Override
                                    public void onPositiveClick() {
                                        finish();
                                    }
                                });
                                dialog.show();
                            }
                        });

                break;
            case R.id.iv_delete:
                break;
            case R.id.add_picture:
                Album.album(mActivity)
                        .requestCode(100) // 请求码，返回时onActivityResult()的第一个参数。
                        .toolBarColor(mActivity.getResources().getColor(R.color.red)) // Toolbar 颜色，默认蓝色。
                        .statusBarColor(mActivity.getResources().getColor(R.color.red)) // StatusBar 颜色，默认蓝色。
                        .navigationBarColor(mActivity.getResources().getColor(R.color.red)) // NavigationBar 颜色，默认黑色，建议使用默认。
                        .selectCount(7 - mList.size()) // 最多选择几张图片。
                        .columnCount(3) // 相册展示列数，默认是2列。
                        .camera(true) // 是否有拍照功能。
                        .start();
                hideAddView();
                break;
            case R.id.add_video:
                hideAddView();
                //同时请求多个权限
                new RxPermissions(this)
                        .request(Manifest.permission.CAMERA,
                                Manifest.permission.RECORD_AUDIO)
                        .subscribe(granted -> {
                            if (granted) {
                                Intent intent = new Intent(mActivity, VideoRecordActivity.class);
                                mActivity.startActivityForResult(intent, 99);
                            } else {
                                ClubSingleBtnDialog dialog = new ClubSingleBtnDialog(mActivity);
                                dialog.setMessage("鑫合家园未获得录像相关权限，请到设置中修改！");
                                dialog.setPositiveListener("确定", new PositiveListener() {
                                    @Override
                                    public void onPositiveClick() {
                                        finish();
                                    }
                                });
                                dialog.show();
                            }
                        });
                break;
            case R.id.add_canel:
                hideAddView();
                isImage = false;
                break;
            case R.id.add_bg:
                hideAddView();
                break;
            case R.id.tv_send:
                if (postFlag) {
                    String topicName = tvTopic.getText().toString().trim();
                    if (!TextUtils.isEmpty(topicName)) {
                        postSend();
                    } else {
                        ToastUtils.longToast(PostEditActivity.this, "请选择话题再发帖");
                    }
                } else {
                    postSend();
                }
                break;
            case R.id.iv_delete_video:
                llVideoLayout.setVisibility(View.GONE);
                dragGridview.setVisibility(View.VISIBLE);
                isVideo = false;
                break;
        }
    }

    /**
     * 发帖子
     */
    private void postSend() {
        //上报埋点
        EventTrackingUtil.EventTrackSubmit(mActivity, "pageCircle", "channel=android",
                "eventPost", "topicId="+topicId);

        mLoadingDialog.show();
        String TAG = "send_post";
        mActivity.addTag(TAG);
        Map<String, String> map = new HashMap<>();
        String content = postInputEdit.getText().toString().trim();
        String str;
        if (!TextUtils.isEmpty(content)) {
            try {
                str = URLEncoder.encode(content, "utf-8");
                map.put("content", str);
            } catch (UnsupportedEncodingException e) {
                map.put("content", "");
            }
        } else {
            if (!isImage && !isVideo) {
                ToastUtils.shortToast(mActivity, "请输入内容...");
                if (mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                }
                return;
            }
        }
        map.put("posterId", PerferenceUtils.get(AppConstants.User.ID, ""));
        map.put("posterName", PerferenceUtils.get(AppConstants.User.NAME, ""));
        if (listPath != null && listPath.length() > 0) {
            if (isImage) {
                map.put("accessoryUoloadList", listPath.substring(0, listPath.length() - 1));
                map.put("fileType", "1");
            }
            if (isVideo) {
                map.put("accessoryUoloadList", listPath.substring(0, listPath.length() - 1));
                map.put("fileType", "2");
            }
        }
        map.put("commitId", orgId);//专委会Id
        if (!TextUtils.isEmpty(topicId)){
            map.put("topicId", topicId);
        }
        map.put("topicName", tvTopic.getText().toString().trim());
        map.put("site", tvAddress.getText().toString());
        LogUtils.dazhiLog("发帖参数----->" + map);
        String url = NetConstants.SOCIETY_POST;
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                LogUtils.dazhiLog("发帖成功");
                if (listPath != null) {
                    listPath = null;
                }
                mLoadingDialog.dismiss();
                Intent intent = new Intent();
                if (postFlag) {
                    setResult(TopicDetailActivity.RESULT_OK, intent);
                } else {
                    setResult(CommitteeActivity.RESULT_BACK, intent);
                }
                finishWithAnim();

            }

            @Override
            public void onError(String errorMsg) {
                LogUtils.dazhiLog("发帖失败");
                mLoadingDialog.dismiss();
            }
        });


    }

    /**
     * 上传视频
     */
    private void postUpLoadVideo() {
        mLoadingDialog.show();
        String TAG = "send_post_video";
        mActivity.addTag(TAG);
        Map<String, String> map = new HashMap<>();
        JsonObject jsonVideo = new JsonObject();
        JsonObject jsonImg = new JsonObject();
        JsonArray array = new JsonArray();
        jsonVideo.addProperty("fileName", videoFileName);
        jsonVideo.addProperty("imgStr", videoStrBase64);
        jsonVideo.addProperty("type", "2");
        jsonImg.addProperty("fileName", videoImgFileName);
        jsonImg.addProperty("imgStr", videoImgBase64);
        jsonImg.addProperty("type", "1");
        array.add(jsonVideo);
        array.add(jsonImg);
        map.put("imgStrList", array.toString());

        String url = NetConstants.SOCIETY_POOT_IMAGE;

        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                isVideo = true;
                ImageUpLoadBean bean = JsonUtils.stringToObject(jsonString, ImageUpLoadBean.class);
                if ("0".equals(bean.getCode())) {
                    ToastUtils.shortToast(mActivity, "上传成功");
                    String strPath = null;
                    for (int i = 0; i < bean.getContent().size(); i++) {
                        strPath = bean.getContent().get(i).getImgPath();
                        if (strPath != null) {
                            listPath.append(strPath);
                            listPath.append(",");
                        }
                    }
                } else {
                    ToastUtils.shortToast(mActivity, "上传失败");
                }
                mLoadingDialog.dismiss();

            }

            @Override
            public void onError(String errorMsg) {
                mLoadingDialog.dismiss();
            }
        });

    }

    @Override
    protected void onDestroy() {
        if (mLoadingDialog != null) {
            mLoadingDialog = null;
        }
        super.onDestroy();
    }
}
