package com.zzq.handlerthread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Demo1 extends AppCompatActivity {

    Handler mainHandler, workHandler;
    HandlerThread mHandlerThread;
    TextView text;
    Button button1, button2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo1);

        text = findViewById(R.id.text);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);

        // 创建与主线程关联的Handler
        mainHandler = new Handler();
        //创建HandlerThread实例对象, 传入参数 = 线程名字, 作用 = 标记该线程
        mHandlerThread = new HandlerThread("HandlerThread");
        //启动线程
        mHandlerThread.start();

        //创建工作线程Handler & 复写handleMessage（）
        //作用：关联HandlerThread的Looper对象、实现消息处理操作 & 与其他线程进行通信
        //消息处理操作（HandlerMessage（））的执行线程 = mHandlerThread所创建的工作线程中执行
        workHandler = new Handler(mHandlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //设置了两种消息处理操作,通过msg来进行识别
                switch (msg.what) {
                    case 1:
                        try {
                            //延时操作
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // 通过主线程Handler.post方法进行在主线程的UI更新操作
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                              text.setText("Button1 clicked");
                            }
                        });
                        break;
                    case 2:
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                text.setText("Button2 clicked");
                            }
                        });
                        break;
                }
            }
        };


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //主线程中向子线程(mHandlerThread)发消息
                workHandler.sendEmptyMessage(1);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                workHandler.sendEmptyMessage(2);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandlerThread.quit(); // 退出消息循环
        workHandler.removeCallbacksAndMessages(null); // 防止Handler内存泄露 清空消息队列

    }
}
