package com.xej.xhjy.https;

import android.content.Context;

import com.xej.xhjy.ClubApplication;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.Base64Utils;
import com.xej.xhjy.common.utils.GenalralUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.MD5Utils;
import com.xej.xhjy.common.utils.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * @author dazhi
 * @class EncryptUtils
 * @Createtime 2018/6/6 09:48
 * @description 报文加解密管理类以及MD5加密
 * @Revisetime
 * @Modifier
 */
public class HttpEncryptUtils {
    private static String[] StrNum = {"j", "b", "A", "B", "l", "b", "g", "b", "a", "L", "F", "y", "b", "t", "z", "J", "g", "G", "n", "Y", "P", "Z", "D", "Y", "T", "b", "I", "z", "o", "b", "B", "p", "J", "J", "s", "n", "s", "C", "q", "l", "Q", "c", "U", "H", "q", "U", "S", "G", "M", "s", "Q", "t"};
    private static int minNum = 0;
    private static int maxNum = 51;
    public static final String ECB_PADDING = "RSA/ECB/PKCS1Padding";//加密填充方式
    public static final String RSA = "RSA";// 非对称加密密钥算法
    private static int KEYSIZE = 1024;// 密钥位数
    private static int RESERVE_BYTES = 11;// 加密block需要预留字节数
    private static int DECRYPT_BLOCK = KEYSIZE / 8; // 每段解密block数，117 bytes
    private static int ENCRYPT_BLOCK = DECRYPT_BLOCK - RESERVE_BYTES; // 每段加密block数117bytes
    //一次性加密数据的长度不能大于117 字节
    private static final int ENCRYPT_BLOCK_MAX = 117;
    //一次性解密的数据长度不能大于128 字节
    private static final int DECRYPT_BLOCK_MAX = 128;
    private static Random random = new Random();

