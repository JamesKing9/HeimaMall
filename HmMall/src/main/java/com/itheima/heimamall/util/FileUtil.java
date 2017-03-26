package com.itheima.heimamall.util;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.UUID;

/**
 * Created by lxj on 2016/8/29.
 */
public class FileUtil {
    /**
     * 将图片保存到缓存目录
     * @param context
     * @param bitmap
     * @return
     */
    public static File saveBmpToCacheDir(Context context,Bitmap bitmap){
        boolean isSuccess = false;
        File file = new File(context.getCacheDir(),randomFileName(".jpg"));
        try {
            file.delete();
            file.createNewFile();

            FileOutputStream fos = new FileOutputStream(file);
            isSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess?file:null;
    }

    /**
     * 随机生成文件名字
     * @param sufix 文件后缀名
     * @return
     */
    public static String randomFileName(String sufix){
        return UUID.randomUUID().toString()+sufix;
    }
}
