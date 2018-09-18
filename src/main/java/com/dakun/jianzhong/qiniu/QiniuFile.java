package com.dakun.jianzhong.qiniu;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

import java.io.File;

public class QiniuFile {

    private static Configuration cfg = new Configuration(Zone.zone1());
    private static String ACCESS_KEY_ceshi = "p-1liZqOtroJtCfVqpfgEeLvcp_XRQJi4YyredZz";
    private static String SECRET_KEY_ceshi = "4aTkvE4hwFqMt1psyL9iReJcRuA58imrBTcVECck";
    private static Auth auth = Auth.create(ACCESS_KEY_ceshi, SECRET_KEY_ceshi);


    public static BucketManager bucketManager = new BucketManager(auth, cfg);

    /*
     * 获取上传token
     * bucket：上传的目标空间名
     * putPolicy：上传策略
     */
    public static String getuploadtoken(String bucket, StringMap putPolicy) {
        return auth.uploadToken(bucket, null, 60 * 60, putPolicy);
    }

	/*
     * 上传文件
	 * bucket:上传的目标空间名
	 * returnBody: 返回值
	 * file:上传源文件
	 * key:目标文件名
	 */

    public static Response upload(String bucket, StringMap returnBody,
                                  File file, String key) {
        String token = auth.uploadToken(bucket, null, 60 * 5, returnBody);
        UploadManager uploadManager = new UploadManager(cfg);
        try {
            Response res = uploadManager.put(file, key, token);
            // uploadManager.put(file, key, token);
            return res;
        } catch (QiniuException e) {
            e.printStackTrace();
            Response res = e.response;
            return res;
        }

    }

    public static Response upload(final String bucket, final File file, final String key) throws QiniuException {
        String token = auth.uploadToken(bucket);
        UploadManager uploadManager = new UploadManager(cfg);
        return uploadManager.put(file, key, token);
    }

    public static Response upload(String bucket, StringMap returnBody,
                                  final byte data[], final String key) throws QiniuException {
        String token = auth.uploadToken(bucket, null, 60 * 60, returnBody);
        UploadManager uploadManager = new UploadManager(cfg);
        return uploadManager.put(data, key, token);
    }

    public static Response upload(String bucket, final byte data[], final String key) throws QiniuException {
        String token = auth.uploadToken(bucket);
        UploadManager uploadManager = new UploadManager(cfg);
        return uploadManager.put(data, key, token);
    }


    /*
     * 删除文件
     * bucket:目标空间名
     * key:目标文件名
     */
    public static void delete(String bucket, String key) {
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException e) {
            System.out.println("QiniuFIle.delete error,bucket:" + bucket
                    + "key:" + key);
        }
    }

    /*
     * 移动文件
     * bucketSrc:源目标空间名
     * keySrc:源目标文件名
     * bucketDest:源目标空间名
     * keyDest:源目标文件名
     */
    public static void move(String bucketSrc, String keySrc, String bucketDest,
                            String keyDest) {
        try {
            bucketManager.move(bucketSrc, keySrc, bucketDest, keyDest);
        } catch (QiniuException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 获得下载地址
     * domain:目标域，如ot9fb9yy1.bkt.clouddn.com
     * key:目标文件名
     * addkey:处理规则，如?imageView2/2/h/200，若无处理，则为空串
     * exptime:下载有效时长
     */
    public static String getdownloadurl(String domain, String key,
                                        String addkey, int exptime) {
        String downloadUrl = "";
        String baseUrl = "http://" + domain + "/" + key;
        if (addkey == null || "".equals(addkey))
            downloadUrl = auth.privateDownloadUrl(baseUrl, exptime);
        else
            downloadUrl = auth.privateDownloadUrl(baseUrl + addkey, exptime);

        return downloadUrl;
    }

    /**
     * 移动文件 bucketSrc:源目标空间名 keySrc:源目标文件名 bucketDest:源目标空间名 keyDest:源目标文件名
     */
    public static void rename(String bucket, String oldkey, String newkey) {
        try {
            bucketManager.rename(bucket, oldkey, newkey);
        } catch (QiniuException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
