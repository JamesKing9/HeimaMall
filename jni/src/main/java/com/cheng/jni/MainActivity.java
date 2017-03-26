package com.cheng.jni;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

import com.mt.mtxx.image.JNI;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    static{
        System.loadLibrary("mtimage-jni");
    }
    @InjectView(R.id.iv)
    ImageView iv;
    @InjectView(R.id.btn)
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
    }


    @OnClick(R.id.btn)
    public void onClick() {
        JNI jni = new JNI();

        // 1.得到原图对象
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.meinv);

        // 2.得到图形对应的数组
        // 用来存储图形对应的数组数据的
        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(),bitmap.getHeight());
        // 3.调用JNI程序修改图形的颜色,其实就是修改了数组
        // 调用public native void StyleLomoC(int[] paramArrayOfInt, int paramInt1, int paramInt2);修改样式
        jni.StyleLomoC(pixels, bitmap.getWidth(),bitmap.getHeight());
        // 4.使用修改后的数组创建一个新的图片,显示出来;
        Bitmap createBitmap = Bitmap.createBitmap(pixels, bitmap.getWidth(),bitmap.getHeight(), bitmap.getConfig());
        iv.setImageBitmap(createBitmap);
    }
}
