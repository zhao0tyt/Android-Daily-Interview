package com.zzq.handlerthread;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Demo2 extends AppCompatActivity {
    /**
     * 图片地址集合
     */
    private String url[]={
            "https://img-blog.csdn.net/20160903083245762",
            "https://img-blog.csdn.net/20160903083252184",
            "https://img-blog.csdn.net/20160903083257871",
            "https://img-blog.csdn.net/20160903083311972",
            "https://img-blog.csdn.net/20160903083319668",
            "https://img-blog.csdn.net/20160903083326871"
    };
    private ImageView imageView;
    private HandlerThread handlerThread;
    private Handler mUiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bitmap bitmap = (Bitmap) msg.obj;
            imageView.setImageBitmap(bitmap);
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo2);
        imageView = findViewById(R.id.image);

        //创建异步HandlerThread
        handlerThread = new HandlerThread("downloadImage");
        handlerThread.start();

        Handler childHandler = new Handler(handlerThread.getLooper(), new ChildCallback());

        for(int i =0; i < url.length; i++){
            childHandler.sendEmptyMessageDelayed(i, i*1000);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerThread.quit();
        mUiHandler.removeCallbacksAndMessages(null);
    }

    class ChildCallback implements Handler.Callback{

        @Override
        public boolean handleMessage(Message message) {
            Bitmap bitmap =downloadUrlBitmap(url[message.what]);
            Message msg = new Message();
            msg.what = message.what;
            msg.obj =bitmap;
            //通知主线程去更新UI
            mUiHandler.sendMessage(msg);
            return false;
        }
    }

    private Bitmap downloadUrlBitmap(String urlString) {
        HttpURLConnection urlConnection = null;
        BufferedInputStream in = null;
        Bitmap bitmap=null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
            bitmap= BitmapFactory.decodeStream(in);
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
}