    public static String encryptRequest(String requestStr) {
        if (!GenalralUtils.isEmpty(requestStr)) {
            int num[] = new int[16];
            StringBuffer buffEncrypt = new StringBuffer();
            StringBuffer buffReport = new StringBuffer();
            for (int i = 0; i < num.length; i++) {
                int randNum = random.nextInt(maxNum) + minNum;
                buffEncrypt.append(StrNum[randNum]);
                if (i < num.length - 1) {
                    buffReport.append(randNum + ",");
                } else {
                    buffReport.append(randNum);
                }
            }
            byte[] arr = new byte[0];
            try {
                //用公钥key加密随机数
                arr = encryptWithPublicKey(buffReport.toString().getBytes(), ClubApplication.getContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //存储这个用公钥加密后的随机数
            String arrKey = "xhjy" + System.currentTimeMillis() + (int) ((Math.random() * 9 + 1) * 100);
//            LogUtils.dazhiLog("随机数key---------" + arrKey);
            arrKey = MD5Utils.getMD5String(arrKey);
            PerferenceUtils.put(arrKey, android.util.Base64.encodeToString(arr, android.util.Base64.DEFAULT));
            JSONObject otherJson = new JSONObject();
            try {
                //将公钥加密后的随机数传给后台
                otherJson.put("KeyArray", android.util.Base64.encodeToString(arr, android.util.Base64.DEFAULT));
                //将存储key传给后台
                otherJson.put("NumberArray", arrKey);
                otherJson.put("SystemVersion", AppConstants.VERSION);//系统版本
                otherJson.put("DeviceName", AppConstants.PHONEBRAND + " " + AppConstants.PHONEMODLE);//设备
                otherJson.put("DeviceMac", AppConstants.DEVICEID);
                otherJson.put("AppVersion", AppConstants.APP_VERSION);//app版本
                // 对通讯报文请求参数进行Base64加密
                JSONObject requestJson = new JSONObject(requestStr);
                requestJson.put("NoStateToken", "");
                String params = requestJson.toString();
                byte[] btyeParams = encrypt(params, buffEncrypt.toString());
                String newParams = new String(Base64Utils.encodeBase64(btyeParams));
                otherJson.put("XhjyData", newParams);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return otherJson.toString();
        } else {
            return "";
        }
    }

    public static String decryptResponse(String responseStr) {
        try {
            JSONObject jsonObject = new JSONObject(responseStr);
            String result = jsonObject.optString("XhjyResData");
            //获取keycode
            String keycode = jsonObject.optString("NumberArray");
            byte[] bytes = new byte[0];
            //拿到存储的加密随机数
            String keyValue = PerferenceUtils.get(keycode, "");
            try {
                bytes = decryptWithPrivateKey(android.util.Base64.decode(keyValue, android.util.Base64.DEFAULT), ClubApplication.getContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
            String numberArray = new String(bytes);
            if (!GenalralUtils.isEmpty(numberArray)) {
                String Str[] = numberArray.split(",");
                StringBuffer buf = new StringBuffer();
                for (int i = 0; i <= Str.length - 1; i++) {
                    if (!GenalralUtils.isEmpty(Str[i])) {
                        buf.append(StrNum[Integer.valueOf(Str[i])]);
                    }
                }
                if (null != result) {
                    byte[] bts = Base64Utils.decodeBase64(result.getBytes());
                    String realResult = decode(bts, buf.toString());
                    return realResult;
                }
            }
            PerferenceUtils.removeKey(keycode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseStr;
    }

    /**
     * 私钥解密
     *
     * @param data    待解密数据
     * @param context 密钥
     */
    public static byte[] decryptWithPrivateKey(byte[] data, Context context)
            throws Exception {
        Cipher cp = Cipher.getInstance(ECB_PADDING);
        cp.init(Cipher.DECRYPT_MODE, getPrivateKey(context));
        return doFinalWithBatch(data, cp, DECRYPT_BLOCK_MAX);
    }

    public static PublicKey getPublicKey(Context context) throws Exception {
        InputStream inStream = context.getAssets().open("app_xhjy_rsa_key.key");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(input2byte(inStream));
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        inStream.close();
        return keyFactory.generatePublic(keySpec);
    }

    public static PrivateKey getPrivateKey(Context context) throws Exception {
        InputStream inStream = context.getAssets().open("app_xhjy_rsa_cer.cer");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(input2byte(inStream));
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        inStream.close();
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 用公钥对字符串进行加密
     *
     * @param data 原文
     */
    public static byte[] encryptWithPublicKey(byte[] data, Context context)
            throws Exception {
        Cipher cp = Cipher.getInstance(ECB_PADDING);
        cp.init(Cipher.ENCRYPT_MODE, getPublicKey(context));
        return doFinalWithBatch(data, cp, ENCRYPT_BLOCK_MAX);
    }

    /**
     * @param content
     * @param cipher
     * @param blockSize
     * @return 传入实例进行加减密
     * @throws Exception
     */
    public static byte[] doFinalWithBatch(byte[] content, Cipher cipher, int blockSize) throws Exception {
        int offset = 0;//操作的起始偏移位置
        int len = content.length;//数据总长度
        byte[] tmp;//临时保存操作结果
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //如果剩下数据
        while (len - offset > 0) {
            if (len - offset >= blockSize) {
                //剩下数据还大于等于一个blockSize
                tmp = cipher.doFinal(content, offset, blockSize);
            } else {
                //剩下数据不足一个blockSize
                tmp = cipher.doFinal(content, offset, len - offset);
            }
            //将临时结果保存到内存缓冲区里
            baos.write(tmp);
            offset = offset + blockSize;
        }
        baos.close();
        return baos.toByteArray();
    }

    public static byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }

    private static byte[] encrypt(String sbData, String Str) {
        byte[] result = null;
        try {
            SecretKeySpec key = new SecretKeySpec(Str.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// 创建密码器
            IvParameterSpec ips = new IvParameterSpec(Str.getBytes());
            byte[] byteContent = sbData.toString().getBytes("UTF-8");
            cipher.init(Cipher.ENCRYPT_MODE, key, ips);// 初始化
            result = cipher.doFinal(byteContent);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return result;
    }


    private static String decode(byte[] bytes, String buffer) {
        String resul = "";
        try {
            SecretKeySpec key = new SecretKeySpec(buffer.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// 创建密码器
            IvParameterSpec ips = new IvParameterSpec(buffer.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, key, ips);// 初始化
            byte[] str = cipher.doFinal(bytes);
            resul = new String(str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return resul;
    }

    /**
     * 正常MD5加密
     *
     * @param content
     * @return
     */
    public static String md5(String content) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * Https校验证书
     *
     * @param context
     * @return
     */
    public static SSLSocketFactory getSSLSocketFactory(Context context) {
        SSLContext sslContext = null;
        try {
            try {
                try {
                    try {
                        //1.生成证书对象
                        LogUtils.dazhiLog("开始X.509------>");
                        CertificateFactory factory = CertificateFactory.getInstance("X.509");
                        InputStream inputStream = context.getAssets().open("xhjy.cer");
                        Certificate certificate = factory.generateCertificate(inputStream);
                        inputStream.close();
                        LogUtils.dazhiLog("xhjy.cer读取成功------>");
                        //2、生成KeyStore对象，导入信任证书
                        String keyStoreType = KeyStore.getDefaultType();
                        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                        try {
                            keyStore.load(null, null);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        keyStore.setCertificateEntry("xinehome", certificate);
                        //3.使用KeyStore初始化TrustManagerFactor
                        String str = TrustManagerFactory.getDefaultAlgorithm();
                        TrustManagerFactory trustManagerFactory = null;
                        try {
                            trustManagerFactory = TrustManagerFactory.getInstance(str);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        trustManagerFactory.init(keyStore);
                        //4.使用TrustManagerFactory初始化SSLContext

                        try {
                            sslContext = SSLContext.getInstance("TLSv1");
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }

                        sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

                        //5.使用SSLContext得到一个SSLSocketFactory对象
                        LogUtils.dazhiLog("setSslSocketFactory成功--->");
                    } catch (KeyStoreException e) {
                        e.printStackTrace();
                    }
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return sslContext.getSocketFactory();
    }
}
