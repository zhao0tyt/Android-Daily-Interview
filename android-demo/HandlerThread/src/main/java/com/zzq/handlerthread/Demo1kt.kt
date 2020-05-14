package com.zzq.handlerthread

import android.os.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_demo1.*

class Demo1kt : AppCompatActivity() {

    var mainHandler: Handler? = null
    var workHandler: Handler? = null
    var mHandlerThread: HandlerThread? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo1)

        // 创建与主线程关联的Handler
        mainHandler = Handler()
        //创建HandlerThread实例对象, 传入参数 = 线程名字, 作用 = 标记该线程
        mHandlerThread = HandlerThread("HandlerThread")
        //启动线程
        mHandlerThread!!.start()

        //创建工作线程Handler & 复写handleMessage（）
        //作用：关联HandlerThread的Looper对象、实现消息处理操作 & 与其他线程进行通信
        //消息处理操作（HandlerMessage（））的执行线程 = mHandlerThread所创建的工作线程中执行
        workHandler = object : Handler(mHandlerThread!!.looper) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                //设置了两种消息处理操作,通过msg来进行识别
                when (msg.what) {
                    1 -> {
                        try {
                            //延时操作
                            Thread.sleep(1000)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }

                        // 通过主线程Handler.post方法进行在主线程的UI更新操作
                        mainHandler!!.post { text.text = "Button1 clicked" }
                    }
                    2 -> {
                        try {
                            Thread.sleep(2000)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }

                        mainHandler!!.post { text.text = "Button2 clicked" }
                    }
                }
            }
        }


        button1.setOnClickListener {
            //主线程中向子线程(mHandlerThread)发消息
            workHandler!!.sendEmptyMessage(1)
        }

        button2.setOnClickListener { workHandler!!.sendEmptyMessage(2) }
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandlerThread!!.quit() // 退出消息循环
        workHandler!!.removeCallbacksAndMessages(null) // 防止Handler内存泄露 清空消息队列

    }
}