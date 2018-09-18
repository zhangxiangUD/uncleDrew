package com.dakun.jianzhong.qiniu;

/**
 * <p>User: wangjie
 * <p>Date: 12/6/2017
 * @author wangjie
 */
public class QiniuUtils {

    public static String imageToUrls(String images, String domain) {
        if (images == null) return null;
        StringBuilder stringBuilder = new StringBuilder();
        String[] imageFileNames = images.split(";");
        for (String image : imageFileNames) {
            stringBuilder.append(QiniuFile.getdownloadurl(domain, image,
                    null, QiniuConstant.portrait_download_app_exp)).append(";");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}
