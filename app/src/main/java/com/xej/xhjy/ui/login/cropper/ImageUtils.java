package com.xej.xhjy.ui.login.cropper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;

import com.xej.xhjy.common.utils.FileUtil;
import com.xej.xhjy.common.utils.LogUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Author   lhy_0203
 * Date     2018/6/24
 * Desc     ${图片相关工具类}
 */

public class ImageUtils {

    /**
     * 保存图片
     *
     * @param src      源图片
     * @param filePath 要保存到的文件路径
     * @param format   格式
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public static boolean save(Bitmap src, String filePath, CompressFormat format) {
        return save(src, FileUtil.getFileByPath(filePath), format, false);
    }

    /**
     * 保存图片
     *
     * @param src    源图片
     * @param file   要保存到的文件
     * @param format 格式
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public static boolean save(Bitmap src, File file, CompressFormat format) {
        return save(src, file, format, false);
    }

    /**
     * 保存图片
     *
     * @param src      源图片
     * @param filePath 要保存到的文件路径
     * @param format   格式
     * @param recycle  是否回收
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public static boolean save(Bitmap src, String filePath, CompressFormat format, boolean recycle) {
        return save(src, FileUtil.getFileByPath(filePath), format, recycle);
    }


    public static boolean savePhotos(Context context, Bitmap src, String filePath, CompressFormat format, boolean recycle) {
        return savePhoto(context, src, FileUtil.getFileByPath(filePath), format, recycle);
    }

    /**
     * 保存图片
     *
     * @param src     源图片
     * @param file    要保存到的文件
     * @param format  格式
     * @param recycle 是否回收
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public static boolean save(Bitmap src, File file, CompressFormat format, boolean recycle) {
        if (isEmptyBitmap(src) || !FileUtil.createOrExistsFile(file)) {

            return false;
        }
        OutputStream os = null;
        boolean ret = false;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            ret = src.compress(format, 50, os);
            if (recycle && !src.isRecycled()) {
                src.recycle();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileUtil.closeIO(os);
        }
        return ret;
    }

    /**
     * 保存图片
     *
     * @param src     源图片
     * @param file    要保存到的文件
     * @param format  格式
     * @param recycle 是否回收
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public static boolean savePhoto(Context context, Bitmap src, File file, CompressFormat format, boolean recycle) {
        if (isEmptyBitmap(src) || !FileUtil.createOrExistsFile(file)) {

            return false;
        }
        OutputStream os = null;
        boolean ret = false;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            ret = src.compress(format, 50, os);
            if (recycle && !src.isRecycled()) {
                src.recycle();
            }

            //发广播告诉相册有图片需要更新，这样可以在图册下看到保存的图片了
            LogUtils.dazhiLog("保存到相册广播执行");
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            context.sendBroadcast(intent);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileUtil.closeIO(os);
        }
        return ret;
    }

    /**
     * 判断bitmap对象是否为空
     *
     * @param src 源图片
     * @return {@code true}: 是<br>{@code false}: 否
     */
    private static boolean isEmptyBitmap(Bitmap src) {
        LogUtils.dazhiLog("bitmap为空" + src);
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }


}
