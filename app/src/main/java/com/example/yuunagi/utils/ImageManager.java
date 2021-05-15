package com.example.yuunagi.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageManager {

    private static ImageManager imageManager;

    private ImageManager() {
    }

    public static ImageManager getInstance() {
        if (imageManager == null)
            imageManager = new ImageManager();
        return imageManager;
    }

    public void saveImageToGallery(Context context, Bitmap bmp) throws IOException {
        File appDir = new File(context.getExternalFilesDir(null), "bilibili");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        //图片文件名称
        String fileName = "bilibili_" + System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        file.createNewFile();
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String path = file.getAbsolutePath();
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), path, fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
    }

}
