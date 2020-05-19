package com.zzq.intentservice

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.ImageView
import com.zzq.intentservice.MyIntentService.UpdateUI
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), UpdateUI {

    //    private val url = arrayOf(
//            "https://img-blog.csdn.net/20160903083245762",
//            "https://img-blog.csdn.net/20160903083252184",
//            "https://img-blog.csdn.net/20160903083257871",
//            "https://img-blog.csdn.net/20160903083311972",
//            "https://img-blog.csdn.net/20160903083319668",
//            "https://img-blog.csdn.net/20160903083326871")
//
//    private val mUiHandler = object : Handler() {
//        override fun handleMessage(msg: Message) {
//            val bitmap = msg.obj as Bitmap
//            image.setImageBitmap(bitmap)
//            super.handleMessage(msg)
//        }
//    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val intent = Intent(this, MyIntentService.javaClass)
//        for (i in url.indices) {
//            Log.d("zhaozhiqiang", "for")
//            intent.putExtra(MyIntentService.DOWNLOAD_URL,url[i])
//            intent.putExtra(MyIntentService.INDEX_FLAG,i)
//            startService(intent)
//        }
//        MyIntentService().setUpdateUI(this)
//    }
//
//    override fun update(message: Message) {
//        Log.d("zhaozhiqiang", "update")
//        mUiHandler.sendMessageDelayed(message, (message.what * 1000).toLong())
//    }
private val url = arrayOf("https://img-blog.csdn.net/20160903083245762",
            "https://img-blog.csdn.net/20160903083252184",
            "https://img-blog.csdn.net/20160903083257871",
            "https://img-blog.csdn.net/20160903083257871",
            "https://img-blog.csdn.net/20160903083311972",
            "https://img-blog.csdn.net/20160903083319668",
            "https://img-blog.csdn.net/20160903083326871")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, MyIntentService::class.java)
        for (i in 0..6) {//循环启动任务
            Log.d("zhaozhiqiang", "url="+url[i])
            intent.putExtra(MyIntentService.DOWNLOAD_URL,url[i])
            intent.putExtra(MyIntentService.INDEX_FLAG, i)
            startService(intent)
        }
        MyIntentService().setUpdateUI(this)
    }

    //必须通过Handler去更新，该方法为异步方法，不可更新UI
    override fun update(message: Message) {
        mUIHandler.sendMessageDelayed(message, (message.what * 1000).toLong())
    }


        private val mUIHandler = object : Handler() {
            override fun handleMessage(msg: Message) {
                image!!.setImageBitmap(msg.obj as Bitmap) }
        }

}
