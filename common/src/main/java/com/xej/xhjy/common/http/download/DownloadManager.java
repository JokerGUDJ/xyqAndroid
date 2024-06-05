package com.xej.xhjy.common.http.download;


import com.xej.xhjy.common.http.RxHttpUtils;
import com.xej.xhjy.common.utils.GenalralUtils;
import com.xej.xhjy.common.utils.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;

/**
 * @author dazhi
 * @class DownloadManager
 * @Createtime 2018/5/30 14:49
 * @description 保存下载的文件
 * @Revisetime
 * @Modifier
 */

public class DownloadManager {
    /**
     * 保存文件
     *
     * @param response     ResponseBody
     * @param destFileName 文件名（包括文件后缀）
     * @return 返回
     * @throws IOException
     */
    public File saveFile(ResponseBody response, final String destFileName, ProgressListener progressListener) throws IOException {

        String destFileDir = GenalralUtils.getDiskCacheDir(RxHttpUtils.getContext()) + File.separator;
//        String destFileDir = GenalralUtils.getSDPath();
        long contentLength = response.contentLength();

        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = response.byteStream();
//            LogUtils.dazhiLog("下载文件大小------"+is.available());
            long sum = 0;

            File dir = new File(destFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, destFileName);
            fos = new FileOutputStream(file);
            boolean isStartInput = false;
            while ((len = is.read(buf)) != -1) {
                isStartInput = true;
                sum += len;
                fos.write(buf, 0, len);
//                由于不能获取文件总长度，所以只能等读完了再回调，那么取消进度，如果想加入进度，则服务器必须加入对下发的资源做GZip操作
                final long finalSum = sum;
                LogUtils.dazhiLog("分段大小------" + sum);
                if (contentLength > 0) {//如果能获取到总长度就回调进度
                    progressListener.onResponseProgress(finalSum, contentLength, (int) ((finalSum * 1.0f / contentLength) * 100), finalSum == contentLength, file.getAbsolutePath());
                }
            }
            if (isStartInput && contentLength < 0) {//开始循环并且走到这里就表示文件输入完毕
                progressListener.onResponseProgress(100, 100, 100, true, file.getAbsolutePath());
            }
            fos.flush();
            return file;

        } finally {
            try {
                response.close();
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
