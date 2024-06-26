package com.netease.nim.uikit.common.media.picker.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import androidx.core.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.media.picker.model.PhotoInfo;
import com.netease.nim.uikit.common.media.picker.model.PickerContract;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;

import java.io.File;
import java.util.List;

import io.reactivex.functions.Consumer;

public class PickImageActivity extends UI {

    private static final String KEY_STATE = "state";

    public static final int FROM_LOCAL = 1;

    public static final int FROM_CAMERA = 2;

    private static final int REQUEST_CODE_CROP = 3;

    private static final int REQUEST_CODE_LOCAL = FROM_LOCAL;

    private static final int REQUEST_CODE_CAMERA = FROM_CAMERA;

    private boolean inited = false;

    public static void start(Activity activity, int requestCode, int from, String outPath) {
        Intent intent = new Intent(activity, PickImageActivity.class);
        intent.putExtra(Extras.EXTRA_FROM, from);
        intent.putExtra(Extras.EXTRA_FILE_PATH, outPath);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void start(Activity activity, int requestCode, int from,
                             String outPath, boolean mutiSelectMode, int multiSelectLimitSize,
                             boolean isSupportOrig, boolean crop, int outputX, int outputY) {
        Intent intent = new Intent(activity, PickImageActivity.class);
        intent.putExtra(Extras.EXTRA_FROM, from);
        intent.putExtra(Extras.EXTRA_FILE_PATH, outPath);
        intent.putExtra(Extras.EXTRA_MUTI_SELECT_MODE, mutiSelectMode);
        intent.putExtra(Extras.EXTRA_MUTI_SELECT_SIZE_LIMIT, multiSelectLimitSize);
        intent.putExtra(Extras.EXTRA_SUPPORT_ORIGINAL, isSupportOrig);
        intent.putExtra(Extras.EXTRA_NEED_CROP, crop);
        intent.putExtra(Extras.EXTRA_OUTPUTX, outputX);
        intent.putExtra(Extras.EXTRA_OUTPUTY, outputY);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nim_pick_image_activity);
        initActionbar();
    }

    private void initActionbar() {
        ImageView head_back = (ImageView) findViewById(R.id.head_back);
        TextView toolbar_title = findView(R.id.toolbar_title);
        toolbar_title.setText("图片选择");
        head_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!inited) {
            new RxPermissions(this)
                    .requestEach(Manifest.permission.CAMERA)
                    .subscribe(new Consumer<Permission>() {
                        @Override
                        public void accept(Permission permission) {
                            if (permission.name.equals(Manifest.permission.CAMERA)) {
                                //当权限获取成功时，permission.granted=true
                                if (permission.granted) {
                                    processIntent();
                                    inited = true;
                                } else {
                                    //未获得权限直接关闭页面
                                    ToastUtils.shortToast(PickImageActivity.this, "未获得相机使用权限，请在设置中修改！");
                                }
                            }
                        }
                    });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_STATE, inited);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            inited = savedInstanceState.getBoolean(KEY_STATE);
        }
    }

    private void processIntent() {
        int from = getIntent().getIntExtra(Extras.EXTRA_FROM, FROM_LOCAL);
        if (from == FROM_LOCAL) {
            pickFromLocal();
        } else {
            pickFromCamera();
        }
    }

    private void pickFromLocal() {
        Intent intent = pickIntent();
        if (intent == null) {
            finish();
            return;
        }

        try {
            startActivityForResult(intent, REQUEST_CODE_LOCAL);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.gallery_invalid, Toast.LENGTH_LONG)
                    .show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, R.string.sdcard_not_enough_head_error,
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void pickFromCamera() {
        try {
            String outPath = getIntent().getStringExtra(Extras.EXTRA_FILE_PATH);
            if (TextUtils.isEmpty(outPath)) {
                Toast.makeText(this, R.string.sdcard_not_enough_error, Toast.LENGTH_LONG).show();
                finish();
                return;
            }
            File outputFile = new File(outPath);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            LogUtils.dazhiLog("pak--->" + mActivity.getPackageName());
            Uri fileUri;
            //这里申请权限
            if (Build.VERSION.SDK_INT >= 24) {
                fileUri = FileProvider.getUriForFile(this, mActivity.getPackageName() + ".generic.file.provider", outputFile);
                LogUtils.dazhiLog("package=" + mActivity.getPackageName() + ".generic.file.provider");
            } else {
                fileUri = Uri.fromFile(outputFile);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, REQUEST_CODE_CAMERA);
        } catch (ActivityNotFoundException e) {
            finish();
        } catch (Exception e) {
            Toast.makeText(this, R.string.sdcard_not_enough_head_error, Toast.LENGTH_LONG).show();
            LogUtils.dazhiLog("Exception=" + e);
            finish();
        }
    }

    private Intent pickIntent() {
        Intent intent = getIntent();
        boolean mutiSelect = intent.getBooleanExtra(Extras.EXTRA_MUTI_SELECT_MODE, false);
        int mutiSelectLimitSize = intent.getIntExtra(Extras.EXTRA_MUTI_SELECT_SIZE_LIMIT, 9);
        boolean isSupportOrg = intent.getBooleanExtra(Extras.EXTRA_SUPPORT_ORIGINAL, false);
        return makeLaunchIntent(this, mutiSelect, mutiSelectLimitSize, isSupportOrg);
    }

    private Intent makeLaunchIntent(Context context, boolean mutiSelectMode,
                                    int mutiSelectLimitSize, boolean isSupportOrig) {
        Intent intent = new Intent();
        intent.setClass(context, PickerAlbumActivity.class);
        intent.putExtra(Extras.EXTRA_MUTI_SELECT_MODE, mutiSelectMode);
        intent.putExtra(Extras.EXTRA_MUTI_SELECT_SIZE_LIMIT,
                mutiSelectLimitSize);
        intent.putExtra(Extras.EXTRA_SUPPORT_ORIGINAL, isSupportOrig);

        return intent;
    }

    private String pathFromResult(Intent data) {
        String outPath = getIntent().getStringExtra(Extras.EXTRA_FILE_PATH);
        if (data == null || data.getData() == null) {
            return outPath;
        }

        Uri uri = data.getData();
        Cursor cursor = getContentResolver()
                .query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
        if (cursor == null) {
            // miui 2.3 有可能为null
            return uri.getPath();
        } else {
            if (uri.toString().contains("content://com.android.providers.media.documents/document/image")) { // htc 某些手机
                // 获取图片地址
                String _id = null;
                String uridecode = uri.decode(uri.toString());
                int id_index = uridecode.lastIndexOf(":");
                _id = uridecode.substring(id_index + 1);
                Cursor mcursor = getContentResolver().query(Images.Media.EXTERNAL_CONTENT_URI, null, " _id = " + _id,
                        null, null);
                mcursor.moveToFirst();
                int column_index = mcursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                outPath = mcursor.getString(column_index);
                if (!mcursor.isClosed()) {
                    mcursor.close();
                }
                if (!cursor.isClosed()) {
                    cursor.close();
                }
                return outPath;

            } else {
                cursor.moveToFirst();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                outPath = cursor.getString(column_index);
                if (!cursor.isClosed()) {
                    cursor.close();
                }
                return outPath;
            }
        }
    }

    private void onPickedLocal(Intent data, int code) {
        boolean mutiSelect = getIntent().getBooleanExtra(Extras.EXTRA_MUTI_SELECT_MODE, false);
        try {
            List<PhotoInfo> photos = PickerContract.getPhotos(data);
            if (photos != null && photos.size() >= 1) {
                PhotoInfo select = photos.get(0);
                String photoPath = select.getAbsolutePath();
                boolean crop = getIntent().getBooleanExtra(Extras.EXTRA_NEED_CROP, false);
                if (crop) {
                    crop(photoPath);
                } else {
                    if (data != null) {
                        Intent result = new Intent(data);
                        result.putExtra(Extras.EXTRA_FROM_LOCAL, true);
                        setResult(RESULT_OK, result);
                        finish();
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, R.string.picker_image_error, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void onPickedCamera(Intent data, int code) {
        try {
            String photoPath = pathFromResult(data);
            boolean crop = getIntent().getBooleanExtra(Extras.EXTRA_NEED_CROP, false);
            if (crop) {
                crop(photoPath);
            } else {
                if (!TextUtils.isEmpty(photoPath)) {
                    Intent result = new Intent();
                    result.putExtra(Extras.EXTRA_FROM_LOCAL, code == REQUEST_CODE_LOCAL);
                    result.putExtra(Extras.EXTRA_FILE_PATH, photoPath);
                    setResult(RESULT_OK, result);
                }
                finish();
            }
        } catch (Exception e) {
            Toast.makeText(this, R.string.picker_image_error, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void crop(String src) {
        Intent intent = getIntent();
        int outputX = intent.getIntExtra(Extras.EXTRA_OUTPUTX, 0);
        int outputY = intent.getIntExtra(Extras.EXTRA_OUTPUTY, 0);
        String outPath = intent.getStringExtra(Extras.EXTRA_FILE_PATH);
        CropImageActivity.startForFile(this, src, outputX, outputY, outPath, REQUEST_CODE_CROP);
    }

    private void onCropped() {
        String outPath = getIntent().getStringExtra(Extras.EXTRA_FILE_PATH);
        Intent result = new Intent();
        result.putExtra(Extras.EXTRA_FILE_PATH, outPath);
        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            setResult(resultCode);
            finish();
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_LOCAL:
                onPickedLocal(data, requestCode);
                break;
            case REQUEST_CODE_CAMERA:
                onPickedCamera(data, requestCode);
                break;
            case REQUEST_CODE_CROP:
                onCropped();
                break;
            default:
                break;
        }
    }
}
